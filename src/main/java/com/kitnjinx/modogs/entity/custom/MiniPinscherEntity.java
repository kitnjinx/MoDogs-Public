package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.MiniPinscherVariant;
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
import net.minecraft.world.entity.monster.Monster;
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

public class MiniPinscherEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(MiniPinscherEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_BLACK =
            SynchedEntityData.defineId(MiniPinscherEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BLACK =
            SynchedEntityData.defineId(MiniPinscherEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DILUTE =
            SynchedEntityData.defineId(MiniPinscherEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_DILUTE =
            SynchedEntityData.defineId(MiniPinscherEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.CHICKEN || entitytype == EntityType.RABBIT || entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER;
    };

    public MiniPinscherEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.ARMOR, 0.0f)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f).build();
    }

    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Monster.class, true, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        MiniPinscherEntity baby = ModEntityTypes.MINI_PINSCHER.get().create(serverLevel);

        determineBabyVariant(baby, (MiniPinscherEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends MiniPinscherEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mini_pinscher.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mini_pinscher.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mini_pinscher.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mini_pinscher.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mini_pinscher.idle"));
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    protected float getSoundVolume(){
        return 0.1F;
    }

    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.75F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.25F;
    }

    /* Tamable */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = ModItems.CHICKEN_TREAT.get();
        Item itemForTaming2 = ModItems.RABBIT_TREAT.get();

        if ((item == itemForTaming || item == itemForTaming2) && !isTame()) {
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
                if (this.isBlack()) {
                    if (this.isDilute()) {
                        message = Component.literal("This Miniature Pinscher demonstrates two recessive traits.");
                    } else if (this.carriesDilute()) {
                        message = Component.literal("This Miniature Pinscher demonstrates the recessive black and tan fur trait and carries the dilution trait.");
                    } else {
                        message = Component.literal("This Miniature Pinscher demonstrates the recessive black and tan fur trait.");
                    }
                } else if (this.carriesBlack()) {
                    if (this.isDilute()) {
                        message = Component.literal("This Miniature Pinscher carries the black and tan fur trait, and has the alleles for diluted black.");
                    } else if (this.carriesDilute()) {
                        message = Component.literal("This Miniature Pinscher carries two recessive traits.");
                    } else {
                        message = Component.literal("This Miniature Pinscher carries the black and tan fur trait.");
                    }
                } else if (this.isDilute()) {
                    message = Component.literal("This Miniature Pinscher has the alleles for diluted black.");
                } else if (this.carriesDilute()) {
                    message = Component.literal("This Miniature Pinscher carries the dilution trait.");
                } else {
                    message = Component.literal("This Miniature Pinscher doesn't have any recessive traits.");
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
        this.entityData.set(IS_DILUTE, tag.getBoolean("IsDilute"));
        this.entityData.set(CARRIES_DILUTE, tag.getBoolean("CarriesDilute"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsBlack", this.isBlack());
        tag.putBoolean("CarriesBlack", this.carriesBlack());
        tag.putBoolean("IsDilute", this.isDilute());
        tag.putBoolean("CarriesDilute", this.carriesDilute());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_BLACK, true);
        this.entityData.define(CARRIES_BLACK, true);
        this.entityData.define(IS_DILUTE, false);
        this.entityData.define(CARRIES_DILUTE, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(26.0);
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
        int determine = r.nextInt(14) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 7) {
            var = 0; // BLACK_TAN
            setBlackStatus(true, true);
            setDiluteStatus(carrier == 1 && r.nextBoolean(), false);
        } else if (determine < 12) {
            var = 1; // RED
            if (carrier == 1 && r.nextBoolean()) {
                setBlackStatus(true, false);
                setDiluteStatus(false, false);
            } else if (carrier == 1) {
                setDiluteStatus(true, false);
                setBlackStatus(false, false);
            } else {
                setDiluteStatus(false, false);
                setBlackStatus(false, false);
            }
        } else {
            var = 2; // CHOCOLATE_TAN
            setBlackStatus(true, true);
            setDiluteStatus(true, true);
        }

        // assign chosen variant and finish the method
        MiniPinscherVariant variant = MiniPinscherVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public MiniPinscherVariant getVariant() {
        return MiniPinscherVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(MiniPinscherVariant variant) {
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

    public boolean isDilute() {
        return this.entityData.get(IS_DILUTE);
    }

    public boolean carriesDilute() {
        return this.entityData.get(CARRIES_DILUTE);
    }

    private void setDiluteStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_DILUTE, carrier);
        this.entityData.set(IS_DILUTE, is);
    }

    private void determineBabyVariant(MiniPinscherEntity baby, MiniPinscherEntity otherParent) {
        // determine if baby is black & tan, a carrier, or red
        if (this.isBlack() && otherParent.isBlack()) {
            // if both parents are black, baby will be black
            baby.setBlackStatus(true, true);
        } else if ((this.isBlack() && otherParent.carriesBlack()) || (this.carriesBlack() && otherParent.isBlack())) {
            // if one parent is black and one is a carrier, baby has 50% chance to be black and 50% chance to
            // be a carrier
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
            // if neither parent is a carrier, baby will not have black
            baby.setBlackStatus(false, false);
        }

        // determine if baby is dilute, a carrier, or neither
        if (this.isDilute() && otherParent.isDilute()) {
            // if both parents are dilutes, baby will be dilute
            baby.setDiluteStatus(true, true);
        } else if ((this.isDilute() && otherParent.carriesDilute()) || (this.carriesDilute() && otherParent.isDilute())) {
            // if one parent is dilute and the other is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be dilute
            baby.setDiluteStatus(true, this.random.nextBoolean());
        } else if (this.isDilute() || otherParent.isDilute()) {
            // if only one parent is dilute, baby will be a carrier
            baby.setDiluteStatus(true, false);
        } else if (this.carriesDilute() && otherParent.carriesDilute()) {
            // if both parents carry dilute, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be dilute
            int determine = this.random.nextInt(4) + 1;
            baby.setDiluteStatus(determine > 1, determine == 4);
        } else if (this.carriesDilute() || otherParent.carriesDilute()) {
            // if only one parent carries dilute, baby has 50/50 chance to be a carrier
            baby.setDiluteStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setDiluteStatus(false, false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isBlack() && baby.isDilute()) {
            baby.setVariant(MiniPinscherVariant.CHOCOLATE_TAN);
        } else if (this.isBlack()) {
            baby.setVariant(MiniPinscherVariant.BLACK_TAN);
        } else {
            baby.setVariant(MiniPinscherVariant.RED);
        }
    }
}