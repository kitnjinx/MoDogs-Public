package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.DobermanVariant;
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
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;
import java.util.function.Predicate;

public class DobermanEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(DobermanEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_RED =
            SynchedEntityData.defineId(DobermanEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_RED =
            SynchedEntityData.defineId(DobermanEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DILUTE =
            SynchedEntityData.defineId(DobermanEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_DILUTE =
            SynchedEntityData.defineId(DobermanEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER || entitytype == EntityType.ZOMBIE_HORSE;
    };

    public DobermanEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        DobermanEntity baby = ModEntityTypes.DOBERMAN.get().create(serverLevel);

        determineBabyVariant(baby, (DobermanEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if (this.isSitting()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.doberman.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.doberman.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.doberman.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.doberman.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.doberman.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller",
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
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
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
                if (this.isRed()) {
                    if (this.isDilute()) {
                        message = Component.literal("This Doberman demonstrates two recessive traits.");
                    } else if (this.carriesDilute()) {
                        message = Component.literal("This Doberman demonstrates the recessive red fur trait and carries the dilution trait.");
                    } else {
                        message = Component.literal("This Doberman demonstrates the recessive red fur trait.");
                    }
                } else if (this.carriesRed()) {
                    if (this.isDilute()) {
                        message = Component.literal("This Doberman demonstrates the recessive dilution trait and carries the red fur trait.");
                    } else if (this.carriesDilute()) {
                        message = Component.literal("This Doberman carries two recessive traits.");
                    } else {
                        message = Component.literal("This Doberman carries the red fur trait.");
                    }
                } else if (this.isDilute()) {
                    message = Component.literal("This Doberman demonstrates the recessive dilution trait.");
                } else if (this.carriesDilute()) {
                    message = Component.literal("This Doberman carries the dilution trait.");
                } else {
                    message = Component.literal("This Doberman doesn't have any recessive traits.");
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
        this.entityData.set(IS_RED, tag.getBoolean("IsRed"));
        this.entityData.set(CARRIES_RED, tag.getBoolean("CarriesRed"));
        this.entityData.set(IS_DILUTE, tag.getBoolean("IsDilute"));
        this.entityData.set(CARRIES_DILUTE, tag.getBoolean("CarriesDilute"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsRed", this.isRed());
        tag.putBoolean("CarriesRed", this.carriesRed());
        tag.putBoolean("IsDilute", this.isDilute());
        tag.putBoolean("CarriesDilute", this.carriesDilute());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_RED, false);
        this.entityData.define(CARRIES_RED, false);
        this.entityData.define(IS_DILUTE, false);
        this.entityData.define(CARRIES_DILUTE, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group,
                                        @Nullable CompoundTag tag) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(16) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 13) {
            setDiluteStatus(carrier == 1, false);
            if (determine < 9) {
                var = 0; // BLACK
                setRedStatus(carrier == 2, false);
            } else {
                var = 1; // RED
                setRedStatus(true, true);
            }
        } else {
            setDiluteStatus(true, true);
            if (determine < 16) {
                var = 2; // BLUE
                setRedStatus(carrier == 1, false);
            } else {
                var = 3; // FAWN
                setRedStatus(true, true);
            }
        }

        // assign chosen variant and finish the method
        DobermanVariant variant = DobermanVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public DobermanVariant getVariant() {
        return DobermanVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(DobermanVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isRed() {
        return this.entityData.get(IS_RED);
    }

    public boolean carriesRed() {
        return this.entityData.get(CARRIES_RED);
    }

    private void setRedStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_RED, carrier);
        this.entityData.set(IS_RED, is);
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

    private void determineBabyVariant(DobermanEntity baby, DobermanEntity otherParent) {
        // determine if baby is red or black based
        if (this.isRed() && otherParent.isRed()) {
            // if both parents are red, baby will be red
            baby.setRedStatus(true, true);
        } else if ((this.isRed() && otherParent.carriesRed()) || (this.carriesRed() && otherParent.isRed())) {
            // if one parent is red and one parent is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be red
            baby.setRedStatus(true, this.random.nextBoolean());
        } else if (this.isRed() || otherParent.isRed()) {
            // if only one parent is red, baby will be a carrier
            baby.setRedStatus(true, false);
        } else if (this.carriesRed() && otherParent.carriesRed()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be red
            int determine = this.random.nextInt(4) + 1;
            baby.setRedStatus(determine > 1, determine == 4);
        } else if (this.carriesRed() || otherParent.carriesRed()) {
            // if only one parent carries red, baby has 50/50 chance to be a carrier
            baby.setRedStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be red
            baby.setRedStatus(false, false);
        }

        // determine if baby is dilute, a carrier, or not
        if (this.isDilute() && otherParent.isDilute()) {
            // if both parents are diluted, baby will be diluted
            baby.setDiluteStatus(true, true);
        } else if ((this.isDilute() && otherParent.carriesDilute()) || (this.carriesDilute() && otherParent.isDilute())) {
            // if one parent is diluted and the other is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be diluted
            baby.setDiluteStatus(true, this.random.nextBoolean());
        } else if (this.isDilute() || otherParent.isDilute()) {
            // if only one parent is diluted, baby will be a carrier
            baby.setDiluteStatus(true, false);
        } else if (this.carriesDilute() && otherParent.carriesDilute()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be diluted
            int determine = this.random.nextInt(4) + 1;
            baby.setDiluteStatus(determine > 1, determine == 4);
        } else if (this.carriesDilute() || otherParent.carriesDilute()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setDiluteStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setDiluteStatus(false, false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isDilute() && baby.isRed()) {
            baby.setVariant(DobermanVariant.FAWN);
        } else if (baby.isDilute()) {
            baby.setVariant(DobermanVariant.BLUE);
        } else if (baby.isRed()) {
            baby.setVariant(DobermanVariant.RED);
        } else {
            baby.setVariant(DobermanVariant.BLACK);
        }
    }
}