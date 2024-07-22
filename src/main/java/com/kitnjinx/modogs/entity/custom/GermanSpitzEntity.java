package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.GermanSpitzVariant;
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
import net.minecraft.world.entity.monster.Monster;
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

public class GermanSpitzEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(GermanSpitzEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_BROWN =
            SynchedEntityData.defineId(GermanSpitzEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BROWN =
            SynchedEntityData.defineId(GermanSpitzEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_RED =
            SynchedEntityData.defineId(GermanSpitzEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_RED =
            SynchedEntityData.defineId(GermanSpitzEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_WHITE =
            SynchedEntityData.defineId(GermanSpitzEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER;
    };

    public GermanSpitzEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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

        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Monster.class, true, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        GermanSpitzEntity baby = ModEntityTypes.GERMAN_SPITZ.get().create(serverLevel);

        determineBabyVariant(baby, (GermanSpitzEntity) otherParent);

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
        RawAnimation sitting = RawAnimation.begin().thenLoop("animation.german_spitz.sitting");
        RawAnimation angryWalk = RawAnimation.begin().thenLoop("animation.german_spitz.angrywalk");
        RawAnimation walk = RawAnimation.begin().thenLoop("animation.german_spitz.walk");
        RawAnimation angryIdle = RawAnimation.begin().thenLoop("animation.german_spitz.angryidle");
        RawAnimation idle = RawAnimation.begin().thenLoop("animation.german_spitz.idle");

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

        Item itemForTaming = ModItems.BACON_TREAT.get();
        Item itemForTaming2 = ModItems.SALMON_TREAT.get();

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
                Component message = determineGeneTesterMessage();
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
        this.entityData.set(IS_BROWN, tag.getBoolean("IsBrown"));
        this.entityData.set(CARRIES_BROWN, tag.getBoolean("CarriesBrown"));
        this.entityData.set(IS_RED, tag.getBoolean("IsRed"));
        this.entityData.set(CARRIES_RED, tag.getBoolean("CarriesRed"));
        this.entityData.set(CARRIES_WHITE, tag.getBoolean("CarriesWhite"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsBrown", this.isBrown());
        tag.putBoolean("CarriesBrown", this.carriesBrown());
        tag.putBoolean("IsRed", this.isRed());
        tag.putBoolean("CarriesRed", this.carriesRed());
        tag.putBoolean("CarriesWhite", this.carriesWhite());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(IS_BROWN, false);
        builder.define(CARRIES_BROWN, false);
        builder.define(IS_RED, true);
        builder.define(CARRIES_RED, true);
        builder.define(CARRIES_WHITE, true);
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
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5D);
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
            var = 0; // WHITE
            setCarriesWhite(true);
            if (r.nextInt(4) + 1 == 4) {
                setRedStatus(carrier == 1, false);
                setBrownStatus(carrier > 4, carrier > 6);
            } else {
                setRedStatus(true, true);
                setBrownStatus(carrier < 5, carrier < 3);
            }
        } else if (determine < 10) {
            var = 1; // RED
            setRedStatus(true, true);
            setBrownStatus(carrier < 5, carrier < 3);
            setCarriesWhite(carrier == 8);
        } else {
            setCarriesWhite(carrier == 1);
            setRedStatus(carrier == 2, false);
            if (r.nextBoolean()) {
                var = 2; // BLACK
                setBrownStatus(false, false);
            } else {
                var = 3; // BROWN
                setBrownStatus(true, true);
            }
        }

        // assign chosen variant and finish the method
        GermanSpitzVariant variant = GermanSpitzVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    public GermanSpitzVariant getVariant() {
        return GermanSpitzVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(GermanSpitzVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isBrown() {
        return this.entityData.get(IS_BROWN);
    }

    public boolean carriesBrown() {
        return this.entityData.get(CARRIES_BROWN);
    }

    private void setBrownStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_BROWN, carrier);
        this.entityData.set(IS_BROWN, is);
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

    public boolean carriesWhite() {
        return this.entityData.get(CARRIES_WHITE);
    }

    private void setCarriesWhite(boolean carrier) {
        this.entityData.set(CARRIES_WHITE, carrier);
    }

    private void determineBabyVariant(GermanSpitzEntity baby, GermanSpitzEntity otherParent) {
        // determine if baby is brown or black
        if (this.isBrown() && otherParent.isBrown()) {
            // if both parents are brown, baby will be brown
            baby.setBrownStatus(true, true);
        } else if ((this.isBrown() && otherParent.carriesBrown()) || (this.carriesBrown() && otherParent.isBrown())) {
            // if one parent is brown and the other is a carrier, baby has 50% chance to be a carrier and
            // 50% chance to be brown
            baby.setBrownStatus(true, this.random.nextBoolean());
        } else if (this.isBrown() || otherParent.isBrown()) {
            // if only one parent is brown, baby will be a carrier
            baby.setBrownStatus(true, false);
        } else if (this.carriesBrown() && otherParent.carriesBrown()) {
            // if both parents carry brown, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be brown
            int determine = this.random.nextInt(4) + 1;
            baby.setBrownStatus(determine > 1, determine == 4);
        } else if (this.carriesBrown() || otherParent.carriesBrown()) {
            // if only one parent carries brown, baby has 50/50 chance to be a carrier
            baby.setBrownStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby won't be a carrier
            baby.setBrownStatus(false, false);
        }

        // determine if baby is red or black/brown
        if (this.isRed() && otherParent.isRed()) {
            // if both parents are red, baby will be red
            baby.setRedStatus(true, true);
        } else if ((this.isRed() && otherParent.carriesRed()) || (this.carriesRed() && otherParent.isRed())) {
            // if one parent is red and the other is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be red
            baby.setRedStatus(true, this.random.nextBoolean());
        } else if (this.isRed() || otherParent.isRed()) {
            // if only one parent is red, baby will be a carrier
            baby.setRedStatus(true, false);
        } else if (this.carriesRed() && otherParent.carriesRed()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier,
            // and 25% chance to be red
            int determine = this.random.nextInt(4) + 1;
            baby.setRedStatus(determine > 1, determine == 4);
        } else if (this.carriesRed() || otherParent.carriesRed()) {
            // if only one parent carries red, baby has 50/50 chance to be a carrier
            baby.setRedStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setRedStatus(false, false);
        }

        // determine if baby is white or shows the base color
        boolean isWhite;
        if (this.getVariant() == GermanSpitzVariant.WHITE && otherParent.getVariant() == GermanSpitzVariant.WHITE) {
            // if both parents are white, baby will be white
            isWhite = true;
            baby.setCarriesWhite(true);
        } else if ((this.getVariant() == GermanSpitzVariant.WHITE && otherParent.carriesWhite()) ||
                (this.carriesWhite() && otherParent.getVariant() == GermanSpitzVariant.WHITE)) {
            // if one parent is white and the other is a carrier, baby has 50% chance to be a carrier and
            // 50% chance to be white
            isWhite = this.random.nextBoolean();
            baby.setCarriesWhite(true);
        } else if (this.getVariant() == GermanSpitzVariant.WHITE || otherParent.getVariant() == GermanSpitzVariant.WHITE) {
            // if only one parent is white, baby will be a carrier
            baby.setCarriesWhite(true);
            isWhite = false;
        } else if (this.carriesWhite() && otherParent.carriesWhite()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be white
            int determine = this.random.nextInt(4) + 1;
            isWhite = determine == 4;
            baby.setCarriesWhite(determine > 1);
        } else if (this.carriesWhite() || otherParent.carriesWhite()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            isWhite = false;
            baby.setCarriesWhite(this.random.nextBoolean());
        } else {
            // if neither parent is a carrier, baby won't be a carrier
            baby.setCarriesWhite(false);
            isWhite = false;
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (isWhite) {
            baby.setVariant(GermanSpitzVariant.WHITE);
        } else if (baby.isRed()) {
            baby.setVariant(GermanSpitzVariant.RED);
        } else if (baby.isBrown()) {
            baby.setVariant(GermanSpitzVariant.BROWN);
        } else {
            baby.setVariant(GermanSpitzVariant.BLACK);
        }
    }

    private Component determineGeneTesterMessage() {
        Component message;
        if (this.getVariant() == GermanSpitzVariant.WHITE && this.isRed()) {
            if (this.isBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive white fur trait. They also have the alleles for red fur and brown fur.");
            } else if (this.carriesBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive white fur trait. They also have the alleles for red fur, and carry the brown fur trait.");
            } else {
                message = Component.literal("This German Spitz demonstrates the recessive white fur trait. They also have the alleles for red fur.");
            }
        } else if (this.getVariant() == GermanSpitzVariant.WHITE && this.carriesRed()) {
            if (this.isBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive white fur trait. They also have the alleles for brown fur and carry the red fur trait.");
            } else if (this.carriesBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive white fur trait. They also carry the red fur and brown fur traits.");
            } else {
                message = Component.literal("This German Spitz demonstrates the recessive white fur trait. They also carry the red fur trait.");
            }
        } else if (this.getVariant() == GermanSpitzVariant.WHITE) {
            if (this.isBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive white fur trait. They also have the alleles for brown fur.");
            } else if (this.carriesBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive white fur trait. They also carry the brown fur trait.");
            } else {
                message = Component.literal("This German Spitz demonstrates the recessive white fur trait.");
            }
        } else if (this.carriesWhite() && this.isRed()) {
            if (this.isBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive red fur trait. They also have the alleles for brown fur and carry the white fur trait.");
            } else if (this.carriesBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive red fur trait. They also carry the brown fur and white fur traits.");
            } else {
                message = Component.literal("This German Spitz demonstrates the recessive red fur trait. They also carry the white fur trait.");
            }
        } else if (this.carriesWhite() && this.carriesRed()) {
            if (this.isBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive brown fur trait. They also carry the red fur and white fur traits.");
            } else if (this.carriesBrown()) {
                message = Component.literal("This German Spitz carries three recessive traits.");
            } else {
                message = Component.literal("This German Spitz carries the red fur and white fur traits.");
            }
        } else if (this.carriesWhite()) {
            if (this.isBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive brown fur trait and carries the white fur trait.");
            } else if (this.carriesBrown()) {
                message = Component.literal("This German Spitz carries the brown fur and white fur traits.");
            } else {
                message = Component.literal("This German Spitz carries the white fur trait.");
            }
        } else if (this.isRed()) {
            if (this.isBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive red fur trait. They also have the alleles for brown fur.");
            } else if (this.carriesBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive red fur trait, and carries the brown fur trait.");
            } else {
                message = Component.literal("This German Spitz demonstrates the recessive red fur trait.");
            }
        } else if (this.carriesRed()) {
            if (this.isBrown()) {
                message = Component.literal("This German Spitz demonstrates the recessive brown fur trait, and carries the red fur trait.");
            } else if (this.carriesBrown()) {
                message = Component.literal("This German Spitz carries the red fur and brown fur traits.");
            } else {
                message = Component.literal("This German Spitz carries the red fur trait.");
            }
        } else if (this.isBrown()) {
            message = Component.literal("This German Spitz demonstrates the recessive brown fur trait.");
        } else if (this.carriesBrown()) {
            message = Component.literal("This German Spitz carries the brown fur trait.");
        } else {
            message = Component.literal("This German Spitz doesn't have any recessive traits.");
        }

        return message;
    }
}