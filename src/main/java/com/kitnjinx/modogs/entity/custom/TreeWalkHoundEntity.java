package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.TreeWalkHoundVariant;
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
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Random;
import java.util.function.Predicate;

public class TreeWalkHoundEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(TreeWalkHoundEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_TRI =
            SynchedEntityData.defineId(TreeWalkHoundEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT || entitytype == EntityType.FOX;
    };

    public TreeWalkHoundEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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

        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        TreeWalkHoundEntity baby = ModEntityTypes.TREE_WALK_HOUND.get().create(serverLevel);

        determineBabyVariant(baby, (TreeWalkHoundEntity) otherParent);

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
        RawAnimation sitting = RawAnimation.begin().thenLoop("animation.tree_walk_hound.sitting");
        RawAnimation angryWalk = RawAnimation.begin().thenLoop("animation.tree_walk_hound.angrywalk");
        RawAnimation walk = RawAnimation.begin().thenLoop("animation.tree_walk_hound.walk");
        RawAnimation angryIdle = RawAnimation.begin().thenLoop("animation.tree_walk_hound.angryidle");
        RawAnimation idle = RawAnimation.begin().thenLoop("animation.tree_walk_hound.idle");

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
                if (this.getVariant() == TreeWalkHoundVariant.TRICOLOR) {
                    message = Component.literal("This Treeing Walker Coonhound demonstrates a recessive trait.");
                } else if (this.isCarrier()) {
                    message = Component.literal("This Treeing Walker Coonhound carries a recessive trait.");
                } else {
                    message = Component.literal("This Treeing Walker Coonhound doesn't have any recessive traits.");
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
        this.entityData.set(CARRIES_TRI, tag.getBoolean("IsCarrier"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsCarrier", this.isCarrier());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(CARRIES_TRI, true);
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
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4f);
    }

    /* VARIANTS */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(6) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine != 6) {
            var = 0; // TRICOLOR
            setCarrier(true);
        } else {
            var = 1; // BLACK
            setCarrier(r.nextBoolean());
        }

        // assign chosen variant and finish the method
        TreeWalkHoundVariant variant = TreeWalkHoundVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    public TreeWalkHoundVariant getVariant() {
        return TreeWalkHoundVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(TreeWalkHoundVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isCarrier() {
        return this.entityData.get(CARRIES_TRI);
    }

    private void setCarrier(boolean status) {
        this.entityData.set(CARRIES_TRI, status);
    }

    private void determineBabyVariant(TreeWalkHoundEntity baby, TreeWalkHoundEntity otherParent) {
        if (this.getVariant() == TreeWalkHoundVariant.TRICOLOR && otherParent.getVariant() == TreeWalkHoundVariant.TRICOLOR) {
            // if both parents are tricolor, baby will be tricolor
            baby.setVariant(TreeWalkHoundVariant.TRICOLOR);
            baby.setCarrier(true);
        } else if ((this.getVariant() == TreeWalkHoundVariant.TRICOLOR && otherParent.isCarrier()) ||
                (this.isCarrier() && otherParent.getVariant() == TreeWalkHoundVariant.TRICOLOR)) {
            // if one parent is tri and the other is a carrier, baby has 50% chance to be tricolor and 50%
            // chance to be a carrier
            baby.setCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(TreeWalkHoundVariant.TRICOLOR);
            } else {
                baby.setVariant(TreeWalkHoundVariant.BLACK);
            }
        } else if (this.getVariant() == TreeWalkHoundVariant.TRICOLOR || otherParent.getVariant() == TreeWalkHoundVariant.TRICOLOR) {
            // if only one parent is tricolor, baby will be a carrier
            baby.setVariant(TreeWalkHoundVariant.BLACK);
            baby.setCarrier(true);
        } else if (this.isCarrier() && otherParent.isCarrier()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be tricolor
            int determine = this.random.nextInt(4) + 1;
            baby.setCarrier(determine > 1);
            if (determine < 4) {
                baby.setVariant(TreeWalkHoundVariant.BLACK);
            } else {
                baby.setVariant(TreeWalkHoundVariant.TRICOLOR);
            }
        } else if (this.isCarrier() || otherParent.isCarrier()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setVariant(TreeWalkHoundVariant.BLACK);
            baby.setCarrier(this.random.nextBoolean());
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setVariant(TreeWalkHoundVariant.BLACK);
            baby.setCarrier(false);
        }
    }
}