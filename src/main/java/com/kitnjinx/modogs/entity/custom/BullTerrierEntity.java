package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.BullTerrierVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.item.ModItems;
import com.kitnjinx.modogs.util.ModTags;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.Random;
import java.util.function.Predicate;

public class BullTerrierEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BullTerrierEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SHOW_TARGET =
            SynchedEntityData.defineId(BullTerrierEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_RED =
            SynchedEntityData.defineId(BullTerrierEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_RED =
            SynchedEntityData.defineId(BullTerrierEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HIGH_WHITE =
            SynchedEntityData.defineId(BullTerrierEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COW || entitytype == EntityType.CHICKEN || entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER;
    };

    public BullTerrierEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Monster.class, true, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        BullTerrierEntity baby = ModEntityTypes.BULL_TERRIER.get().create(serverLevel);

        determineBabyVariant(baby, (BullTerrierEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);
        baby.setTarget(false);

        return baby;
    }

    private <E extends BullTerrierEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bull_terrier.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bull_terrier.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bull_terrier.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bull_terrier.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bull_terrier.idle"));
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
        Item itemForTaming2 = ModItems.CHICKEN_TREAT.get();

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

        if (item == Items.RED_DYE && this.isTame() && this.isOwnedBy(player) && !this.showTarget()) {
            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                this.setTarget(true);

                return InteractionResult.SUCCESS;
            }
        }

        if (Registry.ITEM.getHolderOrThrow(Registry.ITEM.getResourceKey(item).get()).is(ItemTags.WOOL)
                && this.isTame() && this.isOwnedBy(player) && this.showTarget()) {
            if (this.level.isClientSide) {
                return InteractionResult.PASS;
            } else {
                this.setTarget(false);

                return InteractionResult.SUCCESS;
            }
        }

        if (item == ModItems.GENE_TESTER.get()) {
            if (this.level.isClientSide) {
                Component message;
                if (this.isRed()) {
                    if (this.getVariant() == BullTerrierVariant.WHITE) {
                        message = Component.literal("This Bull Terrier demonstrates a fully white coat. They also have the alleles for red fur.");
                    } else if (this.hasHighWhite()) {
                        message = Component.literal("This Bull Terrier demonstrates a recessive trait and high amounts of white markings.");
                    } else {
                        message = Component.literal("This Bull Terrier demonstrates a recessive trait.");
                    }
                } else if (this.carriesRed()) {
                    if (this.getVariant() == BullTerrierVariant.WHITE) {
                        message = Component.literal("This Bull Terrier demonstrates a fully white coat. They also carry a recessive trait.");
                    } else if (this.hasHighWhite()) {
                        message = Component.literal("This Bull Terrier demonstrates high amounts of white markings and carries a recessive trait.");
                    } else {
                        message = Component.literal("This Bull Terrier carries a recessive trait.");
                    }
                } else {
                    if (this.getVariant() == BullTerrierVariant.WHITE) {
                        message = Component.literal("This Bull Terrier demonstrates a fully white coat.");
                    } else if (this.hasHighWhite()) {
                        message = Component.literal("This Bull Terrier demonstrates high amounts of white markings.");
                    } else {
                        message = Component.literal("This Bull Terrier doesn't have any recessive traits.");
                    }
                }
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
        this.entityData.set(SHOW_TARGET, tag.getBoolean("Target"));
        this.entityData.set(IS_RED, tag.getBoolean("IsRed"));
        this.entityData.set(CARRIES_RED, tag.getBoolean("CarriesRed"));
        this.entityData.set(HIGH_WHITE, tag.getBoolean("HighWhite"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("Target", this.showTarget());
        tag.putBoolean("IsRed", this.isRed());
        tag.putBoolean("CarriesRed", this.carriesRed());
        tag.putBoolean("HighWhite", this.hasHighWhite());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(SHOW_TARGET, false);
        this.entityData.define(IS_RED, false);
        this.entityData.define(CARRIES_RED, false);
        this.entityData.define(HIGH_WHITE, true);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
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
        int whiteLevel = r.nextInt(4) + 1;
        int determine = r.nextInt(4) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (whiteLevel < 3) {
            var = 0; // WHITE
            setHighWhite(true);
            if (determine != 4) {
                setRedStatus(carrier == 1, false);
            } else {
                setRedStatus(true, true);
            }
        } else if (whiteLevel == 3) {
            setHighWhite(false);
            if (determine != 4) {
                var = 1; // BLACK
                setRedStatus(carrier == 1, false);
            } else {
                var = 2; // RED
                setRedStatus(true, true);
            }
        } else {
            setHighWhite(true);
            if (determine != 4) {
                var = 3; // WHITE_BLACK
                setRedStatus(carrier == 1, false);
            } else {
                var = 4; // WHITE_RED
                setRedStatus(true, true);
            }
        }

        // assign chosen variant and finish the method
        BullTerrierVariant variant = BullTerrierVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        setTarget(false);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public BullTerrierVariant getVariant() {
        return BullTerrierVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BullTerrierVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean showTarget() {
        return this.entityData.get(SHOW_TARGET);
    }

    private void setTarget(boolean show) {
        this.entityData.set(SHOW_TARGET, show);
    }

    public boolean isRed() {
        return this.entityData.get(IS_RED);
    }

    public boolean carriesRed() {
        return this.entityData.get(CARRIES_RED);
    }

    private void setRedStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_RED, carrier);
        this.entityData.set(IS_RED, is);
    }

    public boolean hasHighWhite() {
        return this.entityData.get(HIGH_WHITE);
    }

    private void setHighWhite(boolean status) {
        this.entityData.set(HIGH_WHITE, status);
    }

    private void determineBabyVariant(BullTerrierEntity baby, BullTerrierEntity otherParent) {
        // determine if baby is red or black
        if (this.isRed() && otherParent.isRed()) {
            // if both parents are red, baby will be red
            baby.setRedStatus(true, true);
        } else if ((this.isRed() && otherParent.carriesRed()) || (this.carriesRed() && otherParent.isRed())) {
            // if one parent is red and the other is a carrier, baby has 50% chance to be red and 50% chance to
            // be a carrier
            baby.setRedStatus(true, this.random.nextBoolean());
        } else if (this.isRed() || otherParent.isRed()) {
            // if only one parent is red, baby will be a carrier
            baby.setRedStatus(true, false);
        } else if (this.carriesRed() && otherParent.carriesRed()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be red
            int determine = this.random.nextInt(4) + 1;
            baby.setRedStatus(determine > 1, determine == 4);
        } else if (this.carriesRed() || otherParent.carriesRed()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setRedStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setRedStatus(false, false);
        }

        // determine if baby has low white, high white, or is pure white
        boolean pureWhite;
        if (this.getVariant() == BullTerrierVariant.WHITE && otherParent.getVariant() == BullTerrierVariant.WHITE) {
            // if both parents are white, baby will be white
            baby.setHighWhite(true);
            pureWhite = true;
        } else if ((this.getVariant() == BullTerrierVariant.WHITE && otherParent.hasHighWhite()) ||
                (this.hasHighWhite() && otherParent.getVariant() == BullTerrierVariant.WHITE)) {
            // if one parent is pure white and the other has high white, baby has 50% chance to be pure white
            // and 50% chance to have high white
            baby.setHighWhite(true);
            pureWhite = this.random.nextBoolean();
        } else if (this.getVariant() == BullTerrierVariant.WHITE || otherParent.getVariant() == BullTerrierVariant.WHITE) {
            // if only one parent is white, baby will have high white
            baby.setHighWhite(true);
            pureWhite = false;
        } else if (this.hasHighWhite() && otherParent.hasHighWhite()) {
            // if both parents have high white, baby has 25% chance to have low white, 50% chance to have high
            // white, and 25% chance to be white
            int determine = this.random.nextInt(4) + 1;
            baby.setHighWhite(determine > 1);
            pureWhite = determine == 4;
        } else if (this.hasHighWhite() || otherParent.hasHighWhite()) {
            // if only one parent has high white, baby will have 50/50 chance to have high white
            baby.setHighWhite(this.random.nextBoolean());
            pureWhite = false;
        } else {
            // if neither parent has high white, baby will not have high white
            baby.setHighWhite(false);
            pureWhite = false;
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (pureWhite) {
            baby.setVariant(BullTerrierVariant.WHITE);
        } else if (baby.hasHighWhite() && baby.isRed()) {
            baby.setVariant(BullTerrierVariant.WHITE_RED);
        } else if (baby.hasHighWhite()) {
            baby.setVariant(BullTerrierVariant.WHITE_BLACK);
        } else if (baby.isRed()) {
            baby.setVariant(BullTerrierVariant.RED);
        } else {
            baby.setVariant(BullTerrierVariant.BLACK);
        }
    }
}