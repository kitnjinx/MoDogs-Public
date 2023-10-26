package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.LabRetrieverVariant;
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
import net.minecraft.world.entity.animal.Turtle;
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

public class LabRetrieverEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(LabRetrieverEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_CHOCOLATE =
            SynchedEntityData.defineId(LabRetrieverEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_CHOCOLATE =
            SynchedEntityData.defineId(LabRetrieverEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_YELLOW =
            SynchedEntityData.defineId(LabRetrieverEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COD || entitytype == EntityType.SALMON || entitytype == EntityType.TROPICAL_FISH;
    };

    public LabRetrieverEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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

        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        LabRetrieverEntity baby = ModEntityTypes.LAB_RETRIEVER.get().create(serverLevel);

        // Determines baby based on the parents
        determineBabyVariant(baby, (LabRetrieverEntity) otherParent);

        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends LabRetrieverEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.lab_retriever.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.lab_retriever.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.lab_retriever.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.lab_retriever.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.lab_retriever.idle"));
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

        Item itemForTaming = ModItems.SALMON_TREAT.get();

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
                if (this.getVariant() == LabRetrieverVariant.YELLOW) {
                    if (this.isChocolate()) {
                        message = new TextComponent("This Labrador Retriever demonstrates a recessive trait, and has the alleles for chocolate fur.");
                    } else if (this.getChocolateCarrier()) {
                        message = new TextComponent("This Labrador Retriever demonstrates a recessive trait, and carries the trait for chocolate fur.");
                    } else {
                        message = new TextComponent("This Labrador Retriever demonstrates a recessive trait. It has otherwise standard traits.");
                    }
                } else if (this.getVariant() == LabRetrieverVariant.CHOCOLATE) {
                    if (this.getYellowCarrier()) {
                        message = new TextComponent("This Labrador Retriever demonstrates a recessive trait. They carry the trait for yellow fur.");
                    } else {
                        message = new TextComponent("This Labrador Retriever demonstrates a recessive trait.");
                    }
                } else {
                    if (this.getChocolateCarrier() && this.getYellowCarrier()) {
                        message = new TextComponent("This Labrador Retriever carries two recessive traits.");
                    } else if (this.getChocolateCarrier()) {
                        // Black, carries Chocolate
                        message = new TextComponent("This Labrador Retriever carries the chocolate fur trait.");
                    } else if (this.getYellowCarrier()) {
                        // Black, carries Yellow
                        message = new TextComponent("This Labrador Retriever carries the yellow fur trait.");
                    } else {
                        // Black, not a carrier
                        message = new TextComponent("This Labrador Retriever doesn't have any recessive traits.");
                    }
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
        this.entityData.set(CARRIES_CHOCOLATE, tag.getBoolean("ChocolateCarrier"));
        this.entityData.set(IS_CHOCOLATE, tag.getBoolean("IsChocolate"));
        this.entityData.set(CARRIES_YELLOW, tag.getBoolean("YellowCarrier"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("ChocolateCarrier", this.getChocolateCarrier());
        tag.putBoolean("IsChocolate", this.isChocolate());
        tag.putBoolean("YellowCarrier", this.getYellowCarrier());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(CARRIES_CHOCOLATE, false);
        this.entityData.define(IS_CHOCOLATE, false);
        this.entityData.define(CARRIES_YELLOW, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.35f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(22.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.3f);
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
            var = 0;
            setChocolateStatus(carrier == 1, false);
            setYellowCarrier(carrier == 2);
        } else if (determine < 9) {
            var = 1;
            setChocolateStatus(true, true);
            setYellowCarrier(carrier == 1);
        } else {
            var = 2;
            setYellowCarrier(true);
            if (r.nextInt(8) + 1 < 6) {
                setChocolateStatus(carrier == 1, false);
            } else {
                setChocolateStatus(true, true);
            }
        }

        // assign chosen variant and finish the method
        LabRetrieverVariant variant = LabRetrieverVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public LabRetrieverVariant getVariant() {
        return LabRetrieverVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(LabRetrieverVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean getChocolateCarrier() {
        return this.entityData.get(CARRIES_CHOCOLATE);
    }

    public boolean isChocolate() {
        return this.entityData.get(IS_CHOCOLATE);
    }

    private void setChocolateStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_CHOCOLATE, carrier);
        this.entityData.set(IS_CHOCOLATE, is);
    }

    public boolean getYellowCarrier() {
        return this.entityData.get(CARRIES_YELLOW);
    }

    private void setYellowCarrier(boolean carrier) {
        this.entityData.set(CARRIES_YELLOW, carrier);
    }

    private void determineBabyVariant(LabRetrieverEntity baby, LabRetrieverEntity otherParent) {
        // determine if baby is black or chocolate
        if (this.isChocolate() && otherParent.isChocolate()) {
            // if both parents are chocolate, baby will be chocolate
            baby.setChocolateStatus(true, true);
        } else if ((this.isChocolate() && otherParent.getChocolateCarrier()) ||
                (this.getChocolateCarrier() && otherParent.isChocolate())) {
            // if one parent is chocolate and the other is a chocolate carrier, baby has 50% chance to be
            // chocolate and 50% chance to carry chocolate
            baby.setChocolateStatus(true, this.random.nextBoolean());
        } else if (this.isChocolate() || otherParent.isChocolate()) {
            // if one parent is chocolate and the other is not a carrier, baby will be a carrier
            baby.setChocolateStatus(true, false);
        } else if (this.getChocolateCarrier() && otherParent.getChocolateCarrier()) {
            // if both parents are carriers, baby has 25% chance not to carry chocolate, 50% chance to carry
            // chocolate, and 25% chance to be chocolate
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setChocolateStatus(false, false);
            } else {
                baby.setChocolateStatus(true, determine == 4);
            }
        } else if (this.getChocolateCarrier() || otherParent.getChocolateCarrier()) {
            //if only one parent is a carrier, baby has 50% chance to carry chocolate and 50% chance not to
            baby.setChocolateStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will be black and won't carry chocolate
            baby.setChocolateStatus(false, false);
        }

        // determine if baby is yellow, and if not set their phenotype (TYPE_VARIANT) to black or chocolate
        // as determined above
        if (this.getVariant() == LabRetrieverVariant.YELLOW && otherParent.getVariant() == LabRetrieverVariant.YELLOW) {
            // if both parents are yellow, baby will be yellow
            baby.setYellowCarrier(true);
            baby.setVariant(LabRetrieverVariant.YELLOW);
        } else if ((this.getVariant() == LabRetrieverVariant.YELLOW && otherParent.getYellowCarrier()) ||
                (this.getYellowCarrier() && otherParent.getVariant() == LabRetrieverVariant.YELLOW)) {
            // if one parent is yellow and the other is a yellow carrier, baby has 50% chance to be
            // yellow and 50% chance to carry yellow
            baby.setYellowCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(LabRetrieverVariant.YELLOW);
            } else {
                setBabyBlackChocolate(baby);
            }
        } else if (this.getVariant() == LabRetrieverVariant.YELLOW ||
                otherParent.getVariant() == LabRetrieverVariant.YELLOW) {
            // if one parent is yellow and the other is not a carrier, baby will be a carrier
            baby.setYellowCarrier(true);
            setBabyBlackChocolate(baby);
        } else if (this.getYellowCarrier() && otherParent.getYellowCarrier()) {
            // if both parents are carriers, baby has 25% chance not to carry yellow, 50% chance to carry
            // yellow, and 25% chance to be yellow
            int determine = this.random.nextInt(4) + 1;
            if (determine == 4) {
                baby.setYellowCarrier(true);
                baby.setVariant(LabRetrieverVariant.YELLOW);
            } else {
                setBabyBlackChocolate(baby);
                baby.setYellowCarrier(determine > 1);
            }
        } else if (this.getYellowCarrier() || otherParent.getYellowCarrier()) {
            //if only one parent is a carrier, baby has 50% chance to carry yellow and 50% chance not to
            baby.setYellowCarrier(this.random.nextBoolean());
            setBabyBlackChocolate(baby);
        } else {
            // if neither parent is a carrier, baby won't carry yellow
            baby.setYellowCarrier(false);
            setBabyBlackChocolate(baby);
        }
    }

    private void setBabyBlackChocolate(LabRetrieverEntity baby) {
        if (baby.isChocolate()) {
            baby.setVariant(LabRetrieverVariant.CHOCOLATE);
        } else {
            baby.setVariant(LabRetrieverVariant.BLACK);
        }
    }
}