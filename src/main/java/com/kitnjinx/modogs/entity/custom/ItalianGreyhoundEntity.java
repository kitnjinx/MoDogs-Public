package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.ItalianGreyhoundVariant;
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

public class ItalianGreyhoundEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(ItalianGreyhoundEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_BLUE =
            SynchedEntityData.defineId(ItalianGreyhoundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BLUE =
            SynchedEntityData.defineId(ItalianGreyhoundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_FAWN =
            SynchedEntityData.defineId(ItalianGreyhoundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_FAWN =
            SynchedEntityData.defineId(ItalianGreyhoundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_WHITE =
            SynchedEntityData.defineId(ItalianGreyhoundEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT;
    };

    public ItalianGreyhoundEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        ItalianGreyhoundEntity baby = ModEntityTypes.ITALIAN_GREYHOUND.get().create(serverLevel);

        determineBabyVariant(baby, (ItalianGreyhoundEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState state) {
        if (this.isSitting()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.italian_greyhound.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.italian_greyhound.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.italian_greyhound.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.italian_greyhound.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.italian_greyhound.idle", Animation.LoopType.LOOP));
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

        Item itemForTaming = ModItems.RABBIT_TREAT.get();

        if (item == itemForTaming && !isTame()) {
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
                if (this.isBlue() && this.isFawn()) {
                    if (this.hasWhite()) {
                        message = Component.literal("This Italian Greyhound demonstrates the recessive dilution trait and has white markings. They also have the alleles for fawn fur.");
                    } else {
                        message = Component.literal("This Italian Greyhound demonstrates the recessive dilution trait. They also have the alleles for fawn fur.");
                    }
                } else if (this.isBlue()) {
                    if (this.hasWhite() && this.carriesFawn()) {
                        message = Component.literal("This Italian Greyhound demonstrates the recessive dilution trait and has white markings. They also carry the fawn fur trait.");
                    } else if (this.hasWhite()) {
                        message = Component.literal("This Italian Greyhound demonstrates the recessive dilution trait and has white markings.");
                    } else if (this.carriesFawn()) {
                        message = Component.literal("This Italian Greyhound demonstrates the recessive dilution trait and carries the trait for fawn fur.");
                    } else {
                        message = Component.literal("This Italian Greyhound demonstrates the recessive dilution trait.");
                    }
                } else if (this.isFawn()) {
                    if (this.hasWhite() && this.carriesBlue()) {
                        message = Component.literal("This Italian Greyhound demonstrates the recessive fawn fur trait and has white markings. They also carry the dilution trait.");
                    } else if (this.hasWhite()) {
                        message = Component.literal("This Italian Greyhound demonstrates the recessive fawn fur trait and has white markings.");
                    } else if (this.carriesBlue()) {
                        message = Component.literal("This Italian Greyhound demonstrates the recessive fawn fur trait and carries the dilution trait.");
                    } else {
                        message = Component.literal("This Italian Greyhound demonstrates the recessive fawn fur trait.");
                    }
                } else if (this.carriesBlue() && carriesFawn()) {
                    if (this.hasWhite()) {
                        message = Component.literal("This Italian Greyhound demonstrates white markings and carries two recessive traits.");
                    } else {
                        message = Component.literal("This Italian Greyhound carries two recessive traits.");
                    }
                } else if (this.carriesBlue()) {
                    if (this.hasWhite()) {
                        message = Component.literal("This Italian Greyhound demonstrates white markings and carries the dilution trait.");
                    } else {
                        message = Component.literal("This Italian Greyhound carries the dilution trait.");
                    }
                } else if (this.carriesFawn()) {
                    if (this.hasWhite()) {
                        message = Component.literal("This Italian Greyhound demonstrates white markings and carries the fawn fur trait.");
                    } else {
                        message = Component.literal("This Italian Greyhound carries the fawn fur trait.");
                    }
                } else {
                    if (this.hasWhite()) {
                        message = Component.literal("This Italian Greyhound demonstrates white markings.");
                    } else {
                        message = Component.literal("This Italian Greyhound doesn't have any recessive traits.");
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
        this.entityData.set(IS_BLUE, tag.getBoolean("IsBlue"));
        this.entityData.set(CARRIES_BLUE, tag.getBoolean("CarriesBlue"));
        this.entityData.set(IS_FAWN, tag.getBoolean("IsFawn"));
        this.entityData.set(CARRIES_FAWN, tag.getBoolean("CarriesFawn"));
        this.entityData.set(HAS_WHITE, tag.getBoolean("HasWhite"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsBlue", this.isBlue());
        tag.putBoolean("CarriesBlue", this.carriesBlue());
        tag.putBoolean("IsFawn", this.isFawn());
        tag.putBoolean("CarriesFawn", this.carriesFawn());
        tag.putBoolean("HasWhite", this.hasWhite());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_BLUE, true);
        this.entityData.define(CARRIES_BLUE, true);
        this.entityData.define(IS_FAWN, true);
        this.entityData.define(CARRIES_FAWN, true);
        this.entityData.define(HAS_WHITE, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group,
                                        @Nullable CompoundTag tag) {
        // Variables for determining the variant
        Random r = new Random();
        int white = r.nextInt(3) + 1;
        int determine = r.nextInt(9) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (white < 3) {
            setWhite(false);
            if (determine < 6) {
                var = 0; // BLUE
                setBlueStatus(true, true);
                setFawnStatus(carrier < 7, carrier < 5);
            } else if (determine < 9) {
                var = 1; // FAWN
                setBlueStatus(carrier < 5, false);
                setFawnStatus(true, true);
            } else {
                var = 2; // BLACK
                setBlueStatus(carrier < 5, false);
                setFawnStatus(carrier == 6, false);
            }
        } else {
            this.setWhite(true);
            if (determine < 6) {
                var = 3; // WHITE_BLUE
                setBlueStatus(true, true);
                setFawnStatus(carrier < 7, carrier < 5);
            } else if (determine < 9) {
                var = 4; // WHITE_FAWN
                setBlueStatus(carrier < 5, false);
                setFawnStatus(true, true);
            } else {
                var = 5; // WHITE_BLACK
                setBlueStatus(carrier < 5, false);
                setFawnStatus(carrier == 6, false);
            }
        }

        // assign chosen variant and finish the method
        ItalianGreyhoundVariant variant = ItalianGreyhoundVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public ItalianGreyhoundVariant getVariant() {
        return ItalianGreyhoundVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(ItalianGreyhoundVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isBlue() {
        return this.entityData.get(IS_BLUE);
    }

    public boolean carriesBlue() {
        return this.entityData.get(CARRIES_BLUE);
    }

    private void setBlueStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_BLUE, carrier);
        this.entityData.set(IS_BLUE, is);
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

    private void setWhite(boolean has) {
        this.entityData.set(HAS_WHITE, has);
    }

    private void determineBabyVariant(ItalianGreyhoundEntity baby, ItalianGreyhoundEntity otherParent) {
        // determine if baby is blue or not
        if (this.isBlue() && otherParent.isBlue()) {
            // if both parents are blue, baby will be blue
            baby.setBlueStatus(true, true);
        } else if ((this.isBlue() && otherParent.carriesBlue()) || (this.carriesBlue() && otherParent.isBlue())) {
            // if one parent is blue and the other carries blue, baby has 50% chance to be blue and 50% chance
            // to be a carrier
            baby.setBlueStatus(true, this.random.nextBoolean());
        } else if (this.isBlue() || otherParent.isBlue()) {
            // if only one parent is blue, baby will be a carrier
            baby.setBlueStatus(true, false);
        } else if (this.carriesBlue() && otherParent.carriesBlue()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be blue
            int determine = this.random.nextInt(4) + 1;
            baby.setBlueStatus(determine > 1, determine == 4);
        } else if (this.carriesBlue() || otherParent.carriesBlue()) {
            // if only one parent carries blue, baby will have 50/50 chance to carry blue
            baby.setBlueStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent has blue, baby will not have blue
            baby.setBlueStatus(false, false);
        }

        // determine if baby is fawn or black-based
        if (this.isFawn() && otherParent.isFawn()) {
            // if both parents are fawn, baby will be fawn
            baby.setFawnStatus(true, true);
        } else if ((this.isFawn() && otherParent.carriesFawn()) || (this.carriesFawn() && otherParent.isFawn())) {
            // if one parent is fawn and the other parent is a carrier, baby has 50% chance to be a carrier and
            // 50% chance to be fawn
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
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setFawnStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby is not a carrier
            baby.setFawnStatus(false, false);
        }

        // determine if baby has white markings or not
        if (this.hasWhite() && otherParent.hasWhite()) {
            // if both parents have white, baby will have white
            baby.setWhite(true);
        } else if (this.hasWhite() || otherParent.hasWhite()) {
            // if only one parent has white, baby will have 50/50 chance to have white
            baby.setWhite(this.random.nextBoolean());
        } else {
            // if neither parent has white, baby will not have white
            baby.setWhite(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.hasWhite() && baby.isBlue()) {
            baby.setVariant(ItalianGreyhoundVariant.WHITE_BLUE);
        } else if (baby.hasWhite() && baby.isFawn()) {
            baby.setVariant(ItalianGreyhoundVariant.WHITE_FAWN);
        } else if (baby.hasWhite()) {
            baby.setVariant(ItalianGreyhoundVariant.WHITE_BLACK);
        } else if (baby.isBlue()) {
            baby.setVariant(ItalianGreyhoundVariant.BLUE);
        } else if (baby.isFawn()) {
            baby.setVariant(ItalianGreyhoundVariant.FAWN);
        } else {
            baby.setVariant(ItalianGreyhoundVariant.BLACK);
        }
    }
}