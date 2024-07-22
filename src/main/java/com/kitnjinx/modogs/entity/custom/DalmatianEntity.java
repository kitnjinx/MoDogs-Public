package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.DalmatianVariant;
import com.kitnjinx.modogs.item.ModItems;
import net.minecraft.Util;
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

public class DalmatianEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> SPOT_PATTERN =
            SynchedEntityData.defineId(DalmatianEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_BROWN =
            SynchedEntityData.defineId(DalmatianEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_BROWN =
            SynchedEntityData.defineId(DalmatianEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT || entitytype == EntityType.PIG;
    };

    public DalmatianEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        DalmatianEntity baby = ModEntityTypes.DALMATIAN.get().create(serverLevel);

        determineBabyVariant(baby, (DalmatianEntity) otherParent);
        assert baby != null;

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
        RawAnimation sitting = RawAnimation.begin().thenLoop("animation.dalmatian.sitting");
        RawAnimation angryWalk = RawAnimation.begin().thenLoop("animation.dalmatian.angrywalk");
        RawAnimation walk = RawAnimation.begin().thenLoop("animation.dalmatian.walk");
        RawAnimation angryIdle = RawAnimation.begin().thenLoop("animation.dalmatian.angryidle");
        RawAnimation idle = RawAnimation.begin().thenLoop("animation.dalmatian.idle");

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

    /* Tamable */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = ModItems.RABBIT_TREAT.get();
        Item itemForTaming2 = ModItems.BACON_TREAT.get();

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
                if (this.isBrown()) {
                    message = Component.literal("This Dalmatian demonstrates a recessive trait.");
                } else if (this.getCarrier()) {
                    message = Component.literal("This Dalmatian carries a recessive trait.");
                } else {
                    message = Component.literal("This Dalmatian doesn't have any recessive traits.");
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
        this.entityData.set(SPOT_PATTERN, tag.getInt("Pattern"));
        this.entityData.set(CARRIES_BROWN, tag.getBoolean("CarrierStatus"));
        this.entityData.set(IS_BROWN, tag.getBoolean("IsBrown"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Pattern", this.getPattern());
        tag.putBoolean("CarrierStatus", this.getCarrier());
        tag.putBoolean("IsBrown", this.isBrown());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPOT_PATTERN, 0);
        builder.define(CARRIES_BROWN, false);
        builder.define(IS_BROWN, false);
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
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(22.0);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5D);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
    }

    /* VARIANTS */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(13) + 1;
        int carrier = r.nextInt(4) + 1;

        setBrownStatus(determine == 13 || carrier == 4, determine == 13); // if dog is brown or rolled to be a carrier, boolean is true

        // assign chosen variant and finish the method
        DalmatianVariant variant = Util.getRandom(DalmatianVariant.values(), this.random);
        setPattern(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    public DalmatianVariant getPatternVariant() {
        return DalmatianVariant.byId(this.getPattern() & 255);
    }

    private int getPattern() {
        return this.entityData.get(SPOT_PATTERN);
    }

    private void setPattern(DalmatianVariant variant) {
        this.entityData.set(SPOT_PATTERN, variant.getId() & 255);
    }

    public boolean getCarrier() {
        return this.entityData.get(CARRIES_BROWN);
    }
    public boolean isBrown() {
        return this.entityData.get(IS_BROWN);
    }

    private void setBrownStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_BROWN, carrier);
        this.entityData.set(IS_BROWN, is);
    }

    private void determineBabyVariant(DalmatianEntity baby, DalmatianEntity otherParent) {
        // determine if baby is brown, a carrier, or black
        if (this.isBrown() && otherParent.isBrown()) {
            // if both parents are brown, baby will be brown
            baby.setBrownStatus(true, true);
        } else if ((this.isBrown() && otherParent.getCarrier()) || (this.getCarrier() && otherParent.isBrown())) {
            // if one parent is brown and the other a carrier, the baby has 50% chance to be a carrier and
            // 50% chance to be brown
            baby.setBrownStatus(true, this.random.nextBoolean());
        } else if (this.isBrown() || otherParent.isBrown()) {
            // if one parent is brown and the other is not a carrier, the baby will be a carrier
            baby.setBrownStatus(true, false);
        } else if (this.getCarrier() && otherParent.getCarrier()) {
            // if both parents are a carrier, baby has 25% chance not to be a carrier, 50% to be a carrier, and
            // 25% to be brown.
            int determine = this.random.nextInt(4) + 1;
            baby.setBrownStatus(determine > 1, determine == 4);
        } else if (this.getCarrier() || otherParent.getCarrier()) {
            // if one parent is a carrier, baby has a 50% chance to be a carrier
            baby.setBrownStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby won't be a carrier
            baby.setBrownStatus(false, false);
        }

        // determine baby's spot pattern
        if (this.getPattern() == otherParent.getPattern()) {
            baby.setPattern(this.getPatternVariant());
        } else {
            baby.setPattern(Util.getRandom(DalmatianVariant.values(), this.random));
        }
    }
}