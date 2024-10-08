package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.PugVariant;
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

public class PugEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(PugEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_BLACK =
            SynchedEntityData.defineId(PugEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.PIG;
    };

    public PugEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0f)
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
        PugEntity baby = ModEntityTypes.PUG.get().create(serverLevel);

        determineBabyVariant(baby, (PugEntity) otherParent);

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
        RawAnimation sitting = RawAnimation.begin().thenLoop("animation.pug.sitting");
        RawAnimation angryWalk = RawAnimation.begin().thenLoop("animation.pug.angrywalk");
        RawAnimation walk = RawAnimation.begin().thenLoop("animation.pug.walk");
        RawAnimation angryIdle = RawAnimation.begin().thenLoop("animation.pug.angryidle");
        RawAnimation idle = RawAnimation.begin().thenLoop("animation.pug.idle");

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

        Item itemForTaming = ModItems.BACON_TREAT.get();

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

                return InteractionResult.SUCCESS;
            }
        }

        if (item == ModItems.GENE_TESTER.get()) {
            if (this.level().isClientSide) {
                Component message;
                if (this.getVariant() == PugVariant.BLACK) {
                    message = Component.literal("This Pug demonstrates a recessive trait.");
                } else if (this.isCarrier()) {
                    message = Component.literal("This Pug carries a recessive trait.");
                } else {
                    message = Component.literal("This Pug doesn't have any recessive traits.");
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
        this.entityData.set(CARRIES_BLACK, tag.getBoolean("CarriesBlack"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("CarriesBlack", this.isCarrier());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(CARRIES_BLACK, false);
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
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
    }

    /* VARIANTS */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(6) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 6) {
            var = 0; // FAWN
        } else {
            var = 1; // BLACK
        }

        setCarrier(carrier == 4 || var == 1);

        // assign chosen variant and finish the method
        PugVariant variant = PugVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    public PugVariant getVariant() {
        return PugVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(PugVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isCarrier() {
        return this.entityData.get(CARRIES_BLACK);
    }

    private void setCarrier(boolean carrier) {
        this.entityData.set(CARRIES_BLACK, carrier);
    }

    private void determineBabyVariant(PugEntity baby, PugEntity otherParent) {
        if (this.getVariant() == PugVariant.BLACK && otherParent.getVariant() == PugVariant.BLACK) {
            // if both parents are black, baby will be black
            baby.setCarrier(true);
            baby.setVariant(PugVariant.BLACK);
        } else if ((this.getVariant() == PugVariant.BLACK && otherParent.isCarrier()) ||
                (this.isCarrier() && otherParent.getVariant() == PugVariant.BLACK)) {
            // if one parent is black and the other is a carrier, baby has 50% chance to be black and 50% chance
            // to be a carrier
            baby.setCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(PugVariant.BLACK);
            } else {
                baby.setVariant(PugVariant.FAWN);
            }
        } else if (this.getVariant() == PugVariant.BLACK || otherParent.getVariant() == PugVariant.BLACK) {
            // if only one parent is black, baby will be a carrier
            baby.setCarrier(true);
            baby.setVariant(PugVariant.FAWN);
        } else if (this.isCarrier() && otherParent.isCarrier()) {
            // if both parents are carriers, baby has 25% chance not to carry black, 50% chance to be a carrier,
            // and 25% chance to be black
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setCarrier(false);
                baby.setVariant(PugVariant.FAWN);
            } else if (determine < 4) {
                baby.setCarrier(true);
                baby.setVariant(PugVariant.FAWN);
            } else {
                baby.setCarrier(true);
                baby.setVariant(PugVariant.BLACK);
            }
        } else if (this.isCarrier() || otherParent.isCarrier()) {
            // if only one parent is a carrier, baby will have 50/50 chance to be a carrier
            baby.setCarrier(this.random.nextBoolean());
            baby.setVariant(PugVariant.FAWN);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setCarrier(false);
            baby.setVariant(PugVariant.FAWN);
        }
    }
}