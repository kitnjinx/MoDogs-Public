package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.MastiffVariant;
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

import java.util.Random;
import java.util.function.Predicate;

public class MastiffEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(MastiffEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_APRICOT =
            SynchedEntityData.defineId(MastiffEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COW || entitytype == EntityType.MOOSHROOM || entitytype == EntityType.ZOMBIE_VILLAGER || entitytype == EntityType.ZOMBIE;
    };

    public MastiffEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 22.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.ARMOR, 2.0f)
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
        MastiffEntity baby = ModEntityTypes.MASTIFF.get().create(serverLevel);

        determineBabyVariant(baby, (MastiffEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends MastiffEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mastiff.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mastiff.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mastiff.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mastiff.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mastiff.idle"));
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

        if (item == ModItems.GENE_TESTER.get())  {
            if (this.level.isClientSide) {
                TextComponent message;
                if (this.getVariant() == MastiffVariant.APRICOT) {
                    message = new TextComponent("This Mastiff demonstrates a recessive trait.");
                } else if (this.isCarrier()) {
                    message = new TextComponent("This Mastiff carries a recessive trait.");
                } else {
                    message = new TextComponent("This Mastiff doesn't have any recessive traits.");
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
        this.entityData.set(CARRIES_APRICOT, tag.getBoolean("IsCarrier"));
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
        this.entityData.define(CARRIES_APRICOT, false);
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
        int determine = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 4) {
            var = 0; // FAWN
            setCarrier(r.nextInt(4) + 1 == 4);
        } else {
            var = 1; // APRICOT
            setCarrier(true);
        }

        // assign chosen variant and finish the method
        MastiffVariant variant = MastiffVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public MastiffVariant getVariant() {
        return MastiffVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(MastiffVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isCarrier() {
        return this.entityData.get(CARRIES_APRICOT);
    }

    private void setCarrier(boolean status) {
        this.entityData.set(CARRIES_APRICOT, status);
    }

    private void determineBabyVariant(MastiffEntity baby, MastiffEntity otherParent) {
        // determine if baby is apricot, fawn and carries apricot, or fawn and not a carrier
        if (this.getVariant() == MastiffVariant.APRICOT && otherParent.getVariant() == MastiffVariant.APRICOT) {
            // if both parents are apricot, baby will be apricot
            baby.setCarrier(true);
            baby.setVariant(MastiffVariant.APRICOT);
        } else if ((this.getVariant() == MastiffVariant.APRICOT && otherParent.isCarrier()) ||
                (this.isCarrier() && otherParent.getVariant() == MastiffVariant.APRICOT)) {
            // if one parent is apricot and the other is a carrier, baby has 50% chance to be apricot and 50%
            // chance to be a carrier
            baby.setCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(MastiffVariant.APRICOT);
            } else {
                baby.setVariant(MastiffVariant.FAWN);
            }
        } else if (this.getVariant() == MastiffVariant.APRICOT || otherParent.getVariant() == MastiffVariant.APRICOT) {
            // if one parent is apricot and the other is not a carrier, baby will be a carrier
            baby.setCarrier(true);
            baby.setVariant(MastiffVariant.FAWN);
        } else if (this.isCarrier() && otherParent.isCarrier()) {
            // if both parents are carriers, baby has 25% chance to not carry, 50% chance to be a carrier, and
            // 25% chance to be apricot
            int determine = this.random.nextInt(4) + 1;
            baby.setCarrier(determine > 1);
            if (determine < 4) {
                baby.setVariant(MastiffVariant.FAWN);
            } else {
                baby.setVariant(MastiffVariant.APRICOT);
            }
        } else if (this.isCarrier() || otherParent.isCarrier()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setCarrier(this.random.nextBoolean());
            baby.setVariant(MastiffVariant.FAWN);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setCarrier(false);
            baby.setVariant(MastiffVariant.FAWN);
        }
    }

    /* COLLAR */
    @Override
    protected void applyReinforcedCollar(Item item) {
        super.applyReinforcedCollar(item);
        getAttribute(Attributes.ARMOR).setBaseValue(7.0);
    }

    @Override
    protected void applyGoldPlatedCollar(Item item) {
        super.applyGoldPlatedCollar(item);
        getAttribute(Attributes.ARMOR).setBaseValue(11.0);
    }

    @Override
    protected void applyIronInfusedCollar(Item item) {
        super.applyIronInfusedCollar(item);
        getAttribute(Attributes.ARMOR).setBaseValue(15.0);
    }

    @Override
    protected void applyDiamondCrustedCollar(Item item) {
        super.applyDiamondCrustedCollar(item);
        getAttribute(Attributes.ARMOR).setBaseValue(20.0);
    }

    @Override
    protected void applyNetheriteLacedCollar(Item item) {
        super.applyNetheriteLacedCollar(item);
        getAttribute(Attributes.ARMOR).setBaseValue(26.0);
    }

    @Override
    protected void removeCollar() {
        super.removeCollar();
    }

    @Override
    protected void removeReinforcedCollar() {
        super.removeReinforcedCollar();
        getAttribute(Attributes.ARMOR).setBaseValue(2.0);
    }

    @Override
    protected void removeGoldPlatedCollar() {
        super.removeGoldPlatedCollar();
        getAttribute(Attributes.ARMOR).setBaseValue(2.0);
    }

    @Override
    protected void removeIronInfusedCollar() {
        super.removeIronInfusedCollar();
        getAttribute(Attributes.ARMOR).setBaseValue(2.0);
    }

    @Override
    protected void removeDiamondCrustedCollar() {
        super.removeDiamondCrustedCollar();
        getAttribute(Attributes.ARMOR).setBaseValue(2.0);
    }

    @Override
    protected void removeNetheriteLacedCollar() {
        super.removeNetheriteLacedCollar();
        getAttribute(Attributes.ARMOR).setBaseValue(2.0);
    }
}
