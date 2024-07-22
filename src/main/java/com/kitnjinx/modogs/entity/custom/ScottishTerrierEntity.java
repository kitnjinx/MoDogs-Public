package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.ScottishTerrierVariant;
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

public class ScottishTerrierEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(ScottishTerrierEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_WHEATEN =
            SynchedEntityData.defineId(ScottishTerrierEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT;
    };

    public ScottishTerrierEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        ScottishTerrierEntity baby = ModEntityTypes.SCOTTISH_TERRIER.get().create(serverLevel);

        determineBabyVariant(baby, (ScottishTerrierEntity) otherParent);

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
        RawAnimation sitting = RawAnimation.begin().thenLoop("animation.scottish_terrier.sitting");
        RawAnimation angryWalk = RawAnimation.begin().thenLoop("animation.scottish_terrier.angrywalk");
        RawAnimation walk = RawAnimation.begin().thenLoop("animation.scottish_terrier.walk");
        RawAnimation angryIdle = RawAnimation.begin().thenLoop("animation.scottish_terrier.angryidle");
        RawAnimation idle = RawAnimation.begin().thenLoop("animation.scottish_terrier.idle");

        controllers.add(
                new AnimationController<>(this, 10, state ->
                        state.setAndContinue(this.isSitting() ? sitting :
                                (this.isAngry() || this.isAggressive() && state.isMoving()) ? angryWalk :
                                        state.isMoving() ? walk :(this.isAngry() || this.isAggressive()) ?
                                                angryIdle : idle))
        );
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

        Item itemForTaming = ModItems.RABBIT_TREAT.get();

        if (item == itemForTaming && !isTame()) {
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

                if (item == ModItems.GENE_TESTER.get()) {
                    if (this.level().isClientSide) {
                        Component message;
                        if (this.getVariant() == ScottishTerrierVariant.WHEATEN) {
                            message = Component.literal("This Scottish Terrier demonstrates a recessive trait.");
                        } else if (this.isCarrier()) {
                            message = Component.literal("This Scottish Terrier carries a recessive trait.");
                        } else {
                            message = Component.literal("This Scottish Terrier doesn't have any recessive traits.");
                        }
                    } else {
                        return InteractionResult.PASS;
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.entityData.set(CARRIES_WHEATEN, tag.getBoolean("CarriesWheaten"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("CarriesWheaten", this.isCarrier());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(CARRIES_WHEATEN, false);
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
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(26.0);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
        getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3D);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
    }

    /* VARIANTS */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(5) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 5) {
            var = 0; // BLACK
            setCarrier(carrier == 4);
        } else {
            var = 1; // WHEATEN
            setCarrier(true);
        }

        // assign chosen variant and finish the method
        ScottishTerrierVariant variant = ScottishTerrierVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    public ScottishTerrierVariant getVariant() {
        return ScottishTerrierVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(ScottishTerrierVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isCarrier() {
        return this.entityData.get(CARRIES_WHEATEN);
    }

    private void setCarrier(boolean status) {
        this.entityData.set(CARRIES_WHEATEN, status);
    }

    private void determineBabyVariant(ScottishTerrierEntity baby, ScottishTerrierEntity otherParent) {
        // determine if baby is wheaten, a carrier, or black
        if (this.getVariant() == ScottishTerrierVariant.WHEATEN && otherParent.getVariant() == ScottishTerrierVariant.WHEATEN) {
            // if both parents are wheaten, baby will be wheaten
            baby.setVariant(ScottishTerrierVariant.WHEATEN);
            baby.setCarrier(true);
        } else if ((this.getVariant() == ScottishTerrierVariant.WHEATEN && otherParent.isCarrier()) ||
                (this.isCarrier() && otherParent.getVariant() == ScottishTerrierVariant.WHEATEN)) {
            // if one parent is wheaten and the other is a carry, baby has 50% chance to be wheaten and 50%
            // chance to be a carrier
            baby.setCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(ScottishTerrierVariant.WHEATEN);
            } else {
                baby.setVariant(ScottishTerrierVariant.BLACK);
            }
        } else if (this.getVariant() == ScottishTerrierVariant.WHEATEN || otherParent.getVariant() == ScottishTerrierVariant.WHEATEN) {
            // if only one parent is wheaten, baby will be a carrier
            baby.setVariant(ScottishTerrierVariant.BLACK);
            baby.setCarrier(true);
        } else if (this.isCarrier() && otherParent.isCarrier()) {
            // if both parents are carriers, baby has 25% not to carry, 50% chance to be a carrier, and 25%
            // chance to be wheaten
            int determine = this.random.nextInt(4) + 1;
            baby.setCarrier(determine > 1);
            if (determine < 4) {
                baby.setVariant(ScottishTerrierVariant.BLACK);
            } else {
                baby.setVariant(ScottishTerrierVariant.WHEATEN);
            }
        } else if (this.isCarrier() || otherParent.isCarrier()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setVariant(ScottishTerrierVariant.BLACK);
            baby.setCarrier(this.random.nextBoolean());
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setVariant(ScottishTerrierVariant.BLACK);
            baby.setCarrier(false);
        }
    }
}