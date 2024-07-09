package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.BerneseMountainDogVariant;
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
import net.minecraft.world.entity.monster.Zombie;
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

public class BerneseMountainDogEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BerneseMountainDogEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_TAN =
            SynchedEntityData.defineId(BerneseMountainDogEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.GOAT || entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_HORSE;
    };

    public BerneseMountainDogEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 22.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.ARMOR, 0.0f)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f).build();
    }

    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Zombie.class, false, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        BerneseMountainDogEntity baby = ModEntityTypes.BERNESE_MOUNTAIN_DOG.get().create(serverLevel);

        determineBabyVariant(baby, (BerneseMountainDogEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.bernese_mountain_dog.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.bernese_mountain_dog.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.bernese_mountain_dog.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.bernese_mountain_dog.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.bernese_mountain_dog.idle", Animation.LoopType.LOOP));
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
                if (this.getVariant() == BerneseMountainDogVariant.TAN) {
                    message = Component.literal("This Bernese Mountain Dog demonstrates a recessive trait.");
                } else if (this.getCarrier()) {
                    message = Component.literal("This Bernese Mountain Dog carries a recessive trait.");
                } else {
                    message = Component.literal("This Bernese Mountain Dog doesn't have any recessive traits.");
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
        this.entityData.set(CARRIES_TAN, tag.getBoolean("CarrierStatus"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("CarrierStatus", this.getCarrier());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(CARRIES_TAN, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(26.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(22.0);
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
        int determine = r.nextInt(5) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine == 5) {
            var = 1; // TAN
        } else {
            var = 0; // RUST
        }

        setCarrier(var == 1 || carrier == 4); // if dog is tan or rolled to be a carrier, boolean is true

        // assign chosen variant and finish the method
        BerneseMountainDogVariant variant = BerneseMountainDogVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public BerneseMountainDogVariant getVariant() {
        return BerneseMountainDogVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BerneseMountainDogVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean getCarrier() {
        return this.entityData.get(CARRIES_TAN);
    }

    private void setCarrier(boolean carrierStatus) {
        this.entityData.set(CARRIES_TAN, carrierStatus);
    }

    private void determineBabyVariant(BerneseMountainDogEntity baby, BerneseMountainDogEntity otherParent) {
        if (this.getVariant() == BerneseMountainDogVariant.TAN && otherParent.getVariant() == BerneseMountainDogVariant.TAN) {
            // if both parents are tan, baby will be marked as a carrier and have the Tan variant
            baby.setCarrier(true);
            baby.setVariant(BerneseMountainDogVariant.TAN);
        } else if ((this.getVariant() == BerneseMountainDogVariant.TAN && otherParent.getCarrier()) ||
                (this.getCarrier() && otherParent.getVariant() == BerneseMountainDogVariant.TAN)) {
            // if one parent is tan and the other a carrier, the baby will be marked as a carrier and have
            // a 50/50% chance of being tan or rust
            baby.setCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(BerneseMountainDogVariant.TAN);
            } else {
                baby.setVariant(BerneseMountainDogVariant.RUST);
            }
        } else if ((this.getVariant() == BerneseMountainDogVariant.TAN && !otherParent.getCarrier()) ||
                (!this.getCarrier() && otherParent.getVariant() == BerneseMountainDogVariant.TAN)) {
            // if one parent is tan and the other is not a carrier, the baby will be marked as a carrier and
            // appear rust
            baby.setCarrier(true);
            baby.setVariant(BerneseMountainDogVariant.RUST);
        } else if (this.getCarrier() && otherParent.getCarrier()) {
            // if both parents are a carrier, baby has 25% chance not to be a carrier, 50% to be a carrier, and
            // 25% to be tan. If baby is not tan, it will be rust.
            Random r = new Random();
            int determine = r.nextInt(4) + 1;
            if (determine == 1) {
                baby.setCarrier(false);
                baby.setVariant(BerneseMountainDogVariant.RUST);
            } else if (determine < 4) {
                baby.setCarrier(true);
                baby.setVariant(BerneseMountainDogVariant.RUST);
            } else {
                baby.setCarrier(true);
                baby.setVariant(BerneseMountainDogVariant.TAN);
            }
        } else if (this.getCarrier() || otherParent.getCarrier()) {
            // if one parent is a carrier, baby has a 50% chance to be a carrier. Baby's visible variant will
            // be based Rust
            baby.setVariant(BerneseMountainDogVariant.RUST);
            baby.setCarrier(this.random.nextBoolean());
        } else {
            // if neither parent is a carrier, baby won't be a carrier and will be rust
            baby.setVariant(BerneseMountainDogVariant.RUST);
            baby.setCarrier(false);
        }
    }
}