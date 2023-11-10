package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.DalmatianVariant;
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

public class DalmatianEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(DalmatianEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_BROWN =
            SynchedEntityData.defineId(DalmatianEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT || entitytype == EntityType.PIG;
    };

    public DalmatianEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0f)
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
        DalmatianEntity baby = ModEntityTypes.DALMATIAN.get().create(serverLevel);

        determineBabyVariant(baby, (DalmatianEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends DalmatianEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dalmatian.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dalmatian.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dalmatian.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dalmatian.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dalmatian.idle"));
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
        Item itemForTaming2 = ModItems.BACON_TREAT.get();

        if ((item == itemForTaming || item == itemForTaming2) && !isTame()) {
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
                if (this.getVariant() == DalmatianVariant.BROWN) {
                    message = new TextComponent("This Dalmatian demonstrates a recessive trait.");
                } else if (this.getCarrier()) {
                    message = new TextComponent("This Dalmatian carries a recessive trait.");
                } else {
                    message = new TextComponent("This Dalmatian doesn't have any recessive traits.");
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
        this.entityData.set(CARRIES_BROWN, tag.getBoolean("CarrierStatus"));
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
        this.entityData.define(CARRIES_BROWN, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(22.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
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
        int determine = r.nextInt(13) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 5) {
            var = 0; // BLACK1
        } else if (determine < 9) {
            var = 1; // BLACK2
        } else if (determine < 13) {
            var = 2; // BLACK3
        } else {
            var = 3; // BROWN
        }

        setCarrier(var == 3 || carrier == 4); // if dog is brown or rolled to be a carrier, boolean is true

        // assign chosen variant and finish the method
        DalmatianVariant variant = DalmatianVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public DalmatianVariant getVariant() {
        return DalmatianVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(DalmatianVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean getCarrier() {
        return this.entityData.get(CARRIES_BROWN);
    }

    private void setCarrier(boolean carrierStatus) {
        this.entityData.set(CARRIES_BROWN, carrierStatus);
    }

    private void determineBabyVariant(DalmatianEntity baby, DalmatianEntity otherParent) {
        if (this.getVariant() == DalmatianVariant.BROWN && otherParent.getVariant() == DalmatianVariant.BROWN) {
            // if both parents are brown, baby will be marked as a carrier and have the Brown variant
            baby.setCarrier(true);
            baby.setVariant(DalmatianVariant.BROWN);
        } else if (this.getVariant() == DalmatianVariant.BROWN && otherParent.getCarrier()) {
            // if one parent is brown and the other a carrier, the baby will be marked as a carrier and have
            // a 50% chance of being Brown. Otherwise, they'll get their black parent's coat
            baby.setCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(DalmatianVariant.BROWN);
            } else {
                baby.setVariant(otherParent.getVariant());
            }
        } else if (this.getCarrier() && otherParent.getVariant() == DalmatianVariant.BROWN) {
            // if one parent is brown and the other a carrier, the baby will be marked as a carrier and have
            // a 50% chance of being Brown. Otherwise, they'll get their black parent's coat
            baby.setCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(DalmatianVariant.BROWN);
            } else {
                baby.setVariant(this.getVariant());
            }
        } else if (this.getVariant() == DalmatianVariant.BROWN && !otherParent.getCarrier()) {
            // if one parent is brown and the other is not a carrier, the baby will be marked as a carrier and
            // have the black parent's variant
            baby.setCarrier(true);
            baby.setVariant(otherParent.getVariant());
        } else if (!this.getCarrier() && otherParent.getVariant() == DalmatianVariant.BROWN) {
            // if one parent is brown and the other is not a carrier, the baby will be marked as a carrier and
            // have the black parent's variant
            baby.setCarrier(true);
            baby.setVariant(this.getVariant());
        } else if (this.getCarrier() && otherParent.getCarrier()) {
            // if both parents are a carrier, baby has 25% chance not to be a carrier, 50% to be a carrier, and
            // 25% to be brown. If baby is not brown, baby will have a variant based on its parents
            Random r = new Random();
            int determine = r.nextInt(4) + 1;
            if (determine == 1) {
                baby.setCarrier(false);
                setBabyBlack(baby, otherParent);
            } else if (determine < 4) {
                baby.setCarrier(true);
                setBabyBlack(baby, otherParent);
            } else {
                baby.setCarrier(true);
                baby.setVariant(DalmatianVariant.BROWN);
            }
        } else if (this.getCarrier() || otherParent.getCarrier()) {
            // if one parent is a carrier, baby has a 50% chance to be a carrier. Baby's visible variant will
            // be based on its parents
            setBabyBlack(baby, otherParent);
            baby.setCarrier(this.random.nextBoolean());
        } else {
            // if neither parent is a carrier, baby won't be a carrier and will have a variant based on its parents
            baby.setCarrier(false);
            setBabyBlack(baby, otherParent);
        }
    }

    private void setBabyBlack(DalmatianEntity baby, DalmatianEntity otherParent) {
        if (this.getVariant() != otherParent.getVariant()) {
            // if parents are 2 different black patterns, baby can have any black pattern
            int determine = this.random.nextInt(3) + 1;

            if (determine == 1) {
                baby.setVariant(DalmatianVariant.BLACK1);
            } else if (determine == 2) {
                baby.setVariant(DalmatianVariant.BLACK2);
            } else {
                baby.setVariant(DalmatianVariant.BLACK3);
            }
        } else {
            // if parents have same pattern, baby will have their pattern
            baby.setVariant(this.getVariant());
        }
    }
}