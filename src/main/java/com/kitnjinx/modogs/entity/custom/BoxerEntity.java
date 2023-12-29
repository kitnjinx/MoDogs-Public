package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.BoxerVariant;
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

public class BoxerEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BoxerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHADE =
            SynchedEntityData.defineId(BoxerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_BLACK =
            SynchedEntityData.defineId(BoxerEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COW || entitytype == EntityType.MOOSHROOM;
    };

    public BoxerEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        BoxerEntity baby = ModEntityTypes.BOXER.get().create(serverLevel);

        determineBabyVariant(baby, (BoxerEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends BoxerEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.boxer.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.boxer.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.boxer.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.boxer.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.boxer.idle"));
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

        Item itemForTaming = ModItems.BEEF_TREAT.get();

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
                if (this.getVariant() == BoxerVariant.BLACK) {
                    if (this.getShade() == 0) {
                        message = Component.literal("This Boxer demonstrates a recessive trait. They also have alleles for medium-colored fur.");
                    } else if (this.getShade() == 1) {
                        message = Component.literal("This Boxer demonstrates a recessive trait. They also have alleles for light-colored fur.");
                    } else {
                        message = Component.literal("This Boxer demonstrates a recessive trait. They also have alleles for dark-colored fur.");
                    }
                } else if (this.getCarrier()) {
                    message = Component.literal("This Boxer carries a recessive trait.");
                } else {
                    message = Component.literal("This Boxer doesn't have any recessive traits.");
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
        this.entityData.set(SHADE, tag.getInt("Shade"));
        this.entityData.set(CARRIES_BLACK, tag.getBoolean("CarrierStatus"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("Shade", this.getShade());
        tag.putBoolean("CarrierStatus", this.getCarrier());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(SHADE, 0);
        this.entityData.define(CARRIES_BLACK, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group,
                                        @Nullable CompoundTag tag) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(10) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 7) {
            var = 0; // MEDIUM
            setShade(0);
        } else if (determine < 10) {
            if (random.nextBoolean()) {
                var = 1; // LIGHT
                setShade(1);
            } else {
                var = 2; // DARK
                setShade(2);
            }
        } else {
            var = 3; // BLACK
            if (random.nextInt(9) + 1 < 7) {
                setShade(0);
            } else if (random.nextBoolean()) {
                setShade(1);
            } else {
                setShade(2);
            }
        }

        setCarrier(var == 3 || carrier == 4);

        // assign chosen variant and finish the method
        BoxerVariant variant = BoxerVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public BoxerVariant getVariant() {
        return BoxerVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BoxerVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public int getShade() {
        return this.entityData.get(SHADE);
    }

    private void setShade(int shade) {
        this.entityData.set(SHADE, shade);
    }

    public boolean getCarrier() {
        return this.entityData.get(CARRIES_BLACK);
    }

    private void setCarrier(boolean status) {
        this.entityData.set(CARRIES_BLACK, status);
    }

    private void determineBabyVariant(BoxerEntity baby, BoxerEntity otherParent) {
        // determine what shade (Light, Medium, or Dark) the baby has based on its parents
        if (this.getShade() == 0 && otherParent.getShade() == 0) {
            // if both parents are Medium, baby has 25% chance to be Light, 50% chance to be Medium, and 25%
            // chance to be Dark
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setShade(1);
            } else if (determine < 4) {
                baby.setShade(0);
            } else {
                baby.setShade(2);
            }
        } else if (this.getShade() == otherParent.getShade()) {
            // if both parents are Light or both are Dark, baby will match them
            baby.setShade(this.getShade());
        } else if ((this.getShade() == 1 && otherParent.getShade() == 2) ||
                (this.getShade() == 2 && otherParent.getShade() == 1)) {
            // if one parent is Light and the other is Dark, baby will be Medium
            baby.setShade(0);
        } else {
            // if none of hte conditions above are true, the baby will match one of its parents
            if (this.random.nextBoolean()) {
                baby.setShade(this.getShade());
            } else {
                baby.setShade(otherParent.getShade());
            }
        }

        // determine if baby is Black, carries Black and shows its Shade, or just shows its Shade
        if (this.getVariant() == BoxerVariant.BLACK && otherParent.getVariant() == BoxerVariant.BLACK) {
            // if both parents are Black, baby will be black and marked as a carrier
            baby.setVariant(BoxerVariant.BLACK);
            baby.setCarrier(true);
        } else if ((this.getVariant() == BoxerVariant.BLACK && otherParent.getCarrier()) ||
                (this.getCarrier() && otherParent.getVariant() == BoxerVariant.BLACK)) {
            // if one parent is black and the other is a carrier, baby has 50% chance to be black and 50%
            // chance to be a carrier
            baby.setCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(BoxerVariant.BLACK);
            } else {
                baby.setVariant(BoxerVariant.byId(baby.getShade()));
            }
        } else if (this.getVariant() == BoxerVariant.BLACK || otherParent.getVariant() == BoxerVariant.BLACK) {
            // if one parent is black and the other is not a carrier, baby will be a carrier
            baby.setVariant(BoxerVariant.byId(baby.getShade()));
            baby.setCarrier(true);
        } else if (this.getCarrier() && otherParent.getCarrier()) {
            // if both parents are carriers, baby has 25% not to carry, 50% chance to be a carrier, and 25%
            // chance to be Black
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setVariant(BoxerVariant.byId(baby.getShade()));
                baby.setCarrier(false);
            } else if (determine < 4) {
                baby.setVariant(BoxerVariant.byId(baby.getShade()));
                baby.setCarrier(true);
            } else {
                baby.setVariant(BoxerVariant.BLACK);
                baby.setCarrier(true);
            }
        } else if (this.getCarrier() || otherParent.getCarrier()) {
            // if only one parent is a carrier, baby has 50% chance to be a carrier
            baby.setVariant(BoxerVariant.byId(baby.getShade()));
            baby.setCarrier(this.random.nextBoolean());
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setVariant(BoxerVariant.byId(baby.getShade()));
            baby.setCarrier(false);
        }
    }
}