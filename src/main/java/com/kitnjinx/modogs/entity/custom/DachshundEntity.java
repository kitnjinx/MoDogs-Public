package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.DachshundVariant;
import com.kitnjinx.modogs.item.ModItems;
import net.minecraft.nbt.CompoundTag;
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
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.Random;
import java.util.function.Predicate;

public class DachshundEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(DachshundEntity.class, EntityDataSerializers.INT);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT;
    };

    public DachshundEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 18.0f)
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
        DachshundEntity baby = ModEntityTypes.DACHSHUND.get().create(serverLevel);

        determineBabyVariant(baby, (DachshundEntity) otherParent);

        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);
        
        return baby;
    }

    private <E extends DachshundEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dachshund.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dachshund.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dachshund.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dachshund.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dachshund.idle"));
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
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

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(18.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(2D);
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
        int cream = r.nextInt(3) + 1;
        int var;

        if (cream == 3) {
            if (determine < 6) {
                var = 3;
            } else if (determine < 9) {
                var = 4;
            } else {
                var = 5;
            }
        } else {
            if (determine < 6) {
                var = 0;
            } else if (determine < 9) {
                var = 1;
            } else {
                var = 2;
            }
        }

        // assign chosen variant and finish the method
        DachshundVariant variant = DachshundVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public DachshundVariant getVariant() {
        return DachshundVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(DachshundVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    private void determineBabyVariant(DachshundEntity baby, DachshundEntity otherParent) {
        boolean parentACream; // true if this entity is a _CREAM variant
        boolean parentBCream; // true if this entity is a _CREAM variant
        boolean colorCheck; // true if special coat color possible

        // checks parentA
        if (this.getVariant() == DachshundVariant.byId(0) || this.getVariant() == DachshundVariant.byId(1) || this.getVariant() == DachshundVariant.byId(2)) {
            parentACream = false;
        } else {
            parentACream = true;
        }

        // checks parentB
        if (otherParent.getVariant() == DachshundVariant.byId(0) || otherParent.getVariant() == DachshundVariant.byId(1) || otherParent.getVariant() == DachshundVariant.byId(2)) {
            parentBCream = false;
        } else {
            parentBCream = true;
        }

        // checks that one parent is a cream and one parent isn't; marks colorCheck true if true
        if ((parentACream && !parentBCream) || (!parentACream && parentBCream)) {
            colorCheck = true;
        } else {
            colorCheck = false;
        }

        // if potential special coat is possible, this runs
        if (colorCheck) {
            DachshundVariant parAVar = this.getVariant(); // gets this entity's exact variant
            int variantA = parAVar.getId(); // gets variant ID of parent A
            DachshundVariant parBVar = otherParent.getVariant(); // gets entity's exact variant
            int variantB = parBVar.getId(); // gets variant ID of parent B
            int variantDistance = Math.abs(variantA - variantB); // gets absolute value of the difference between the two variant ids
            DachshundVariant babyVariant; // creates holder for the baby's variant

            // ensures that the variants are more than three apart (IE: parent variants aren't X_TAN and X_CREAM
            if (variantDistance != 3) {
                // changes variantA to either the CREAM or TAN version of parentA ID number
                if (variantA < 3) {
                    variantA = variantA + 3;
                } else {
                    variantA = variantA - 3;
                }

                // changes variantB to either the CREAM or TAN version of parentB ID number
                if (variantB < 3) {
                    variantB = variantB + 3;
                } else {
                    variantB = variantB - 3;
                }

                // determines outcome of special coat check
                Random r = new Random();
                int determine = r.nextInt(10) + 1;

                // assigns baby to either a special variant or one of its parents' variants
                if (determine == 10) {
                    babyVariant = DachshundVariant.byId(variantA);
                    baby.setVariant(babyVariant);
                } else if (determine == 9) {
                    babyVariant = DachshundVariant.byId(variantB);
                    baby.setVariant(babyVariant);
                } else if (determine > 4) {
                    baby.setVariant(this.getVariant());
                } else {
                    baby.setVariant(otherParent.getVariant());
                }
            } else {
                // assigns baby to one of its parents' variants
                if (this.random.nextBoolean()) {
                    baby.setVariant(this.getVariant());
                } else {
                    baby.setVariant( otherParent.getVariant());
                }
            }
        } else {
            // assigns baby to one of its parents' variants
            if (this.random.nextBoolean()) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant( otherParent.getVariant());
            }
        }
    }
}
