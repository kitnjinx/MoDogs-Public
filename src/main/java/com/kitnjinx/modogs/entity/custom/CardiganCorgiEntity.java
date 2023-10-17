package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CardiganCorgiVariant;
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

public class CardiganCorgiEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(CardiganCorgiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BASE_COLOR =
            SynchedEntityData.defineId(CardiganCorgiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_MERLE =
            SynchedEntityData.defineId(CardiganCorgiEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COW || entitytype == EntityType.FOX;
    };

    public CardiganCorgiEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        CardiganCorgiEntity baby = ModEntityTypes.CARDIGAN_CORGI.get().create(serverLevel);

        determineBabyVariant(baby, (CardiganCorgiEntity) otherParent);

        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends CardiganCorgiEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cardigan_corgi.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cardigan_corgi.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cardigan_corgi.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cardigan_corgi.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cardigan_corgi.idle"));
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

        Item itemForTaming = ModItems.BEEF_TREAT.get();

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
                if (this.getVariant() == CardiganCorgiVariant.SABLE) {
                    if (this.isMerle()) {
                        message = new TextComponent("This Cardigan Corgi demonstrates a recessive trait. They also carry the merle trait.");
                    } else {
                        message = new TextComponent("This Cardigan Corgi demonstrates a recessive trait.");
                    }
                } else if (this.getVariant() == CardiganCorgiVariant.RED) {
                    if (this.isMerle()) {
                        message = new TextComponent("This Cardigan Corgi demonstrates an incomplete dominant trait. They also carry the merle trait.");
                    } else {
                        message = new TextComponent("This Cardigan Corgi demonstrates an incomplete dominant trait.");
                    }
                } else if (this.getVariant() == CardiganCorgiVariant.BLACK) {
                    message = new TextComponent("This Cardigan Corgi doesn't have any recessive traits.");
                } else {
                    message = new TextComponent("This Cardigan Corgi doesn't have any recessive traits, but does demonstrate the merle trait.");
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
        this.entityData.set(BASE_COLOR, tag.getInt("BaseColor"));
        this.entityData.set(IS_MERLE, tag.getBoolean("IsMerle"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("BaseColor", this.getBaseColor());
        tag.putBoolean("IsMerle", this.isMerle());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(BASE_COLOR, 0);
        this.entityData.define(IS_MERLE, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(26.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(18.0);
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
        int determine = r.nextInt(16) + 1;
        int var;

        if (determine < 8) {
            var = 0;
            setBaseColor(0);
        } else if (determine < 13) {
            var = 1;
            setBaseColor(1);
        } else if (determine < 16) {
            var = 2;
            setBaseColor(2);
        } else {
            var = 3;
            setBaseColor(1);
        }

        setMerle(var == 3);

        // assign chosen variant and finish the method
        CardiganCorgiVariant variant = CardiganCorgiVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public CardiganCorgiVariant getVariant() {
        return CardiganCorgiVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(CardiganCorgiVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public int getBaseColor() {
        return this.entityData.get(BASE_COLOR);
    }

    private void setBaseColor(int color) {
        this.entityData.set(BASE_COLOR, color);
    }

    public boolean isMerle() {
        return this.entityData.get(IS_MERLE);
    }

    private void setMerle(boolean merle) {
        this.entityData.set(IS_MERLE, merle);
    }

    private void determineBabyVariant(CardiganCorgiEntity baby, CardiganCorgiEntity otherParent) {
        // if tree determines if baby is Black, Red, or Sable
        if (this.getBaseColor() == 0 && otherParent.getBaseColor() == 0) {
            // if both parents are Red, baby has 25% chance to be Black, 50% chance to be Red, and 25% chance
            // to be Sable
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setBaseColor(1);
            } else if (determine < 4) {
                baby.setBaseColor(0);
            } else {
                baby.setBaseColor(2);
            }
        } else if ((this.getBaseColor() == 1 && otherParent.getBaseColor() == 2) ||
                (this.getBaseColor() == 2 && otherParent.getBaseColor() == 1)) {
            // if one parent is Black and one parent is Sable, baby will be Red
            baby.setBaseColor(0);
        } else if (this.getBaseColor() == otherParent.getBaseColor()) {
            // if both parents are Black or both parents are Sable, baby will match the parents
            baby.setBaseColor(this.getBaseColor());
        } else {
            // if one parent is Red and the other is Black or Sable, baby will match one of their parents
            if (this.random.nextBoolean()) {
                baby.setBaseColor(this.getBaseColor());
            } else {
                baby.setBaseColor(otherParent.getBaseColor());
            }
        }

        // if tree determines if baby has merle or not
        if (this.isMerle() && otherParent.isMerle()) {
            // if both parents are merle, baby will be merle
            baby.setMerle(true);
        } else if (this.isMerle() || otherParent.isMerle()) {
            // if one parent is merle, baby has 50% chance to be merle and 50% chance not to be merle
            baby.setMerle(this.random.nextBoolean());
        } else {
            // if neither parent is merle, baby will not be merle
            baby.setMerle(false);
        }

        // if tree decides baby's visual variant
        if (baby.getBaseColor() == 1 && baby.isMerle()) {
            // if baby's base color is Black, and they have merle, baby will be Blue Merle
            baby.setVariant(CardiganCorgiVariant.BLUE_MERLE);
        } else if (baby.getBaseColor() == 0) {
            // if baby's base color is Red, they will be Red
            baby.setVariant(CardiganCorgiVariant.RED);
        } else if (baby.getBaseColor() == 1) {
            // if baby's base color is Black (and they don't have merle), baby will be Black
            baby.setVariant(CardiganCorgiVariant.BLACK);
        } else {
            // if baby's base color is Sable, baby will be Sable
            baby.setVariant(CardiganCorgiVariant.SABLE);
        }
    }
}
