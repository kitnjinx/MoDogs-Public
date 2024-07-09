package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.BulldogVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.pattern_variation.TwoWhiteVariant;
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
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;
import java.util.function.Predicate;

public class BulldogEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BulldogEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WHITE_VARIANT =
            SynchedEntityData.defineId(BulldogEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_FAWN =
            SynchedEntityData.defineId(BulldogEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_FAWN =
            SynchedEntityData.defineId(BulldogEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_WHITE =
            SynchedEntityData.defineId(BulldogEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PURE_WHITE =
            SynchedEntityData.defineId(BulldogEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COW || entitytype == EntityType.MOOSHROOM;
    };

    public BulldogEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        BulldogEntity baby = ModEntityTypes.BULLDOG.get().create(serverLevel);

        determineBabyVariant(baby, (BulldogEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.bulldog.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.bulldog.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.bulldog.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.bulldog.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.bulldog.idle", Animation.LoopType.LOOP));
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

    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.75F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.25F;
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
                if (this.isPureWhite()) {
                    if (this.isFawn()) {
                        message = Component.literal("This Bulldog demonstrates a fully white coat. They also have the alleles for fawn fur.");
                    } else if (this.carriesFawn()) {
                        message = Component.literal("This Bulldog demonstrates a fully white coat. They also carry the fawn fur trait.");
                    } else {
                        message = Component.literal("This Bulldog demonstrates a fully white coat.");
                    }
                } else if (this.hasWhite()) {
                    if (this.isFawn()) {
                        message = Component.literal("This Bulldog demonstrates the recessive fawn fur trait, as well as high amounts of white.");
                    } else if (this.carriesFawn()) {
                        message = Component.literal("This Bulldog demonstrates high amounts of white markings, and carries the fawn fur trait.");
                    } else {
                        message = Component.literal("This Bulldog demonstrates high amounts of white markings.");
                    }
                } else if (this.isFawn()) {
                    message = Component.literal("This Bulldog demonstrates the recessive fawn fur trait.");
                } else if (this.carriesFawn()) {
                    message = Component.literal("This Bulldog carries the fawn fur trait.");
                } else {
                    message = Component.literal("This Bulldog doesn't have any recessive traits.");
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
        this.entityData.set(WHITE_VARIANT, tag.getInt("WhiteVariant"));
        this.entityData.set(IS_FAWN, tag.getBoolean("IsFawn"));
        this.entityData.set(CARRIES_FAWN, tag.getBoolean("CarriesFawn"));
        this.entityData.set(HAS_WHITE, tag.getBoolean("HasWhite"));
        this.entityData.set(PURE_WHITE, tag.getBoolean("PureWhite"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("WhiteVariant", this.getWhiteVariant());
        tag.putBoolean("IsFawn", this.isFawn());
        tag.putBoolean("CarriesFawn", this.carriesFawn());
        tag.putBoolean("HasWhite", this.hasWhite());
        tag.putBoolean("PureWhite", this.isPureWhite());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(WHITE_VARIANT, 0);
        this.entityData.define(IS_FAWN, false);
        this.entityData.define(CARRIES_FAWN, false);
        this.entityData.define(HAS_WHITE, true);
        this.entityData.define(PURE_WHITE, false);
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
        int white = r.nextInt(10) + 1;
        int determine = r.nextInt(5) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine != 5) {
            var = 0; // RED
            setFawnStatus(carrier == 4, false);
        } else {
            var = 1; // FAWN
            setFawnStatus(true, true);
        }

        setWhiteStatus(white < 10, white < 4);

        // assign chosen variant and finish the method
        setVariant(BulldogVariant.byId(var));
        setWhitePattern(Util.getRandom(TwoWhiteVariant.values(), this.random));
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public BulldogVariant getVariant() {
        return BulldogVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BulldogVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public TwoWhiteVariant getWhitePattern() {
        return TwoWhiteVariant.byId(this.getWhiteVariant() & 255);
    }

    private int getWhiteVariant() {
        return this.entityData.get(WHITE_VARIANT);
    }

    private void setWhitePattern(TwoWhiteVariant variant) {
        this.entityData.set(WHITE_VARIANT, variant.getId() & 255);
    }

    public boolean isFawn() {
        return this.entityData.get(IS_FAWN);
    }

    public boolean carriesFawn() {
        return this.entityData.get(CARRIES_FAWN);
    }

    private void setFawnStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_FAWN, carrier);
        this.entityData.set(IS_FAWN, is);
    }

    public boolean hasWhite() {
        return this.entityData.get(HAS_WHITE);
    }

    public boolean isPureWhite() {
        return this.entityData.get(PURE_WHITE);
    }

    private void setWhiteStatus(boolean has, boolean pure) {
        this.entityData.set(HAS_WHITE, has);
        this.entityData.set(PURE_WHITE, pure);
    }

    private void determineBabyVariant(BulldogEntity baby, BulldogEntity otherParent) {
        // determine if baby is red or fawn
        if (this.isFawn() && otherParent.isFawn()) {
            // if both parents are fawn, baby will be fawn
            baby.setFawnStatus(true, true);
        } else if ((this.isFawn() && otherParent.carriesFawn()) || (this.carriesFawn() && otherParent.isFawn())) {
            // if one parent is fawn and the other is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be fawn
            baby.setFawnStatus(true, this.random.nextBoolean());
        } else if (this.isFawn() || otherParent.isFawn()) {
            // if only one parent is fawn, baby will be a carrier
            baby.setFawnStatus(true, false);
        } else if (this.carriesFawn() && otherParent.carriesFawn()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be fawn
            int determine = this.random.nextInt(4) + 1;
            baby.setFawnStatus(determine > 1, determine == 4);
        } else if (this.carriesFawn() || otherParent.carriesFawn()) {
            // if only one parent carries fawn, baby has 50/50 chance to be a carrier
            baby.setFawnStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not carry
            baby.setFawnStatus(false, false);
        }

        // determine baby's white status
        if (this.isPureWhite() && otherParent.isPureWhite()) {
            // if both parents are white, baby will be white
            baby.setWhiteStatus(true, true);
        } else if ((this.isPureWhite() && otherParent.hasWhite()) ||
                (this.hasWhite() && otherParent.isPureWhite())) {
            // if one parent is white and the other has white, baby has 50% chance to have white and 50%
            // chance to be pure white
            baby.setWhiteStatus(true, this.random.nextBoolean());
        } else if (this.isPureWhite() || otherParent.isPureWhite()) {
            // if only one parent is pure white, baby will have white
            baby.setWhiteStatus(true, false);
        } else if (this.hasWhite() && otherParent.hasWhite()) {
            // if both parents have white, baby has 25% chance not to have white, 50% chance to have white,
            // and 25% to be pure white
            int determine = this.random.nextInt(4) + 1;
            baby.setWhiteStatus(determine > 1, determine == 4);
        } else if (this.hasWhite() || otherParent.hasWhite()) {
            // if only one parent has white, baby has 50/50 chance to have white
            baby.setWhiteStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent has white, baby will not have white
            baby.setWhiteStatus(false, false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isFawn()) {
            baby.setVariant(BulldogVariant.FAWN);
        } else {
            baby.setVariant(BulldogVariant.RED);
        }

        // determine baby's white based on parents (loosely)
        if (!this.isPureWhite() && !otherParent.isPureWhite() && !baby.isPureWhite() &&
                this.hasWhite() == otherParent.hasWhite() && this.hasWhite() == baby.hasWhite() &&
                this.getWhitePattern() == otherParent.getWhitePattern()) {
            baby.setWhitePattern(this.getWhitePattern());
        } else {
            baby.setWhitePattern(Util.getRandom(TwoWhiteVariant.values(), this.random));
        }
    }
}