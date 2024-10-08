package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.ShibaInuVariant;
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
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Random;
import java.util.function.Predicate;

public class ShibaInuEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(ShibaInuEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_BLACK =
            SynchedEntityData.defineId(ShibaInuEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BLACK =
            SynchedEntityData.defineId(ShibaInuEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_CREAM =
            SynchedEntityData.defineId(ShibaInuEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_CREAM =
            SynchedEntityData.defineId(ShibaInuEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT || entitytype == EntityType.FOX || entitytype == EntityType.CHICKEN;
    };

    public ShibaInuEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 3.0f)
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
        ShibaInuEntity baby = ModEntityTypes.SHIBA_INU.get().create(serverLevel);

        determineBabyVariant(baby, (ShibaInuEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true, true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        RawAnimation sitting = RawAnimation.begin().thenLoop("animation.shiba_inu.sitting");
        RawAnimation angryWalk = RawAnimation.begin().thenLoop("animation.shiba_inu.angrywalk");
        RawAnimation walk = RawAnimation.begin().thenLoop("animation.shiba_inu.walk");
        RawAnimation angryIdle = RawAnimation.begin().thenLoop("animation.shiba_inu.angryidle");
        RawAnimation idle = RawAnimation.begin().thenLoop("animation.shiba_inu.idle");

        controllers.add(
                new AnimationController<>(this, 10, state ->
                        state.setAndContinue(this.isSitting() ? sitting :
                                (this.isAngry() || this.isAggressive() && state.isMoving()) ? angryWalk :
                                        state.isMoving() ? walk :(this.isAngry() || this.isAggressive()) ?
                                                angryIdle : idle))
        );
    }

    protected float getSoundVolume(){
        return 0.2F;
    }

    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.75F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.25F;
    }

    /* Tamable */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = ModItems.RABBIT_TREAT.get();
        Item itemForTaming2 = ModItems.CHICKEN_TREAT.get();

        if ((item == itemForTaming || item == itemForTaming2) && !isTame()) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
                    if (!this.level().isClientSide) {
                        super.tame(player);
                        this.navigation.recomputePath();
                        this.setTarget(null);
                        this.level().broadcastEntityEvent(this, (byte)7);
                        setSitting(true);
                        this.setHealth(this.getMaxHealth());
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        if (item == ModItems.GENE_TESTER.get()) {
            if (this.level().isClientSide) {
                Component message;
                if (this.isBlack() && this.isCream()) {
                    message = Component.literal("This Shiba Inu demonstrates two recessive traits.");
                } else if (this.isBlack() && this.carriesCream()) {
                    message = Component.literal("This Shiba Inu demonstrates the recessive black fur trait, and carries the dilution trait.");
                } else if (this.isBlack()) {
                    message = Component.literal("This Shiba Inu demonstrates the recessive black fur trait.");
                } else if (this.isCream() && this.carriesBlack()) {
                    message = Component.literal("This Shiba Inu demonstrates the recessive dilution trait, and carries the black fur trait.");
                } else if (this.isCream()) {
                    message = Component.literal("This Shiba Inu demonstrates the recessive dilution trait.");
                } else if (this.carriesBlack() && this.carriesCream()) {
                    message = Component.literal("This Shiba Inu carries two recessive traits.");
                } else if (this.carriesBlack()) {
                    message = Component.literal("This Shiba Inu carries the recessive black fur trait.");
                } else if (this.carriesCream()) {
                    message = Component.literal("This Shiba Inu carries the recessive dilution trait.");
                } else {
                    message = Component.literal("This Shiba Inu doesn't have any recessive traits.");
                }
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
        this.entityData.set(IS_CREAM, tag.getBoolean("IsCream"));
        this.entityData.set(CARRIES_CREAM, tag.getBoolean("CarriesCream"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsBlack", this.isBlack());
        tag.putBoolean("CarriesBlack", this.carriesBlack());
        tag.putBoolean("IsCream", this.isCream());
        tag.putBoolean("CarriesCream", this.carriesCream());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(IS_BLACK, false);
        builder.define(CARRIES_BLACK, false);
        builder.define(IS_CREAM, false);
        builder.define(CARRIES_CREAM, false);
    }

    @Override
    public void setTame (boolean tamed, boolean applyTamingSideEffects) {
        super.setTame(tamed, applyTamingSideEffects);
        if (applyTamingSideEffects) {
            this.applyTamingSideEffects();
        }
    }

    @Override
    protected void applyTamingSideEffects() {
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5D);
        getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3.5);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
    }

    /* VARIANTS */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(10) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 7) {
            var = 0; // RED
            setBlackStatus(carrier == 1, false);
            setCreamStatus(carrier == 2, false);
        } else if (determine < 10) {
            var = 1; // BLACK_TAN
            setBlackStatus(true, true);
            setCreamStatus(carrier < 3, false);
        } else {
            setCreamStatus(true, true);
            if (r.nextInt(3) + 1 < 3) {
                var = 2; // CREAM
                setBlackStatus(carrier == 1, false);
            } else {
                var = 3; // DARK_CREAM
                setBlackStatus(true, true);
            }
        }

        // assign chosen variant and finish the method
        ShibaInuVariant variant = ShibaInuVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    public ShibaInuVariant getVariant() {
        return ShibaInuVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(ShibaInuVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isBlack() {
        return this.entityData.get(IS_BLACK);
    }

    public boolean carriesBlack() {
        return this.entityData.get(CARRIES_BLACK);
    }

    private void setBlackStatus(boolean carries, boolean is) {
        this.entityData.set(CARRIES_BLACK, carries);
        this.entityData.set(IS_BLACK, is);
    }

    public boolean isCream() {
        return this.entityData.get(IS_CREAM);
    }

    public boolean carriesCream() {
        return this.entityData.get(CARRIES_CREAM);
    }

    private void setCreamStatus(boolean carries, boolean is) {
        this.entityData.set(CARRIES_CREAM, carries);
        this.entityData.set(IS_CREAM, is);
    }

    private void determineBabyVariant(ShibaInuEntity baby, ShibaInuEntity otherParent) {
        // determine if baby is red or black_tan (referred to as black for simplicity)
        if (this.isBlack() && otherParent.isBlack()) {
            // if both parents are black, baby will be black
            baby.setBlackStatus(true, true);
        } else if ((this.isBlack() && otherParent.carriesBlack()) || (this.carriesBlack() && otherParent.isBlack())) {
            // if one parent is black and the other is a carrier, baby has 50% chance to be black and 50% chance
            // to be a carrier
            baby.setBlackStatus(true, this.random.nextBoolean());
        } else if (this.isBlack() || otherParent.isBlack()) {
            // if one parent is black and the other is not a carrier, baby will be a carrier
            baby.setBlackStatus(true, false);
        } else if (this.carriesBlack() && otherParent.carriesBlack()) {
            // if both parents carry black, baby has 25% chance not to carry, 50% to be a carrier, and 25% chance
            // to be black
            int determine = this.random.nextInt(4) + 1;
            baby.setBlackStatus(determine > 1, determine == 4);
        } else if (this.carriesBlack() || otherParent.carriesBlack()) {
            // if only one parent carries black, baby has 50/50 chance to be a carrier
            baby.setBlackStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent carries black, baby will not carry black
            baby.setBlackStatus(false, false);
        }

        // determine if baby is cream or not
        if (this.isCream() && otherParent.isCream()) {
            // if both parents are cream, baby will be cream
            baby.setCreamStatus(true, true);
        } else if ((this.isCream() && otherParent.carriesCream()) || (this.carriesCream() && otherParent.isCream())) {
            // if one parent is cream and one is a carrier, baby has 50% chance to be cream and 50% chance to be
            // a carrier
            baby.setCreamStatus(true, this.random.nextBoolean());
        } else if (this.isCream() || otherParent.isCream()) {
            // if only one parent is cream and the other is not a carrier, baby will be a carrier
            baby.setCreamStatus(true, false);
        } else if (this.carriesCream() && otherParent.carriesCream()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be cream
            int determine = this.random.nextInt(4) + 1;
            baby.setCreamStatus(determine > 1, determine == 4);
        } else if (this.carriesCream() || otherParent.carriesCream()) {
            // if only one parent carries cream, baby has 50/50 chance to be a carrier
            baby.setCreamStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not have cream
            baby.setCreamStatus(false, false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isCream() && baby.isBlack()) {
            baby.setVariant(ShibaInuVariant.DARK_CREAM);
        } else if (baby.isCream()) {
            baby.setVariant(ShibaInuVariant.CREAM);
        } else if (baby.isBlack()) {
            baby.setVariant(ShibaInuVariant.BLACK_TAN);
        } else {
            baby.setVariant(ShibaInuVariant.RED);
        }
    }
}