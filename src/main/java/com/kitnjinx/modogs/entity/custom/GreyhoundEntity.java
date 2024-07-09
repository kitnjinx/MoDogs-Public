package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.GreyhoundVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
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
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;
import java.util.function.Predicate;

public class GreyhoundEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(GreyhoundEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WHITE_PATTERN =
            SynchedEntityData.defineId(GreyhoundEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_RED =
            SynchedEntityData.defineId(GreyhoundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_RED =
            SynchedEntityData.defineId(GreyhoundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BLUE =
            SynchedEntityData.defineId(GreyhoundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_BLUE =
            SynchedEntityData.defineId(GreyhoundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_WHITE =
            SynchedEntityData.defineId(GreyhoundEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.SHEEP || entitytype == EntityType.RABBIT;
    };

    public GreyhoundEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        GreyhoundEntity baby = ModEntityTypes.GREYHOUND.get().create(serverLevel);

        determineBabyVariant(baby, (GreyhoundEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.greyhound.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.greyhound.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.greyhound.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.greyhound.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.greyhound.idle", Animation.LoopType.LOOP));
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

        Item itemForTaming = ModItems.MUTTON_TREAT.get();
        Item itemForTaming2 = ModItems.RABBIT_TREAT.get();

        if ((item == itemForTaming || item == itemForTaming2) && !isTame()) {
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
                if (this.getVariant() == GreyhoundVariant.WHITE) {
                    if (this.isRed() && this.isBlue()) {
                        message = Component.literal("This Greyhound demonstrates a fully white coat. They also have the alleles for diluted red fur.");
                    } else if (this.isRed() && this.isBlueCarrier()) {
                        message = Component.literal("This Greyhound demonstrates a fully white coat. They also have the alleles for red fur and carry the dilution trait.");
                    } else if (this.isRed()) {
                        message = Component.literal("This Greyhound demonstrates a fully white coat. They also have the alleles for red fur.");
                    } else if (this.isBlue() && this.isRedCarrier()) {
                        message = Component.literal("This Greyhound demonstrates a fully white coat. They also have the alleles for diluted color, and carry the red fur trait.");
                    } else if (this.isBlue()) {
                        message = Component.literal("This Greyhound demonstrates a fully white coat. They also have the alleles for diluted color.");
                    } else if (this.isBlueCarrier() && this.isRedCarrier()) {
                        message = Component.literal("This Greyhound demonstrates a fully white coat. They also carry both the red fur and dilution traits.");
                    } else if (this.isBlueCarrier()) {
                        // pure white and blue carrier
                        message = Component.literal("This Greyhound demonstrates a fully white coat. They also carry the dilution trait.");
                    } else if (this.isRedCarrier()) {
                        message = Component.literal("This Greyhound demonstrates a fully white coat. They also carry the red fur trait.");
                    } else {
                        message = Component.literal("This Greyhound demonstrates a fully white coat.");
                    }
                } else if (this.isBlue()) {
                    if (this.hasWhite() && this.isRed()) {
                        message = Component.literal("This Greyhound demonstrates the recessive dilution trait. They also have white markings and the alleles for red fur.");
                    } else if (this.hasWhite() && this.isRedCarrier()) {
                        message = Component.literal("This Greyhound demonstrates the recessive dilution trait. They also have white markings and carry the red fur trait.");
                    } else if (this.hasWhite()) {
                        message = Component.literal("This Greyhound demonstrates the recessive dilution trait. They also have white markings.");
                    } else if (this.isRed()) {
                        message = Component.literal("This Greyhound demonstrates the recessive dilution trait. They also have the alleles for red fur.");
                    } else if (this.isRedCarrier()) {
                        message = Component.literal("This Greyhound demonstrates the recessive dilution trait. They also carry the red fur trait.");
                    } else {
                        message = Component.literal("This Greyhound demonstrates the recessive dilution trait.");
                    }
                } else if (this.isRed()) {
                    if (this.hasWhite() && this.isBlueCarrier()) {
                        message = Component.literal("This Greyhound demonstrates the recessive red fur trait. They also have white markings and carry the dilution trait.");
                    } else if (this.hasWhite()) {
                        message = Component.literal("This Greyhound demonstrates the recessive red fur trait. They also have white markings.");
                    } else if (this.isBlueCarrier()) {
                        message = Component.literal("This Greyhound demonstrates the recessive red fur trait. They also carry the dilution trait.");
                    } else {
                        message = Component.literal("This Greyhound demonstrates the recessive red fur trait.");
                    }
                } else {
                    if (this.hasWhite()) {
                        if (this.isRedCarrier() && this.isBlueCarrier()) {
                            message = Component.literal("This Greyhound has white markings and carries two recessive traits.");
                        } else if (this.isRedCarrier()) {
                            message = Component.literal("This Greyhound has white markings and carries the recessive red fur trait.");
                        } else if (this.isBlueCarrier()) {
                            message = Component.literal("This Greyhound has white markings and carries the recessive dilution trait.");
                        } else {
                            message = Component.literal("This Greyhound has white markings.");
                        }
                    } else {
                        if (this.isRedCarrier() && this.isBlueCarrier()) {
                            message = Component.literal("This Greyhound carries two recessive traits.");
                        } else if (this.isRedCarrier()) {
                            message = Component.literal("This Greyhound carries the recessive red fur trait.");
                        } else if (this.isBlueCarrier()) {
                            message = Component.literal("This Greyhound carries the recessive dilution trait.");
                        } else {
                            message = Component.literal("This Greyhound doesn't have any recessive traits.");
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
        this.entityData.set(WHITE_PATTERN, tag.getInt("Pattern"));
        this.entityData.set(CARRIES_RED, tag.getBoolean("RedCarrier"));
        this.entityData.set(IS_RED, tag.getBoolean("IsRed"));
        this.entityData.set(CARRIES_BLUE, tag.getBoolean("BlueCarrier"));
        this.entityData.set(IS_BLUE, tag.getBoolean("IsBlue"));
        this.entityData.set(HAS_WHITE, tag.getBoolean("HasWhite"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("Pattern", this.getWhitePattern());
        tag.putBoolean("RedCarrier", this.isRedCarrier());
        tag.putBoolean("IsRed", this.isRed());
        tag.putBoolean("BlueCarrier", this.isBlueCarrier());
        tag.putBoolean("IsBlue", this.isBlue());
        tag.putBoolean("HasWhite", this.hasWhite());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(WHITE_PATTERN, 0);
        this.entityData.define(CARRIES_RED, true);
        this.entityData.define(IS_RED, true);
        this.entityData.define(CARRIES_BLUE, false);
        this.entityData.define(IS_BLUE, false);
        this.entityData.define(HAS_WHITE, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(22.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3.0);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group,
                                        @Nullable CompoundTag tag) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(9) + 1;
        boolean determineWhite = r.nextBoolean();
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine == 9 && r.nextBoolean()) {
            var = 0; // WHITE
            int status = r.nextInt(9) + 1;
            if (status < 6) {
                setRedStatus(carrier == 1, false);
                setBlueStatus(carrier == 2, false);
            } else if (status < 9) {
                setRedStatus(true, true);
                setBlueStatus(carrier == 1, false);
            } else {
                spawnBlueGreyhound(carrier);
            }
        } else if (determine < 6) {
            var = 1; // BLACK
            setRedStatus(carrier == 1, false);
            setBlueStatus(carrier == 2, false);
        } else if (determine < 9) {
            var = 2; // RED
            setRedStatus(true, true);
            setBlueStatus(carrier == 1, false);
        } else {
            var = 3; // BLUE
            spawnBlueGreyhound(carrier);
        }

        setWhite(var == 0 || determineWhite);

        // assign chosen variant and finish the method
        GreyhoundVariant variant = GreyhoundVariant.byId(var);
        setVariant(variant);
        setWhitePattern(Util.getRandom(ThreeWhiteVariant.values(), this.random));
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    private void spawnBlueGreyhound(int carrier) {
        Random r = new Random();
        setBlueStatus(true, true);
        if (r.nextInt(8) + 1 < 6) {
            setRedStatus(carrier == 1, false);
        } else {
            setRedStatus(true, true);
        }
    }

    public GreyhoundVariant getVariant() {
        return GreyhoundVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(GreyhoundVariant variant) {
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

    private void setWhite(boolean has) {
        this.entityData.set(HAS_WHITE, has);
    }

    private void determineBabyVariant(GreyhoundEntity baby, GreyhoundEntity otherParent) {
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
        boolean isWhite;
        if (this.getVariant() == GreyhoundVariant.WHITE && otherParent.getVariant() == GreyhoundVariant.WHITE) {
            // if both parents are white, baby will be white
            isWhite = true;
            baby.setWhite(true);
        } else if ((this.getVariant() == GreyhoundVariant.WHITE && otherParent.hasWhite()) ||
                (this.hasWhite() && otherParent.getVariant() == GreyhoundVariant.WHITE)) {
            // if one parent is white and one parent has white markings, baby has 50% chance to be white and
            // 50% chance to have white markings
            isWhite = this.random.nextBoolean();
            baby.setWhite(true);
        } else if (this.getVariant() == GreyhoundVariant.WHITE || otherParent.getVariant() == GreyhoundVariant.WHITE) {
            // if one parent is white and the other has no white markings, baby will have white markings
            isWhite = false;
            baby.setWhite(true);
        } else if (this.hasWhite() && otherParent.hasWhite()) {
            // if both parents have white markings, baby has 25% chance to have no white, 50% chance to have
            // white markings, and 25% chance to be white
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                isWhite = false;
                baby.setWhite(false);
            } else {
                isWhite = determine == 4;
                baby.setWhite(true);
            }
        } else if (this.hasWhite() || otherParent.hasWhite()) {
            // if only one parent has white markings, baby has 50/50 chance to have white markings
            isWhite = false;
            baby.setWhite(this.random.nextBoolean());
        } else {
            // if neither parent has white markings, baby will not have white markings
            isWhite = false;
            baby.setWhite(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (isWhite) {
            baby.setVariant(GreyhoundVariant.WHITE);
        } else if (baby.isBlue()) {
            baby.setVariant(GreyhoundVariant.BLUE);
        } else if (baby.isRed()) {
            baby.setVariant(GreyhoundVariant.RED);
        } else {
            baby.setVariant(GreyhoundVariant.BLACK);
        }

        // determine baby's white pattern
        baby.setWhitePattern(Util.getRandom(ThreeWhiteVariant.values(), this.random));
    }
}