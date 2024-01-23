package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.BorderCollieVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.pattern_variation.ThreeMerleVariant;
import com.kitnjinx.modogs.entity.variant.pattern_variation.BorderCollieStripeVariant;
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
import net.minecraft.world.entity.monster.ZombieVillager;
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

public class BorderCollieEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Boolean> CARRIES_RED =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_RED =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_LILAC =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MERLE =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BASE_COLOR =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MERLE_PATTERN =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HEAD_STRIPE =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.INT);
    /* Explanation of Genetic Variables
    CARRIES_RED: true for Red-based dogs, black dogs carrying red, or lilac that carry red
    IS_RED: true for Red-based dogs and lilacs that would be red without dilution
    CARRIES_LILAC: true for Red or Black dogs that carry dilution, and true for lilac dogs
    MERLE: true for dogs with a _MERLE variant
    BASE_COLOR: Dogs TYPE_VARIANT without merle. 0 for Black or Black Merles, 1 for Red or Red Merles, and
        2 for Lilacs and Lilac Merles
    MERLE_PATTERN: Which pattern the dog has for merle (1, 2, or 3)
    HEAD_STRIPE: Which white head stripe the dog has (1, 2, 3, or 4)
     */

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.ZOMBIE_HORSE || entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER;
    };

    public BorderCollieEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 18.0)
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
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, ZombieVillager.class, false, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        BorderCollieEntity baby = ModEntityTypes.BORDER_COLLIE.get().create(serverLevel);

        determineBabyVariant(baby, (BorderCollieEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.border_collie.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.border_collie.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.border_collie.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.border_collie.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.border_collie.idle", Animation.LoopType.LOOP));
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

        Item itemForTaming = ModItems.MUTTON_TREAT.get();

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
                Component message;
                Component part1;
                if (this.getBaseColor() == 2) {
                    if (this.isMerle()) {
                        part1 = Component.literal("This Border Collie demonstrates a recessive trait, as well as the merle trait.");
                    } else {
                        part1 = Component.literal("This Border Collie demonstrates a recessive trait.");
                    }

                    if (this.isRed()) {
                        message = Component.literal(part1.getString() + " They also have the alleles for red fur.");
                    } else if (this.getRedCarrier()) {
                        message = Component.literal(part1.getString() + " They also carry the red fur trait.");
                    } else {
                        message = Component.literal(part1.getString() + " They have otherwise standard traits.");
                    }
                } else if (this.getBaseColor() == 1) {
                    if (this.isMerle()) {
                        part1 = Component.literal("This Border Collie demonstrates a recessive trait, as well as the merle trait.");
                    } else {
                        part1 = Component.literal("This Border Collie demonstrates a recessive trait.");
                    }

                    if (this.getLilacCarrier()) {
                        message = Component.literal(part1.getString() + " They also carry the recessive dilution trait.");
                    } else {
                        message = part1;
                    }
                } else {
                    if (this.getRedCarrier() && this.getLilacCarrier()) {
                        part1 = Component.literal("This Border Collie carries two recessive traits.");
                    } else if (this.getRedCarrier()) {
                        part1 = Component.literal("This Border Collie carries the recessive red fur trait.");
                    } else if (this.getLilacCarrier()) {
                        part1 = Component.literal("This Border Collie carries the recessive dilution trait.");
                    } else {
                        part1 = Component.literal("This Border Collie doesn't have any recessive traits.");
                    }

                    if (this.isMerle()) {
                        message = Component.literal(part1.getString() + " They demonstrate the merle trait.");
                    } else {
                        message = part1;
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
        this.entityData.set(CARRIES_RED, tag.getBoolean("RedCarrier"));
        this.entityData.set(IS_RED, tag.getBoolean("IsRed"));
        this.entityData.set(CARRIES_LILAC, tag.getBoolean("LilacCarrier"));
        this.entityData.set(MERLE, tag.getBoolean("Merle"));
        this.entityData.set(BASE_COLOR, tag.getInt("BaseColor"));
        this.entityData.set(MERLE_PATTERN, tag.getInt("MerlePattern"));
        this.entityData.set(HEAD_STRIPE, tag.getInt("HeadStripe"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("RedCarrier", this.getRedCarrier());
        tag.putBoolean("IsRed", this.isRed());
        tag.putBoolean("LilacCarrier", this.getLilacCarrier());
        tag.putBoolean("Merle", this.isMerle());
        tag.putInt("BaseColor", this.getBaseColor());
        tag.putInt("MerlePattern", this.getMerlePattern());
        tag.putInt("HeadStripe", this.getHeadStripe());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CARRIES_RED, false);
        this.entityData.define(IS_RED, false);
        this.entityData.define(CARRIES_LILAC, false);
        this.entityData.define(MERLE, false);
        this.entityData.define(BASE_COLOR, 0);
        this.entityData.define(MERLE_PATTERN, 1);
        this.entityData.define(HEAD_STRIPE, 1);
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
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
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
        int merle = r.nextInt(3) + 1;
        int carrier = r.nextInt(8) + 1;

        /* Brief explanation of different variables:
        determine: Used to decide if the dog's phenotype (visible coat) is Black, Red, or Lilac
        merle: Used to determine if dog is merle or not
        carrier: Used to determine potential carrier status for recessive traits (Red and Lilac)
         */

        setMerle(merle == 3);

        if (determine < 6) {
            // BLACK
            setBaseColor(0);
            setRedStatus(carrier == 1, false);
            setLilacCarrier(carrier == 2);
        } else if (determine < 9) {
            // RED
            setBaseColor(1);
            setRedStatus(true, true);
            setLilacCarrier(carrier == 1);
        } else {
            // LILAC
            setBaseColor(2);
            setLilacCarrier(true);
            if ((r.nextInt(8) + 1) < 6) {
                setRedStatus(true, true);
            } else {
                setRedStatus(carrier == 1, false);
            }
        }

        // assign chosen variant and finish the method
        setMerlePattern(r.nextInt(3) + 1);
        setHeadStripe(r.nextInt(4) + 1);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public boolean getRedCarrier() {
        return this.entityData.get(CARRIES_RED);
    }

    public boolean isRed() {
        return this.entityData.get(IS_RED);
    }

    private void setRedStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_RED, carrier);
        this.entityData.set(IS_RED, is);
    }

    public boolean getLilacCarrier() {
        return this.entityData.get(CARRIES_LILAC);
    }

    private void setLilacCarrier(boolean status) {
        this.entityData.set(CARRIES_LILAC, status);
    }

    public boolean isMerle() {
        return this.entityData.get(MERLE);
    }

    private void setMerle(boolean status) {
        this.entityData.set(MERLE, status);
    }

    public int getBaseColor() {
        return this.entityData.get(BASE_COLOR);
    }

    public BorderCollieVariant getVariant() {
        return BorderCollieVariant.byId(this.getBaseColor() & 255);
    }

    private void setBaseColor(int base) {
        this.entityData.set(BASE_COLOR, base);
    }

    public ThreeMerleVariant getMerleVariant() {
        return ThreeMerleVariant.byId((this.getMerlePattern() - 1) & 255);
    }

    public int getMerlePattern() {
        return this.entityData.get(MERLE_PATTERN);
    }

    private void setMerlePattern(int pattern) {
        this.entityData.set(MERLE_PATTERN, pattern);
    }

    public BorderCollieStripeVariant getStripeVariant() {
        return BorderCollieStripeVariant.byId(this.getHeadStripe() & 255);
    }

    public int getHeadStripe() {
        return this.entityData.get(HEAD_STRIPE);
    }

    private void setHeadStripe(int stripe) {
        this.entityData.set(HEAD_STRIPE, stripe);
    }

    private void determineBabyVariant(BorderCollieEntity baby, BorderCollieEntity otherParent) {
        // determine if baby is red or black
        if (this.isRed() && otherParent.isRed()) {
            // if both parents are red, baby will be red
            baby.setRedStatus(true, true);
        } else if ((this.isRed() && otherParent.getRedCarrier()) ||
                (this.getRedCarrier() && otherParent.isRed())) {
            // if one parent is red and the other is a carrier, baby has 50% chance to be red and 50% chance
            // to be a carrier
            baby.setRedStatus(true, this.random.nextBoolean());
        } else if (this.isRed() || otherParent.isRed()) {
            // if only one parent is red, baby will be a carrier
            baby.setRedStatus(true, false);
        } else if (this.getRedCarrier() && otherParent.getRedCarrier()) {
            // if both parents are carriers, baby has 25% chance to be red, 50% chance to be a carrier,
            // and 25% chance not to be a carrier
            int determine = this.random.nextInt(4) + 1;
            baby.setRedStatus(determine > 1, determine == 4);
        } else if (this.getRedCarrier() || otherParent.getRedCarrier()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setRedStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setRedStatus(false, false);
        }

        // determine if baby is diluted (lilac), a carrier, or neither
        boolean isLilac;
        if (this.getBaseColor() == 2 && otherParent.getBaseColor() == 2) {
            // if both parents are lilac, baby will be lilac
            isLilac = true;
            baby.setLilacCarrier(true);
        } else if ((this.getBaseColor() == 2 && otherParent.getLilacCarrier()) || (this.getLilacCarrier() && otherParent.getBaseColor() == 2)) {
            // if one parent is lilac and the other is a carrier, baby has 50% chance to carry lilac and
            // 50% chance to be lilac
            baby.setLilacCarrier(true);
            isLilac = this.random.nextBoolean();
        } else if (this.getBaseColor() == 2 || otherParent.getBaseColor() == 2) {
            // if only one parent is lilac, baby will be a carrier
            baby.setLilacCarrier(true);
            isLilac = false;
        } else if (this.getLilacCarrier() && otherParent.getLilacCarrier()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier,
            // and 25% chance to be lilac
            int determine = this.random.nextInt(4) + 1;
            baby.setLilacCarrier(determine > 1);
            isLilac = determine == 4;
        } else if (this.getLilacCarrier() || otherParent.getLilacCarrier()) {
            // if only one parent is a carrier, baby has 50% chance to be a carrier
            baby.setLilacCarrier(this.random.nextBoolean());
            isLilac = false;
        } else {
            // if neither parent is lilac, baby won't be lilac
            baby.setLilacCarrier(false);
            isLilac = false;
        }

        // determine if baby is merle or not
        if (this.isMerle() && otherParent.isMerle()) {
            // if both parents are merle, baby will be merle
            baby.setMerle(true);
        } else if (this.isMerle() || otherParent.isMerle()) {
            // if one parent is merle and the other is not, baby has 50% chance to be merle
            baby.setMerle(this.random.nextBoolean());
        } else {
            // if neither parent is merle, baby will not be merle
            baby.setMerle(false);
        }

        // determine baby's phenotype
        baby.setMerlePattern(this.random.nextInt(3) + 1);

        if (this.random.nextBoolean()) {
            baby.setHeadStripe(this.getHeadStripe());
        } else {
            baby.setHeadStripe(otherParent.getHeadStripe());
        }

        if (isLilac) {
            baby.setBaseColor(2);
        } else if (baby.isRed()) {
            baby.setBaseColor(1);
        } else {
            baby.setBaseColor(0);
        }
    }
}