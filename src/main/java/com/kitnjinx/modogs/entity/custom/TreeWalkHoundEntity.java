package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.TreeWalkHoundVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
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
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends TreeWalkHoundEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tree_walk_hound.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tree_walk_hound.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tree_walk_hound.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tree_walk_hound.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tree_walk_hound.idle"));
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
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
                TextComponent message;
                if (this.getVariant() == TreeWalkHoundVariant.TRICOLOR) {
                    message = new TextComponent("This Treeing Walker Coonhound demonstrates a recessive trait.");
                } else if (this.isCarrier()) {
                    message = new TextComponent("This Treeing Walker Coonhound carries a recessive trait.");
                } else {
                    message = new TextComponent("This Treeing Walker Coonhound doesn't have any recessive traits.");
                }

                player.sendMessage(message, player.getUUID());

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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(CARRIES_TRI, true);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(22.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
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
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
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