package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CKCharlesSpanielVariant;
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

public class CKCharlesSpanielEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(CKCharlesSpanielEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_POINTS =
            SynchedEntityData.defineId(CKCharlesSpanielEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_POINTS =
            SynchedEntityData.defineId(CKCharlesSpanielEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_WHITE =
            SynchedEntityData.defineId(CKCharlesSpanielEntity.class, EntityDataSerializers.BOOLEAN);


    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.CHICKEN;
    };

    public CKCharlesSpanielEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        CKCharlesSpanielEntity baby = ModEntityTypes.CK_CHARLES_SPANIEL.get().create(serverLevel);

        determineBabyVariant(baby, (CKCharlesSpanielEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.ck_charles_spaniel.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.ck_charles_spaniel.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.ck_charles_spaniel.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.ck_charles_spaniel.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.ck_charles_spaniel.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller",
                0, this::predicate));
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

        Item itemForTaming = ModItems.CHICKEN_TREAT.get();

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
                if (this.hasPoints()) {
                    if (this.hasWhite()) {
                        message = Component.literal("This Cavalier King Charles Spaniel demonstrates a recessive trait and white markings.");
                    } else {
                        message = Component.literal("This Cavalier King Charles Spaniel demonstrates a recessive trait.");
                    }
                } else if (this.isCarrier()) {
                    if (this.hasWhite()) {
                        message = Component.literal("This Cavalier King Charles Spaniel carries a recessive trait and demonstrates white markings.");
                    } else {
                        message = Component.literal("This Cavalier King Charles Spaniel carries a recessive trait.");
                    }
                } else {
                    if (this.hasWhite()) {
                        message = Component.literal("This Cavalier King Charles Spaniel demonstrates white markings.");
                    } else {
                        message = Component.literal("This Cavalier King Charles Spaniel doesn't have any recessive traits.");
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
        this.entityData.set(HAS_POINTS, tag.getBoolean("HasPoints"));
        this.entityData.set(CARRIES_POINTS, tag.getBoolean("CarriesPoints"));
        this.entityData.set(HAS_WHITE, tag.getBoolean("HasWhite"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("HasPoints", this.hasPoints());
        tag.putBoolean("CarriesPoints", this.isCarrier());
        tag.putBoolean("HasWhite", this.hasWhite());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(HAS_POINTS, false);
        this.entityData.define(CARRIES_POINTS, false);
        this.entityData.define(HAS_WHITE, true);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
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
        int solid = r.nextInt(4) + 1;
        int determine = r.nextInt(5) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (solid < 4) {
            setWhite(true);
            if (determine < 5) {
                var = 0; // BLENHEIM
                setPointsStatus(carrier == 4, false);
            } else {
                var = 1; // TRICOLOR
                setPointsStatus(true, true);
            }
        } else {
            setWhite(false);
            if (determine < 4) {
                var = 2; // RUBY
                setPointsStatus(carrier == 4, false);
            } else {
                var = 3; // BLACK_TAN
                setPointsStatus(true, true);
            }
        }

        // assign chosen variant and finish the method
        CKCharlesSpanielVariant variant = CKCharlesSpanielVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public CKCharlesSpanielVariant getVariant() {
        return CKCharlesSpanielVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(CKCharlesSpanielVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean hasPoints() {
        return this.entityData.get(HAS_POINTS);
    }

    public boolean isCarrier() {
        return this.entityData.get(CARRIES_POINTS);
    }

    private void setPointsStatus(boolean carrier, boolean has) {
        this.entityData.set(CARRIES_POINTS, carrier);
        this.entityData.set(HAS_POINTS, has);
    }

    public boolean hasWhite() {
        return this.entityData.get(HAS_WHITE);
    }

    private void setWhite(boolean has) {
        this.entityData.set(HAS_WHITE, has);
    }

    private void determineBabyVariant(CKCharlesSpanielEntity baby, CKCharlesSpanielEntity otherParent) {
        // determine baby's point status
        if (this.hasPoints() && otherParent.hasPoints()) {
            // if both parents have points, baby will have points
            baby.setPointsStatus(true, true);
        } else if ((this.hasPoints() && otherParent.isCarrier()) || (this.isCarrier() && otherParent.hasPoints())) {
            // if one parent has points and the other is a carrier, baby has 50% chance to have points and 50%
            // chance to be a carrier
            baby.setPointsStatus(true, this.random.nextBoolean());
        } else if (this.hasPoints() || otherParent.hasPoints()) {
            // if only one parent has points, baby will be a carrier
            baby.setPointsStatus(true, false);
        } else if (this.isCarrier() && otherParent.isCarrier()) {
            // if both parents are carriers, baby has 25% chance to not carry, 50% chance to be a carrier, and
            // 25% chance to have points
            int determine = this.random.nextInt(4) + 1;
            baby.setPointsStatus(determine > 1, determine == 4);
        } else if (this.isCarrier() || otherParent.isCarrier()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setPointsStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setPointsStatus(false, false);
        }

        // determine if baby has white or not
        if (this.hasWhite() && otherParent.hasWhite()) {
            // if both parents have white, baby will have white
            baby.setWhite(true);
        } else if (this.hasWhite() || otherParent.hasWhite()) {
            // if only one parent has white, baby has 50/50 chance to have white
            baby.setWhite(this.random.nextBoolean());
        } else {
            // if neither parent has white, baby will not have white
            baby.setWhite(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.hasWhite() && baby.hasPoints()) {
            baby.setVariant(CKCharlesSpanielVariant.TRICOLOR);
        } else if (baby.hasPoints()) {
            baby.setVariant(CKCharlesSpanielVariant.BLACK_TAN);
        } else if (baby.hasWhite()) {
            baby.setVariant(CKCharlesSpanielVariant.BLENHEIM);
        } else {
            baby.setVariant(CKCharlesSpanielVariant.RUBY);
        }
    }
}