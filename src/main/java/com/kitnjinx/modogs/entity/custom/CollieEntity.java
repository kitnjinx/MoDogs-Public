package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollieVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.Random;
import java.util.function.Predicate;

public class CollieEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(CollieEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_BLACK =
            SynchedEntityData.defineId(CollieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BLACK =
            SynchedEntityData.defineId(CollieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_MERLE =
            SynchedEntityData.defineId(CollieEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.SHEEP;
    };

    public CollieEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.ARMOR, 0.0f)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f).build();
    }

    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        CollieEntity baby = ModEntityTypes.COLLIE.get().create(serverLevel);

        determineBabyVariant(baby, (CollieEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends CollieEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.collie.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.collie.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.collie.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.collie.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.collie.idle"));
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    protected float getSoundVolume(){
        return 0.2F;
    }

    /* Tamable */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = ModItems.MUTTON_TREAT.get();

        if (item == itemForTaming && !isTame()) {
            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    if (!this.level.isClientSide) {
                        super.tame(player);
                        this.navigation.recomputePath();
                        this.setTarget(null);
                        this.level.broadcastEntityEvent(this, (byte)7);
                        setSitting(true);
                        this.setHealth(this.getMaxHealth());
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        if (item == ModItems.GENE_TESTER.get()) {
            if (this.level.isClientSide) {
                Component message;
                if (this.isBlack() && this.isMerle()) {
                    message = Component.literal("This Collie demonstrates a recessive trait and the merle trait.");
                } else if (this.isBlack()) {
                    message = Component.literal("This Collie demonstrates a recessive trait.");
                } else if (this.carriesBlack() && this.isMerle()) {
                    message = Component.literal("This Collie demonstrates the merle trait and carries a recessive trait.");
                } else if (this.carriesBlack()) {
                    message = Component.literal("This Collie carries a recessive trait.");
                } else if (this.isMerle()) {
                    message = Component.literal("This Collie demonstrates the merle trait.");
                } else {
                    message = Component.literal("This Collie doesn't have any recessive traits.");
                }

                player.sendSystemMessage(message);

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.entityData.set(IS_BLACK, tag.getBoolean("IsBlack"));
        this.entityData.set(CARRIES_BLACK, tag.getBoolean("CarriesBlack"));
        this.entityData.set(IS_MERLE, tag.getBoolean("IsMerle"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsBlack", this.isBlack());
        tag.putBoolean("CarriesBlack", this.carriesBlack());
        tag.putBoolean("IsMerle", this.isMerle());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_BLACK, false);
        this.entityData.define(CARRIES_BLACK, false);
        this.entityData.define(IS_MERLE, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(2D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group,
                                        @Nullable CompoundTag tag) {
        // Variables for determining the variant
        Random r = new Random();
        int merle = r.nextInt(5) + 1;
        int determine = r.nextInt(6) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (merle < 5) {
            setMerle(false);
            if (determine < 6) {
                var = 0; // SABLE
                setBlackStatus(carrier == 4, false);
            } else {
                var = 1; // BLACK_TAN
                setBlackStatus(true, true);
            }
        } else {
            setMerle(true);
            if (determine < 6) {
                var = 2; // SABLE_MERLE
                setBlackStatus(carrier == 4, false);
            } else {
                var = 3; // BLUE_TAN_MERLE
                setBlackStatus(true, true);
            }
        }

        // assign chosen variant and finish the method
        CollieVariant variant = CollieVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public CollieVariant getVariant() {
        return CollieVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(CollieVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isBlack() {
        return this.entityData.get(IS_BLACK);
    }

    public boolean carriesBlack() {
        return this.entityData.get(CARRIES_BLACK);
    }

    private void setBlackStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_BLACK, carrier);
        this.entityData.set(IS_BLACK, is);
    }

    public boolean isMerle() {
        return this.entityData.get(IS_MERLE);
    }

    private void setMerle(boolean status) {
        this.entityData.set(IS_MERLE, status);
    }

    private void determineBabyVariant(CollieEntity baby, CollieEntity otherParent) {
        // determine if baby is black (and tan) or sable
        if (this.isBlack() && otherParent.isBlack()) {
            // if both parents are black, baby will be black
            baby.setBlackStatus(true, true);
        } else if ((this.isBlack() && otherParent.carriesBlack()) || (this.carriesBlack() && otherParent.isBlack())) {
            // if one parent is black and the other is a carrier, baby has 50% chance to be black and
            // 50% chance to be a carrier
            baby.setBlackStatus(true, this.random.nextBoolean());
        } else if (this.isBlack() || otherParent.isBlack()) {
            // if only one parent is black, baby will be a carrier
            baby.setBlackStatus(true, false);
        } else if (this.carriesBlack() && otherParent.carriesBlack()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be black
            int determine = this.random.nextInt(4) + 1;
            baby.setBlackStatus(determine > 1, determine == 4);
        } else if (this.carriesBlack() || otherParent.carriesBlack()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setBlackStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent has black, baby will not have black
            baby.setBlackStatus(false, false);
        }

        // determine if baby is merle or not
        if (this.isMerle() && otherParent.isMerle()) {
            // if both parents are merle, baby will be merle
            baby.setMerle(true);
        } else if (this.isMerle() || otherParent.isMerle()) {
            // if only one parent is merle, baby has 50/50 chance to be merle
            baby.setMerle(this.random.nextBoolean());
        } else {
            // if neither parent is merle, baby will not be merle
            baby.setMerle(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isMerle() && baby.isBlack()) {
            baby.setVariant(CollieVariant.BLUE_TAN_MERLE);
        } else if (baby.isMerle()) {
            baby.setVariant(CollieVariant.SABLE_MERLE);
        } else if (baby.isBlack()) {
            baby.setVariant(CollieVariant.BLACK_TAN);
        } else {
            baby.setVariant(CollieVariant.SABLE);
        }
    }
}