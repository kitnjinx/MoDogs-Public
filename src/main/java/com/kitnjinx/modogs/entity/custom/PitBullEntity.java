package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.PitBullVariant;
import com.kitnjinx.modogs.entity.variant.pattern_variation.ThreeWhiteVariant;
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

public class PitBullEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(PitBullEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WHITE_PATTERN =
            SynchedEntityData.defineId(PitBullEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_BROWN =
            SynchedEntityData.defineId(PitBullEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_BROWN =
            SynchedEntityData.defineId(PitBullEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BLUE =
            SynchedEntityData.defineId(PitBullEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_BLUE =
            SynchedEntityData.defineId(PitBullEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_WHITE =
            SynchedEntityData.defineId(PitBullEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COW || entitytype == EntityType.MOOSHROOM;
    };

    public PitBullEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        PitBullEntity baby = ModEntityTypes.PIT_BULL.get().create(serverLevel);

        determineBabyVariant(baby, (PitBullEntity) otherParent);

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
        RawAnimation sitting = RawAnimation.begin().thenLoop("animation.pit_bull.sitting");
        RawAnimation angryWalk = RawAnimation.begin().thenLoop("animation.pit_bull.angrywalk");
        RawAnimation walk = RawAnimation.begin().thenLoop("animation.pit_bull.walk");
        RawAnimation angryIdle = RawAnimation.begin().thenLoop("animation.pit_bull.angryidle");
        RawAnimation idle = RawAnimation.begin().thenLoop("animation.pit_bull.idle");

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

        Item itemForTaming = ModItems.BEEF_TREAT.get();

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
                if (this.getVariant() == PitBullVariant.WHITE) {
                    if (this.isBrown() && this.isBlue()) {
                        message = Component.literal("This Pit Bull demonstrates a fully white coat. They also have the alleles for two recessive traits.");
                    } else if (this.isBrown() && this.isBlueCarrier()) {
                        message = Component.literal("This Pit Bull demonstrates a fully white coat. They also have the alleles for brown fur and carry the dilution trait.");
                    } else if (this.isBrown()) {
                        message = Component.literal("This Pit Bull demonstrates a fully white coat. They also have the alleles for brown fur.");
                    } else if (this.isBrownCarrier() && this.isBlue()) {
                        message = Component.literal("This Pit Bull demonstrates a fully white coat. They also have the alleles for dilution, and carry the brown fur trait.");
                    } else if (this.isBrownCarrier() && this.isBlueCarrier()) {
                        message = Component.literal("This Pit Bull demonstrates a fully white coat. They also carry the dilution and brown fur traits.");
                    } else if (this.isBlue()) {
                        message = Component.literal("This Pit Bull demonstrates a fully white coat. They also have the alleles for diluted fur.");
                    } else if (this.isBrownCarrier()) {
                        message = Component.literal("This Pit Bull demonstrates a fully white coat. They also carry the brown fur trait.");
                    } else if (this.isBlueCarrier()) {
                        message = Component.literal("This Pit Bull demonstrates a fully white coat. They also carry the dilution trait.");
                    } else {
                        message = Component.literal("This Pit Bull demonstrates a fully white coat.");
                    }
                } else if (this.hasWhite()) {
                    if (this.isBrown() && this.isBlue()) {
                        message = Component.literal("This Pit Bull demonstrates a recessive fur color and white markings. They also have the alleles for diluted fur.");
                    } else if (this.isBrown() && this.isBlueCarrier()) {
                        message = Component.literal("This Pit Bull demonstrates a recessive fur color and white markings. They also carry the dilution trait.");
                    } else if (this.isBrown()) {
                        message = Component.literal("This Pit Bull demonstrates a recessive fur color and white markings.");
                    } else if (this.isBrownCarrier() && this.isBlue()) {
                        message = Component.literal("This Pit Bull demonstrates the recessive dilution trait and white markings. They also carry the brown fur trait/");
                    } else if (this.isBrownCarrier() && this.isBlueCarrier()) {
                        message = Component.literal("This Pit Bull demonstrates white markings, and carries two recessive traits.");
                    } else if (this.isBlue()) {
                        message = Component.literal("This Pit Bull demonstrates the recessive dilution trait and white markings.");
                    } else if (this.isBrownCarrier()) {
                        message = Component.literal("This Pit Bull demonstrates white markings, and carries the brown fur trait.");
                    } else if (this.isBlueCarrier()) {
                        message = Component.literal("This Pit Bull demonstrates white markings, and carries the dilution trait.");
                    } else {
                        message = Component.literal("This Pit Bull demonstrates white markings.");
                    }
                } else {
                    if (this.isBrown() && this.isBlue()) {
                        message = Component.literal("This Pit Bull demonstrates a recessive fur color, and has the alleles for the dilution trait.");
                    } else if (this.isBrown() && this.isBlueCarrier()) {
                        message = Component.literal("This Pit Bull demonstrates a recessive fur color, and carries the dilution trait.");
                    } else if (this.isBrown()) {
                        message = Component.literal("This Pit Bull demonstrates a recessive fur color.");
                    } else if (this.isBrownCarrier() && this.isBlue()) {
                        message = Component.literal("This Pit Bull demonstrates the recessive dilution trait, and carries the brown fur trait.");
                    } else if (this.isBrownCarrier() && this.isBlueCarrier()) {
                        message = Component.literal("This Pit Bull carries two recessive traits.");
                    } else if (this.isBlue()) {
                        message = Component.literal("This Pit Bull demonstrates the recessive dilution trait.");
                    } else if (this.isBrownCarrier()) {
                        message = Component.literal("This Pit Bull carries the brown fur trait.");
                    } else if (this.isBlueCarrier()) {
                        message = Component.literal("This Pit Bull carries the dilution trait.");
                    } else {
                        message = Component.literal("This Pit Bull doesn't have any recessive traits.");
                    }
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
        this.entityData.set(WHITE_PATTERN, tag.getInt("WhitePattern"));
        this.entityData.set(CARRIES_BROWN, tag.getBoolean("BrownCarrier"));
        this.entityData.set(IS_BROWN, tag.getBoolean("IsBrown"));
        this.entityData.set(CARRIES_BLUE, tag.getBoolean("BlueCarrier"));
        this.entityData.set(IS_BLUE, tag.getBoolean("IsBlue"));
        this.entityData.set(HAS_WHITE, tag.getBoolean("HasWhite"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("WhitePattern", this.getWhitePattern());
        tag.putBoolean("BrownCarrier", this.isBrownCarrier());
        tag.putBoolean("IsBrown", this.isBrown());
        tag.putBoolean("BlueCarrier", this.isBlueCarrier());
        tag.putBoolean("IsBlue", this.isBlue());
        tag.putBoolean("HasWhite", this.hasWhite());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(WHITE_PATTERN, 0);
        builder.define(CARRIES_BROWN, true);
        builder.define(IS_BROWN, true);
        builder.define(CARRIES_BLUE, false);
        builder.define(IS_BLUE, false);
        builder.define(HAS_WHITE, false);
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
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
    }

    /* VARIANTS */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group) {
        // Variables for determining the variant
        Random r = new Random();
        int white = r.nextInt(3) + 1;
        int determine = r.nextInt(12) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 7) {
            var = 0; // BROWN
            setBrownStatus(true, true);
            setBlueStatus(carrier == 1, false);
        } else if (determine < 10) {
            var = 1; // BLACK
            setBrownStatus(carrier == 1, false);
            setBlueStatus(carrier == 2, false);
        } else if (determine < 12) {
            var = 2; // BLUE
            setBrownStatus(carrier < 7, false);
            setBlueStatus(true, true);
        } else {
            var = 3; // WHITE
            int blue = r.nextInt(10) + 1;
            if (blue == 10) {
                setBrownStatus(carrier < 7, false);
                setBlueStatus(true, true);
            } else {
                setBrownStatus(carrier < 7, carrier < 4);
                setBlueStatus(carrier == 1, false);
            }
        }

        setWhite(var == 3 || white < 3);

        // assign chosen variant and finish the method
        PitBullVariant variant = PitBullVariant.byId(var);
        setVariant(variant);
        setWhitePattern(Util.getRandom(ThreeWhiteVariant.values(), this.random));
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    public PitBullVariant getVariant() {
        return PitBullVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(PitBullVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public ThreeWhiteVariant getWhiteVariant() {
        return ThreeWhiteVariant.byId(this.getWhitePattern() & 255);
    }

    private int getWhitePattern() {
        return this.entityData.get(WHITE_PATTERN);
    }

    private void setWhitePattern(ThreeWhiteVariant variant) {
        this.entityData.set(WHITE_PATTERN, variant.getId() & 255);
    }

    public boolean isBrown() {
        return this.entityData.get(IS_BROWN);
    }

    public boolean isBrownCarrier() {
        return this.entityData.get(CARRIES_BROWN);
    }

    private void setBrownStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_BROWN, carrier);
        this.entityData.set(IS_BROWN, is);
    }

    public boolean isBlue() {
        return this.entityData.get(IS_BLUE);
    }

    public boolean isBlueCarrier() {
        return this.entityData.get(CARRIES_BLUE);
    }

    private void setBlueStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_BLUE, carrier);
        this.entityData.set(IS_BLUE, is);
    }

    public boolean hasWhite() {
        return this.entityData.get(HAS_WHITE);
    }

    private void setWhite(boolean has) {
        this.entityData.set(HAS_WHITE, has);
    }

    private void determineBabyVariant(PitBullEntity baby, PitBullEntity otherParent) {
        // determine if baby is black or brown
        if (this.isBrown() && otherParent.isBrown()) {
            // if both parents are brown, baby will be brown
            baby.setBrownStatus(true, true);
        } else if ((this.isBrown() && otherParent.isBrownCarrier()) ||
                (this.isBrownCarrier() && otherParent.isBrown())) {
            // if one parent is brown and the other is a carrier, baby has 50% chance to be brown and
            // 50% chance to be a carrier
            baby.setBrownStatus(true, this.random.nextBoolean());
        } else if (this.isBrown() || otherParent.isBrown()) {
            // if one parent is brown and the other is not a carrier, baby will be a carrier
            baby.setBrownStatus(true, false);
        } else if (this.isBrownCarrier() && otherParent.isBrownCarrier()) {
            // if both parents are carriers, baby has 25% chance to not carry, 50% chance to be a carrier,
            // and 25% chance to be brown
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setBrownStatus(false, false);
            } else {
                baby.setBrownStatus(true, determine == 4);
            }
        } else if (this.isBrownCarrier() || otherParent.isBrownCarrier()) {
            // if only one parent is a carrier, baby will have 50/50 chance to be a carrier
            baby.setBrownStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a brown carrier, baby will not be a carrier
            baby.setBrownStatus(false, false);
        }

        // determine if baby is blue or not
        if (this.isBlue() && otherParent.isBlue()) {
            // if both parents are blue, baby will be blue
            baby.setBlueStatus(true, true);
        } else if ((this.isBlue() && otherParent.isBlueCarrier()) ||
                (this.isBlueCarrier() && otherParent.isBlue())) {
            // if one parent is blue and the other is a carrier, baby has 50% chance to be blue and
            // 50% chance to be a carrier
            baby.setBlueStatus(true, this.random.nextBoolean());
        } else if (this.isBlue() || otherParent.isBlue()) {
            // if one parent is blue and the other is not a carrier, baby will be a carrier
            baby.setBlueStatus(true, false);
        } else if (this.isBlueCarrier() && otherParent.isBlueCarrier()) {
            // if both parents are carriers, baby has 25% chance to not carry, 50% chance to be a carrier,
            // and 25% chance to be blue
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setBlueStatus(false, false);
            } else {
                baby.setBlueStatus(true, determine == 4);
            }
        } else if (this.isBlueCarrier() || otherParent.isBlueCarrier()) {
            // if only one parent is a carrier, baby will have 50/50 chance to be a carrier
            baby.setBlueStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a blue carrier, baby will not be a carrier
            baby.setBlueStatus(false, false);
        }

        // determine if baby is white, has white markings, or no white
        boolean pureWhite;
        if (this.getVariant() == PitBullVariant.WHITE && otherParent.getVariant() == PitBullVariant.WHITE) {
            // if both parents are white, baby will also be white
            pureWhite = true;
            baby.setWhite(true);
        } else if ((this.getVariant() == PitBullVariant.WHITE && otherParent.hasWhite()) ||
                (this.hasWhite() && otherParent.getVariant() == PitBullVariant.WHITE)) {
            // if one parent is white and the other has markings, baby has 50% chance to be white and 50% chance
            // to have markings
            pureWhite = this.random.nextBoolean();
            baby.setWhite(true);
        } else if (this.getVariant() == PitBullVariant.WHITE || otherParent.getVariant() == PitBullVariant.WHITE) {
            // if one parent is white and the other has no markings, baby will have white markings
            pureWhite = false;
            baby.setWhite(true);
        } else if (this.hasWhite() && otherParent.hasWhite()) {
            // if both parents have white markings, baby has 25% chance to have no white, 50% chance to have
            // white markings, and 25% chance to be white
            int determine = this.random.nextInt(4) + 1;
            pureWhite = determine == 4;
            baby.setWhite(determine > 1);
        } else if (this.hasWhite() || otherParent.hasWhite()) {
            // if only one parent has white markings, baby has 50/50 chance to have white markings
            pureWhite = false;
            baby.setWhite(this.random.nextBoolean());
        } else {
            // if neither parent has white markings, baby will not have markings
            pureWhite = false;
            baby.setWhite(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (pureWhite) {
            baby.setVariant(PitBullVariant.WHITE);
        } else if (baby.isBrown()) {
            baby.setVariant(PitBullVariant.BROWN);
        } else if (baby.isBlue()) {
            baby.setVariant(PitBullVariant.BLUE);
        } else {
            baby.setVariant(PitBullVariant.BLACK);
        }

        setWhitePattern(Util.getRandom(ThreeWhiteVariant.values(), this.random));
    }
}