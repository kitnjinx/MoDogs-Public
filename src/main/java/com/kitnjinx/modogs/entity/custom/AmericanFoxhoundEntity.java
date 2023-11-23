package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.AmericanFoxhoundVariant;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.item.ModItems;
import net.minecraft.Util;
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
import net.minecraft.world.entity.monster.Monster;
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

import java.util.function.Predicate;

public class AmericanFoxhoundEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(AmericanFoxhoundEntity.class, EntityDataSerializers.INT);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.FOX;
    };

    public AmericanFoxhoundEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        AmericanFoxhoundEntity baby = ModEntityTypes.AMERICAN_FOXHOUND.get().create(serverLevel);

        determineBabyVariant(baby, (AmericanFoxhoundEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);
        
        return baby;
    }

    private <E extends AmericanFoxhoundEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.american_foxhound.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.american_foxhound.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.american_foxhound.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.american_foxhound.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.american_foxhound.idle"));
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
                TextComponent message;
                if (this.getVariant() == AmericanFoxhoundVariant.LIGHT) {
                    message = new TextComponent("This American Foxhound has the alleles for light fur.");
                } else if (this.getVariant() == AmericanFoxhoundVariant.MEDIUM) {
                    message = new TextComponent("This American Foxhound has the alleles for medium fur.");
                } else {
                    message = new TextComponent("This American Foxhound has the alleles for dark fur.");
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
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(23.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
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
        AmericanFoxhoundVariant variant = Util.getRandom(AmericanFoxhoundVariant.values(), this.random);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public AmericanFoxhoundVariant getVariant() {
        return AmericanFoxhoundVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(AmericanFoxhoundVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    private void determineBabyVariant(AmericanFoxhoundEntity baby, AmericanFoxhoundEntity otherParent) {
        if (this.getVariant() == AmericanFoxhoundVariant.MEDIUM &&
                otherParent.getVariant() == AmericanFoxhoundVariant.MEDIUM) {
            // if both parents have a medium coat shade, baby has 25% chance to be light, 50% chance to be medium,
            // and 25% chance to be dark
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setVariant(AmericanFoxhoundVariant.LIGHT);
            } else if (determine < 4) {
                baby.setVariant(AmericanFoxhoundVariant.MEDIUM);
            } else {
                baby.setVariant(AmericanFoxhoundVariant.DARK);
            }
        } else if (this.getVariant() == otherParent.getVariant()) {
            // if both parents are light or both parents are dark, baby will match the parents
            baby.setVariant(this.getVariant());
        } else if ((this.getVariant() == AmericanFoxhoundVariant.LIGHT && otherParent.getVariant() == AmericanFoxhoundVariant.DARK) ||
                (this.getVariant() == AmericanFoxhoundVariant.DARK && otherParent.getVariant() == AmericanFoxhoundVariant.LIGHT)) {
            // if one parent is dark and one parent is light, baby will be medium
            baby.setVariant(AmericanFoxhoundVariant.MEDIUM);
        } else {
            // if one parent is medium and one parent is light/dark, baby will have 50/50 chance of matching
            // either parent
            if (this.random.nextBoolean()) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(otherParent.getVariant());
            }
        }
    }
}