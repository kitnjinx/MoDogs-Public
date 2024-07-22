package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.WhippetVariant;
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

public class WhippetEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(WhippetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WHITE_VARIANT =
            SynchedEntityData.defineId(WhippetEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_RED =
            SynchedEntityData.defineId(WhippetEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_RED =
            SynchedEntityData.defineId(WhippetEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BLUE =
            SynchedEntityData.defineId(WhippetEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_BLUE =
            SynchedEntityData.defineId(WhippetEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_WHITE =
            SynchedEntityData.defineId(WhippetEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PURE_WHITE =
            SynchedEntityData.defineId(WhippetEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT;
    };

    public WhippetEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        WhippetEntity baby = ModEntityTypes.WHIPPET.get().create(serverLevel);

        determineBabyVariant(baby, (WhippetEntity) otherParent);

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
        RawAnimation sitting = RawAnimation.begin().thenLoop("animation.whippet.sitting");
        RawAnimation angryWalk = RawAnimation.begin().thenLoop("animation.whippet.angrywalk");
        RawAnimation walk = RawAnimation.begin().thenLoop("animation.whippet.walk");
        RawAnimation angryIdle = RawAnimation.begin().thenLoop("animation.whippet.angryidle");
        RawAnimation idle = RawAnimation.begin().thenLoop("animation.whippet.idle");

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
                if (this.isPureWhite()) {
                    if (this.isRed() && this.isBlue()) {
                        message = Component.literal("This Whippet demonstrates a fully white coat. They also have the alleles for diluted red fur.");
                    } else if (this.isRed() && this.isBlueCarrier()) {
                        message = Component.literal("This Whippet demonstrates a fully white coat. They also have the alleles for red fur and carry the dilution trait.");
                    } else if (this.isRed()) {
                        message = Component.literal("This Whippet demonstrates a fully white coat. They also have the alleles for red fur.");
                    } else if (this.isBlue() && this.isRedCarrier()) {
                        message = Component.literal("This Whippet demonstrates a fully white coat. They also have the alleles for diluted color, and carry the red fur trait.");
                    } else if (this.isBlue()) {
                        message = Component.literal("This Whippet demonstrates a fully white coat. They also have the alleles for diluted color.");
                    } else if (this.isBlueCarrier() && this.isRedCarrier()) {
                        message = Component.literal("This Whippet demonstrates a fully white coat. They also carry both the red fur and dilution traits.");
                    } else if (this.isBlueCarrier()) {
                        // pure white and blue carrier
                        message = Component.literal("This Whippet demonstrates a fully white coat. They also carry the dilution trait.");
                    } else if (this.isRedCarrier()) {
                        message = Component.literal("This Whippet demonstrates a fully white coat. They also carry the red fur trait.");
                    } else {
                        message = Component.literal("This Whippet demonstrates a fully white coat.");
                    }
                } else if (this.isBlue()) {
                    if (this.hasWhite() && this.isRed()) {
                        message = Component.literal("This Whippet demonstrates the recessive dilution trait. They also have white markings and the alleles for red fur.");
                    } else if (this.hasWhite() && this.isRedCarrier()) {
                        message = Component.literal("This Whippet demonstrates the recessive dilution trait. They also have white markings and carry the red fur trait.");
                    } else if (this.hasWhite()) {
                        message = Component.literal("This Whippet demonstrates the recessive dilution trait. They also have white markings.");
                    } else if (this.isRed()) {
                        message = Component.literal("This Whippet demonstrates the recessive dilution trait. They also have the alleles for red fur.");
                    } else if (this.isRedCarrier()) {
                        message = Component.literal("This Whippet demonstrates the recessive dilution trait. They also carry the red fur trait.");
                    } else {
                        message = Component.literal("This Whippet demonstrates the recessive dilution trait.");
                    }
                } else if (this.isRed()) {
                    if (this.hasWhite() && this.isBlueCarrier()) {
                        message = Component.literal("This Whippet demonstrates the recessive red fur trait. They also have white markings and carry the dilution trait.");
                    } else if (this.hasWhite()) {
                        message = Component.literal("This Whippet demonstrates the recessive red fur trait. They also have white markings.");
                    } else if (this.isBlueCarrier()) {
                        message = Component.literal("This Whippet demonstrates the recessive red fur trait. They also carry the dilution trait.");
                    } else {
                        message = Component.literal("This Whippet demonstrates the recessive red fur trait.");
                    }
                } else {
                    if (this.hasWhite()) {
                        if (this.isRedCarrier() && this.isBlueCarrier()) {
                            message = Component.literal("This Whippet has white markings and carries two recessive traits.");
                        } else if (this.isRedCarrier()) {
                            message = Component.literal("This Whippet has white markings and carries the recessive red fur trait.");
                        } else if (this.isBlueCarrier()) {
                            message = Component.literal("This Whippet has white markings and carries the recessive dilution trait.");
                        } else {
                            message = Component.literal("This Whippet has white markings.");
                        }
                    } else {
                        if (this.isRedCarrier() && this.isBlueCarrier()) {
                            message = Component.literal("This Whippet carries two recessive traits.");
                        } else if (this.isRedCarrier()) {
                            message = Component.literal("This Whippet carries the recessive red fur trait.");
                        } else if (this.isBlueCarrier()) {
                            message = Component.literal("This Whippet carries the recessive dilution trait.");
                        } else {
                            message = Component.literal("This Whippet doesn't have any recessive traits.");
                        }
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
        this.entityData.set(WHITE_VARIANT, tag.getInt("WhiteVariant"));
        this.entityData.set(CARRIES_RED, tag.getBoolean("RedCarrier"));
        this.entityData.set(IS_RED, tag.getBoolean("IsRed"));
        this.entityData.set(CARRIES_BLUE, tag.getBoolean("BlueCarrier"));
        this.entityData.set(IS_BLUE, tag.getBoolean("IsBlue"));
        this.entityData.set(HAS_WHITE, tag.getBoolean("HasWhite"));
        this.entityData.set(PURE_WHITE, tag.getBoolean("PureWhite"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("WhiteVariant", this.getWhiteValue());
        tag.putBoolean("RedCarrier", this.isRedCarrier());
        tag.putBoolean("IsRed", this.isRed());
        tag.putBoolean("BlueCarrier", this.isBlueCarrier());
        tag.putBoolean("IsBlue", this.isBlue());
        tag.putBoolean("HasWhite", this.hasWhite());
        tag.putBoolean("PureWhite", this.isPureWhite());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(WHITE_VARIANT, 0);
        builder.define(CARRIES_RED, true);
        builder.define(IS_RED, true);
        builder.define(CARRIES_BLUE, false);
        builder.define(IS_BLUE, false);
        builder.define(HAS_WHITE, false);
        builder.define(PURE_WHITE, false);
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
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
        getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4D);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4f);
    }

    /* VARIANTS */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(9) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 6) {
            var = 0; // RED
            setRedStatus(true, true);
            setBlueStatus(carrier == 1, false);
        } else if (determine < 9) {
            var = 1; // BLUE
            spawnBlueWhippet(carrier);
        } else {
            var = 2; // BLACK
            setRedStatus(carrier == 1, false);
            setBlueStatus(carrier == 2, false);
        }

        // determine white status
        boolean has = r.nextBoolean();
        setWhiteStatus(has, has && r.nextInt(9) + 1 == 9);

        // assign chosen variant and finish the method
        setVariant(WhippetVariant.byId(var));
        setWhiteVariant(Util.getRandom(ThreeWhiteVariant.values(), this.random));
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    private void spawnBlueWhippet(int carrier) {
        Random r = new Random();
        setBlueStatus(true, true);
        if (r.nextInt(8) + 1 < 6) {
            setRedStatus(carrier == 1, false);
        } else {
            setRedStatus(true, true);
        }
    }

    public WhippetVariant getVariant() {
        return WhippetVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(WhippetVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public ThreeWhiteVariant getWhiteVariant() {
        return ThreeWhiteVariant.byId(this.getWhiteValue() & 255);
    }

    private int getWhiteValue() {
        return this.entityData.get(WHITE_VARIANT);
    }

    private void setWhiteVariant(ThreeWhiteVariant variant) {
        this.entityData.set(WHITE_VARIANT, variant.getId() & 255);
    }

    public boolean isRedCarrier() {
        return this.entityData.get(CARRIES_RED);
    }

    public boolean isRed() {
        return this.entityData.get(IS_RED);
    }

    private void setRedStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_RED, carrier);
        this.entityData.set(IS_RED, is);
    }

    public boolean isBlueCarrier() {
        return this.entityData.get(CARRIES_BLUE);
    }

    public boolean isBlue() {
        return this.entityData.get(IS_BLUE);
    }

    private void setBlueStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_BLUE, carrier);
        this.entityData.set(IS_BLUE, is);
    }

    public boolean hasWhite() {
        return this.entityData.get(HAS_WHITE);
    }
    
    public boolean isPureWhite() {
        return this.entityData.get(PURE_WHITE);
    }

    private void setWhiteStatus(boolean has, boolean is) {
        this.entityData.set(HAS_WHITE, has);
        this.entityData.set(PURE_WHITE, is);
    }

    private void determineBabyVariant(WhippetEntity baby, WhippetEntity otherParent) {
        // determine if baby is black or red
        if (this.isRed() && otherParent.isRed()) {
            // if both parents are red, baby will be red
            baby.setRedStatus(true, true);
        } else if ((this.isRed() && otherParent.isRedCarrier()) || (this.isRedCarrier() && otherParent.isRed())) {
            // if one parent is red and the other is a carrier, baby has 50% chance to be red and 50% chance to
            // be a carrier
            baby.setRedStatus(true, this.random.nextBoolean());
        } else if (this.isRed() || otherParent.isRed()) {
            // if one parent is red and the other is not a carrier, baby will be a red carrier
            baby.setRedStatus(true, false);
        } else if (this.isRedCarrier() && otherParent.isRedCarrier()) {
            // if both parents are red carriers, baby has 25% chance to not be a carrier, 50% chance to be a
            // carrier, and 25% chance to be red
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setRedStatus(false, false);
            } else {
                baby.setRedStatus(true, determine == 4);
            }
        } else if (this.isRedCarrier() || otherParent.isRedCarrier()) {
            // if only one parent is a red carrier, baby has 50/50 chance to be a carrier
            baby.setRedStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a red carrier, baby will not be a carrier
            baby.setRedStatus(false, false);
        }

        // determine if baby is diluted (blue) or not
        if (this.isBlue() && otherParent.isBlue()) {
            // if both parents are blue, baby will be blue
            baby.setBlueStatus(true, true);
        } else if ((this.isBlue() && otherParent.isBlueCarrier()) ||
                (this.isBlueCarrier() && otherParent.isBlue())) {
            // if one parent is blue and the other is a carrier, baby has 50% chance to be blue and 50% chance to
            // carry blue
            baby.setBlueStatus(true, this.random.nextBoolean());
        } else if (this.isBlue() || otherParent.isBlue()) {
            // if one parent is blue and the other is not a carrier, baby will be a carrier
            baby.setBlueStatus(true, false);
        } else if (this.isBlueCarrier() && otherParent.isBlueCarrier()) {
            // if both parents are carriers, baby has 25% chance to not carry blue, 50% chance to be a carrier,
            // and 25% chance to be blue
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setBlueStatus(false, false);
            } else {
                baby.setBlueStatus(true, determine == 4);
            }
        } else if (this.isBlueCarrier() || otherParent.isBlueCarrier()) {
            // if only one parent is a blue carrier, baby has 50% chance to carry blue
            baby.setBlueStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not carry blue
            baby.setBlueStatus(false, false);
        }

        // determine if baby is white, has white markings, or is solid colored
        if (this.isPureWhite() && otherParent.isPureWhite()) {
            // if both parents are white, baby will be white
            baby.setWhiteStatus(true, true);
        } else if ((this.isPureWhite() && otherParent.hasWhite()) ||
                (this.hasWhite() && otherParent.isPureWhite())) {
            // if one parent is white and one parent has white markings, baby has 50% chance to be white and
            // 50% chance to have white markings
            baby.setWhiteStatus(true, this.random.nextBoolean());
        } else if (this.isPureWhite() || otherParent.isPureWhite()) {
            // if one parent is white and the other has no white markings, baby will have white markings
            baby.setWhiteStatus(true, false);
        } else if (this.hasWhite() && otherParent.hasWhite()) {
            // if both parents have white markings, baby has 25% chance to have no white, 50% chance to have
            // white markings, and 25% chance to be white
            int determine = this.random.nextInt(4) + 1;
            baby.setWhiteStatus(determine > 1, determine == 4);
        } else if (this.hasWhite() || otherParent.hasWhite()) {
            // if only one parent has white markings, baby has 50/50 chance to have white markings
            baby.setWhiteStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent has white markings, baby will not have white markings
            baby.setWhiteStatus(false, false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isBlue()) {
            baby.setVariant(WhippetVariant.BLUE);
        } else if (baby.isRed()) {
            baby.setVariant(WhippetVariant.RED);
        } else {
            baby.setVariant(WhippetVariant.BLACK);
        }

        // determine baby's white pattern
        if ((this.hasWhite() && !this.isPureWhite()) && (otherParent.hasWhite() && !otherParent.isPureWhite())) {
            if (this.getWhiteVariant() == ThreeWhiteVariant.WHITE2 &&
                    otherParent.getWhiteVariant() == ThreeWhiteVariant.WHITE2) {
                int determine = this.random.nextInt(4) + 1;
                if (determine < 3) {
                    baby.setWhiteVariant(ThreeWhiteVariant.WHITE2);
                } else if (determine < 4) {
                    baby.setWhiteVariant(ThreeWhiteVariant.WHITE1);
                } else {
                    baby.setWhiteVariant(ThreeWhiteVariant.WHITE3);
                }
            } else if (this.getWhiteVariant() == otherParent.getWhiteVariant()) {
                baby.setWhiteVariant(this.getWhiteVariant());
            } else if ((this.getWhiteVariant() == ThreeWhiteVariant.WHITE2 ||
                    otherParent.getWhiteVariant() == ThreeWhiteVariant.WHITE2)) {
                if (this.random.nextBoolean()) {
                    baby.setWhiteVariant(this.getWhiteVariant());
                } else {
                    baby.setWhiteVariant(otherParent.getWhiteVariant());
                }
            } else {
                baby.setWhiteVariant(ThreeWhiteVariant.WHITE2);
            }
        }
    }
}