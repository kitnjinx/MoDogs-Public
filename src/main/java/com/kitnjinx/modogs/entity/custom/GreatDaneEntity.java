package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.GreatDaneVariant;
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

import java.awt.*;
import java.util.Random;
import java.util.function.Predicate;

public class GreatDaneEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(GreatDaneEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_FAWN =
            SynchedEntityData.defineId(GreatDaneEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BLUE =
            SynchedEntityData.defineId(GreatDaneEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_BLUE =
            SynchedEntityData.defineId(GreatDaneEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.PIG || entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER;
    };

    public GreatDaneEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 22.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
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
        GreatDaneEntity baby = ModEntityTypes.GREAT_DANE.get().create(serverLevel);

        determineBabyVariant(baby, (GreatDaneEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.great_dane.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.great_dane.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.great_dane.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.great_dane.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.great_dane.idle", Animation.LoopType.LOOP));
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

        Item itemForTaming = ModItems.BACON_TREAT.get();

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
                if (this.getVariant() == GreatDaneVariant.FAWN) {
                    if (this.isBlue()) {
                        message = Component.literal("This Great Dane demonstrates a recessive fur color and has the alleles for the recessive dilution trait.");
                    } else if (this.isBlueCarrier()) {
                        message = Component.literal("This Great Dane demonstrates a recessive fur color and carries the recessive dilution trait.");
                    } else {
                        message = Component.literal("This Great Dane demonstrates a recessive fur color.");
                    }
                } else if (this.isBlue()) {
                    if (this.isFawnCarrier()) {
                        message = Component.literal("This Great Dane demonstrates the recessive dilution trait, and also carries recessive fawn fur.");
                    } else {
                        message = Component.literal("This Great Dane demonstrates the recessive dilution trait.");
                    }
                } else {
                    if (this.isFawnCarrier() && this.isBlueCarrier()) {
                        message = Component.literal("This Great Dane carries two recessive traits.");
                    } else if (this.isFawnCarrier()) {
                        message = Component.literal("This Great Dane carries the recessive fawn fur trait.");
                    } else if (this.isBlueCarrier()) {
                        message = Component.literal("This Great Dane carries the recessive dilution trait.");
                    } else {
                        message = Component.literal("This Great Dane doesn't have any recessive traits.");
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
        this.entityData.set(CARRIES_FAWN, tag.getBoolean("FawnCarrier"));
        this.entityData.set(CARRIES_BLUE, tag.getBoolean("BlueCarrier"));
        this.entityData.set(IS_BLUE, tag.getBoolean("IsBlue"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("FawnCarrier", this.isFawnCarrier());
        tag.putBoolean("BlueCarrier", this.isBlueCarrier());
        tag.putBoolean("IsBlue", this.isBlue());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(CARRIES_FAWN, true);
        this.entityData.define(CARRIES_BLUE, false);
        this.entityData.define(IS_BLUE, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(28.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(22.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
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
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 6) {
            var = 0; // FAWN
            setFawnCarrier(true);
            setBlueStatus(carrier == 1, false);
        } else if (determine < 9) {
            var = 1; // BLACK
            setFawnCarrier(carrier == 1);
            setBlueStatus(carrier == 2, false);
        } else {
            var = 2; // BLUE
            setFawnCarrier(carrier < 5);
            setBlueStatus(true, true);
        }

        // assign chosen variant and finish the method
        GreatDaneVariant variant = GreatDaneVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public GreatDaneVariant getVariant() {
        return GreatDaneVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(GreatDaneVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isFawnCarrier() {
        return this.entityData.get(CARRIES_FAWN);
    }

    private void setFawnCarrier(boolean status) {
        this.entityData.set(CARRIES_FAWN, status);
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

    private void determineBabyVariant(GreatDaneEntity baby, GreatDaneEntity otherParent) {
        // determine if baby is fawn or black
        boolean isFawn;
        if (this.getVariant() == GreatDaneVariant.FAWN && otherParent.getVariant() == GreatDaneVariant.FAWN) {
            // if both parents are fawn, baby will be fawn
            isFawn = true;
            baby.setFawnCarrier(true);
        } else if ((this.getVariant() == GreatDaneVariant.FAWN && otherParent.isFawnCarrier()) ||
                (this.isFawnCarrier() && otherParent.getVariant() == GreatDaneVariant.FAWN)) {
            // if one parent is fawn and the other is a carrier, baby has 50% chance to be fawn and 50% chance
            // to be a carrier
            isFawn = this.random.nextBoolean();
            baby.setFawnCarrier(true);
        } else if (this.getVariant() == GreatDaneVariant.FAWN || otherParent.getVariant() == GreatDaneVariant.FAWN) {
            // if only one parent is fawn and the other is not a carrier, baby will be a carrier
            isFawn = false;
            baby.setFawnCarrier(true);
        } else if (this.isFawnCarrier() && otherParent.isFawnCarrier()) {
            // if both parents are carriers, baby has 25% chance not to carry fawn, 50% chance to carry fawn, and
            // 25% chance to be fawn
            int determine = this.random.nextInt(4) + 1;
            isFawn = determine == 4;
            baby.setFawnCarrier(determine != 1);
        } else if (this.isFawnCarrier() || otherParent.isFawnCarrier()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a fawn carrier
            isFawn = false;
            baby.setFawnCarrier(this.random.nextBoolean());
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            isFawn = false;
            baby.setFawnCarrier(false);
        }

        // determine if baby's blue status (blue, carrier, neither)
        if (this.isBlue() && otherParent.isBlue()) {
            // if both parents are blue, baby will be blue
            baby.setBlueStatus(true, true);
        } else if ((this.isBlue() && otherParent.isBlueCarrier()) || (this.isBlueCarrier() && otherParent.isBlue())) {
            // if one parent is blue and the other is a carrier, baby has 50% chance to be blue and 50% chance to
            // be a carrier
            baby.setBlueStatus(true, this.random.nextBoolean());
        } else if (this.isBlue() || otherParent.isBlue()) {
            // if only one parent is blue and the other is not a carrier, baby will be a carrier
            baby.setBlueStatus(true, false);
        } else if (this.isBlueCarrier() && otherParent.isBlueCarrier()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be blue
            int determine = this.random.nextInt(4) + 1;
            baby.setBlueStatus(determine != 1, determine == 4);
        } else if (this.isBlueCarrier() || otherParent.isBlueCarrier()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setBlueStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent carries blue, baby will not carry blue
            baby.setBlueStatus(false, false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (isFawn) {
            baby.setVariant(GreatDaneVariant.FAWN);
        } else if (baby.isBlue()) {
            baby.setVariant(GreatDaneVariant.BLUE);
        } else {
            baby.setVariant(GreatDaneVariant.BLACK);
        }
    }
}