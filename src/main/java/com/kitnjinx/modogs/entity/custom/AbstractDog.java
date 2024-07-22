package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.item.ModItems;
import com.kitnjinx.modogs.util.ModTags;
import com.kitnjinx.modogs.world.level.storage.loot.ModBuiltInLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public abstract class AbstractDog extends TamableAnimal implements GeoEntity, NeutralMob {
    // animations & level access
    private AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    // handles if the animal is sitting
    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(AbstractDog.class, EntityDataSerializers.BOOLEAN);

    // handles collar variant
    private static final EntityDataAccessor<Integer> DATA_ID_COLLAR_VARIANT =
            SynchedEntityData.defineId(AbstractDog.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_ARMOR_VARIANT =
            SynchedEntityData.defineId(AbstractDog.class, EntityDataSerializers.INT);

    // following variables handle animal aggression/hunting
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(AbstractDog.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    @Nullable
    private UUID persistentAngerTarget;

    public AbstractDog(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(PathType.DANGER_POWDER_SNOW, -1.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.1D, true));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 1.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        // individual dog prey lists on 5, 6, & 7 priorities, found in individual classes
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(ModTags.Items.DOG_TREAT);
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound(){
        if (this.isAngry() || this.isAggressive()) {
            return SoundEvents.WOLF_GROWL;
        }
        return SoundEvents.WOLF_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource){
        return SoundEvents.WOLF_HURT;
    }

    protected SoundEvent getDeathSound(){
        return SoundEvents.WOLF_DEATH;
    }

    /* TAMABLE */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (item == ModItems.HEALING_TREAT.get()  && player.getUUID() != this.getPersistentAngerTarget() &&
                this.getHealth() != this.getMaxHealth()) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                this.setHealth(this.getMaxHealth());
            }

            return InteractionResult.SUCCESS;
        }

        if(itemstack.is(ModTags.Items.COLLAR) && this.getCollar() == CollarVariant.NONE
                && this.isTame() && this.isOwnedBy(player)) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                applyCollar(item);
            }

            return InteractionResult.SUCCESS;
        }

        if(itemstack.is(ModTags.Items.REINFORCED_COLLAR) && this.getCollar() == CollarVariant.NONE
                && this.isTame() && this.isOwnedBy(player)) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                applyReinforcedCollar(item);
            }

            return InteractionResult.SUCCESS;
        }

        if(itemstack.is(ModTags.Items.GOLD_PLATED_COLLAR) && this.getCollar() == CollarVariant.NONE
                && this.isTame() && this.isOwnedBy(player)) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                applyGoldPlatedCollar(item);
            }

            return InteractionResult.SUCCESS;
        }

        if(itemstack.is(ModTags.Items.IRON_INFUSED_COLLAR) && this.getCollar() == CollarVariant.NONE
                && this.isTame() && this.isOwnedBy(player)) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                applyIronInfusedCollar(item);
            }

            return InteractionResult.SUCCESS;
        }

        if(itemstack.is(ModTags.Items.DIAMOND_CRUSTED_COLLAR) && this.getCollar() == CollarVariant.NONE
                && this.isTame() && this.isOwnedBy(player)) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                applyDiamondCrustedCollar(item);
            }

            return InteractionResult.SUCCESS;
        }

        if(itemstack.is(ModTags.Items.NETHERITE_LACED_COLLAR) && this.getCollar() == CollarVariant.NONE
                && this.isTame() && this.isOwnedBy(player)) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                applyNetheriteLacedCollar(item);
            }

            return InteractionResult.SUCCESS;
        }

        if(this.getCollar() != CollarVariant.NONE && item == Items.SHEARS
                && this.isTame() && this.isOwnedBy(player)) {
            this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (this.getArmor() == ArmorVariant.NONE) {
                removeCollar();
            } else if (this.getArmor() == ArmorVariant.REINFORCED) {
                removeReinforcedCollar();
            } else if (this.getArmor() == ArmorVariant.GOLD_PLATED) {
                removeGoldPlatedCollar();
            } else if (this.getArmor() == ArmorVariant.IRON_INFUSED) {
                removeIronInfusedCollar();
            } else if (this.getArmor() == ArmorVariant.DIAMOND_CRUSTED) {
                removeDiamondCrustedCollar();
            } else {
                removeNetheriteLacedCollar();
            }

            return InteractionResult.SUCCESS;
        }

        if(isFood(itemstack)) {
            return super.mobInteract(player, hand);
        }

        if (item == ModItems.GENO_READER.get()) {
            return InteractionResult.PASS;
        }

        if(isTame() && !this.level().isClientSide && hand == InteractionHand.MAIN_HAND && this.isOwnedBy(player)) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;

        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
        this.entityData.set(DATA_ID_COLLAR_VARIANT, tag.getInt("Collar"));
        this.entityData.set(DATA_ID_ARMOR_VARIANT, tag.getInt("Armor"));
        this.entityData.set(DATA_REMAINING_ANGER_TIME, tag.getInt("Anger"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
        tag.putInt("Collar", this.getCollarTypeVariant());
        tag.putInt("Armor", this.getArmorTypeVariant());
        tag.putInt("Anger", this.getRemainingPersistentAngerTime());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SITTING, false);
        builder.define(DATA_ID_COLLAR_VARIANT, 0);
        builder.define(DATA_ID_ARMOR_VARIANT, 0);
        builder.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public PlayerTeam getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player) {
        return false;
    }

    /* VARIANTS */
    public CollarVariant getCollar() { return CollarVariant.byId(this.getCollarTypeVariant() & 255); }

    private int getCollarTypeVariant() { return this.entityData.get(DATA_ID_COLLAR_VARIANT); }

    public void setCollar(CollarVariant variant) {
        this.entityData.set(DATA_ID_COLLAR_VARIANT, variant.getId() & 255);
    }

    public ArmorVariant getArmor() {
        return ArmorVariant.byId(this.getArmorTypeVariant() & 255);
    }

    private int getArmorTypeVariant() {
        return this.entityData.get(DATA_ID_ARMOR_VARIANT);
    }

    public void setArmor(ArmorVariant variant) {
        this.entityData.set(DATA_ID_ARMOR_VARIANT, variant.getId() & 255);
    }

    protected void applyCollar(Item item) {
        if (item == ModItems.WHITE_COLLAR.get()) {
            this.setCollar(CollarVariant.WHITE);
        } else if (item == ModItems.LIGHT_GRAY_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_GRAY);
        } else if (item == ModItems.GRAY_COLLAR.get()) {
            this.setCollar(CollarVariant.GRAY);
        } else if (item == ModItems.BLACK_COLLAR.get()) {
            this.setCollar(CollarVariant.BLACK);
        } else if (item == ModItems.BROWN_COLLAR.get()) {
            this.setCollar(CollarVariant.BROWN);
        } else if (item == ModItems.RED_COLLAR.get()) {
            this.setCollar(CollarVariant.RED);
        } else if (item == ModItems.ORANGE_COLLAR.get()) {
            this.setCollar(CollarVariant.ORANGE);
        } else if (item == ModItems.YELLOW_COLLAR.get()) {
            this.setCollar(CollarVariant.YELLOW);
        } else if (item == ModItems.LIME_COLLAR.get()) {
            this.setCollar(CollarVariant.LIME);
        } else if (item == ModItems.GREEN_COLLAR.get()) {
            this.setCollar(CollarVariant.GREEN);
        } else if (item == ModItems.CYAN_COLLAR.get()) {
            this.setCollar(CollarVariant.CYAN);
        } else if (item == ModItems.LIGHT_BLUE_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_BLUE);
        } else if (item == ModItems.BLUE_COLLAR.get()) {
            this.setCollar(CollarVariant.BLUE);
        } else if (item == ModItems.PURPLE_COLLAR.get()) {
            this.setCollar(CollarVariant.PURPLE);
        } else if (item == ModItems.MAGENTA_COLLAR.get()) {
            this.setCollar(CollarVariant.MAGENTA);
        } else {
            this.setCollar(CollarVariant.PINK);
        }
    }

    protected void applyReinforcedCollar(Item item) {
        if (item == ModItems.WHITE_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.WHITE);
        } else if (item == ModItems.LIGHT_GRAY_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_GRAY);
        } else if (item == ModItems.GRAY_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.GRAY);
        } else if (item == ModItems.BLACK_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.BLACK);
        } else if (item == ModItems.BROWN_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.BROWN);
        } else if (item == ModItems.RED_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.RED);
        } else if (item == ModItems.ORANGE_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.ORANGE);
        } else if (item == ModItems.YELLOW_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.YELLOW);
        } else if (item == ModItems.LIME_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIME);
        } else if (item == ModItems.GREEN_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.GREEN);
        } else if (item == ModItems.CYAN_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.CYAN);
        } else if (item == ModItems.LIGHT_BLUE_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_BLUE);
        } else if (item == ModItems.BLUE_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.BLUE);
        } else if (item == ModItems.PURPLE_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.PURPLE);
        } else if (item == ModItems.MAGENTA_REINFORCED_COLLAR.get()) {
            this.setCollar(CollarVariant.MAGENTA);
        } else {
            this.setCollar(CollarVariant.PINK);
        }

        this.setArmor(ArmorVariant.REINFORCED);
        getAttribute(Attributes.ARMOR).setBaseValue(5.0);
    }

    protected void applyGoldPlatedCollar(Item item) {
        if (item == ModItems.WHITE_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.WHITE);
        } else if (item == ModItems.LIGHT_GRAY_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_GRAY);
        } else if (item == ModItems.GRAY_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.GRAY);
        } else if (item == ModItems.BLACK_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.BLACK);
        } else if (item == ModItems.BROWN_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.BROWN);
        } else if (item == ModItems.RED_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.RED);
        } else if (item == ModItems.ORANGE_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.ORANGE);
        } else if (item == ModItems.YELLOW_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.YELLOW);
        } else if (item == ModItems.LIME_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIME);
        } else if (item == ModItems.GREEN_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.GREEN);
        } else if (item == ModItems.CYAN_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.CYAN);
        } else if (item == ModItems.LIGHT_BLUE_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_BLUE);
        } else if (item == ModItems.BLUE_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.BLUE);
        } else if (item == ModItems.PURPLE_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.PURPLE);
        } else if (item == ModItems.MAGENTA_GOLD_PLATED_COLLAR.get()) {
            this.setCollar(CollarVariant.MAGENTA);
        } else {
            this.setCollar(CollarVariant.PINK);
        }

        this.setArmor(ArmorVariant.GOLD_PLATED);
        getAttribute(Attributes.ARMOR).setBaseValue(9.0);
        getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(2.0);
    }

    protected void applyIronInfusedCollar(Item item) {
        if (item == ModItems.WHITE_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.WHITE);
        } else if (item == ModItems.LIGHT_GRAY_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_GRAY);
        } else if (item == ModItems.GRAY_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.GRAY);
        } else if (item == ModItems.BLACK_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.BLACK);
        } else if (item == ModItems.BROWN_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.BROWN);
        } else if (item == ModItems.RED_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.RED);
        } else if (item == ModItems.ORANGE_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.ORANGE);
        } else if (item == ModItems.YELLOW_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.YELLOW);
        } else if (item == ModItems.LIME_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIME);
        } else if (item == ModItems.GREEN_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.GREEN);
        } else if (item == ModItems.CYAN_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.CYAN);
        } else if (item == ModItems.LIGHT_BLUE_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_BLUE);
        } else if (item == ModItems.BLUE_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.BLUE);
        } else if (item == ModItems.PURPLE_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.PURPLE);
        } else if (item == ModItems.MAGENTA_IRON_INFUSED_COLLAR.get()) {
            this.setCollar(CollarVariant.MAGENTA);
        } else {
            this.setCollar(CollarVariant.PINK);
        }

        this.setArmor(ArmorVariant.IRON_INFUSED);
        getAttribute(Attributes.ARMOR).setBaseValue(13.0);
        getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(4.0);
    }

    protected void applyDiamondCrustedCollar(Item item) {
        if (item == ModItems.WHITE_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.WHITE);
        } else if (item == ModItems.LIGHT_GRAY_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_GRAY);
        } else if (item == ModItems.GRAY_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.GRAY);
        } else if (item == ModItems.BLACK_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.BLACK);
        } else if (item == ModItems.BROWN_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.BROWN);
        } else if (item == ModItems.RED_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.RED);
        } else if (item == ModItems.ORANGE_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.ORANGE);
        } else if (item == ModItems.YELLOW_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.YELLOW);
        } else if (item == ModItems.LIME_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIME);
        } else if (item == ModItems.GREEN_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.GREEN);
        } else if (item == ModItems.CYAN_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.CYAN);
        } else if (item == ModItems.LIGHT_BLUE_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_BLUE);
        } else if (item == ModItems.BLUE_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.BLUE);
        } else if (item == ModItems.PURPLE_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.PURPLE);
        } else if (item == ModItems.MAGENTA_DIAMOND_CRUSTED_COLLAR.get()) {
            this.setCollar(CollarVariant.MAGENTA);
        } else {
            this.setCollar(CollarVariant.PINK);
        }

        this.setArmor(ArmorVariant.DIAMOND_CRUSTED);
        getAttribute(Attributes.ARMOR).setBaseValue(18.0);
        getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(8.0);
    }

    protected void applyNetheriteLacedCollar(Item item) {
        if (item == ModItems.WHITE_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.WHITE);
        } else if (item == ModItems.LIGHT_GRAY_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_GRAY);
        } else if (item == ModItems.GRAY_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.GRAY);
        } else if (item == ModItems.BLACK_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.BLACK);
        } else if (item == ModItems.BROWN_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.BROWN);
        } else if (item == ModItems.RED_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.RED);
        } else if (item == ModItems.ORANGE_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.ORANGE);
        } else if (item == ModItems.YELLOW_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.YELLOW);
        } else if (item == ModItems.LIME_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIME);
        } else if (item == ModItems.GREEN_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.GREEN);
        } else if (item == ModItems.CYAN_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.CYAN);
        } else if (item == ModItems.LIGHT_BLUE_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.LIGHT_BLUE);
        } else if (item == ModItems.BLUE_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.BLUE);
        } else if (item == ModItems.PURPLE_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.PURPLE);
        } else if (item == ModItems.MAGENTA_NETHERITE_LACED_COLLAR.get()) {
            this.setCollar(CollarVariant.MAGENTA);
        } else {
            this.setCollar(CollarVariant.PINK);
        }

        this.setArmor(ArmorVariant.NETHERITE_LACED);
        getAttribute(Attributes.ARMOR).setBaseValue(24.0);
        getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(12.0);
    }

    protected void removeCollar() {
        if (this.getCollar() == CollarVariant.WHITE) {
            spawnAtLocation(ModItems.WHITE_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_GRAY) {
            spawnAtLocation(ModItems.LIGHT_GRAY_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GRAY) {
            spawnAtLocation(ModItems.GRAY_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLACK) {
            spawnAtLocation(ModItems.BLACK_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BROWN) {
            spawnAtLocation(ModItems.BROWN_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.RED) {
            spawnAtLocation(ModItems.RED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.ORANGE) {
            spawnAtLocation(ModItems.ORANGE_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.YELLOW) {
            spawnAtLocation(ModItems.YELLOW_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIME) {
            spawnAtLocation(ModItems.LIME_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GREEN) {
            spawnAtLocation(ModItems.GREEN_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.CYAN) {
            spawnAtLocation(ModItems.CYAN_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_BLUE) {
            spawnAtLocation(ModItems.LIGHT_BLUE_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLUE) {
            spawnAtLocation(ModItems.BLUE_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.PURPLE) {
            spawnAtLocation(ModItems.PURPLE_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.MAGENTA) {
            spawnAtLocation(ModItems.MAGENTA_COLLAR.get());
        } else {
            spawnAtLocation(ModItems.PINK_COLLAR.get());
        }

        setCollar(CollarVariant.NONE);
    }

    protected void removeReinforcedCollar() {
        if (this.getCollar() == CollarVariant.WHITE) {
            spawnAtLocation(ModItems.WHITE_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_GRAY) {
            spawnAtLocation(ModItems.LIGHT_GRAY_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GRAY) {
            spawnAtLocation(ModItems.GRAY_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLACK) {
            spawnAtLocation(ModItems.BLACK_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BROWN) {
            spawnAtLocation(ModItems.BROWN_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.RED) {
            spawnAtLocation(ModItems.RED_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.ORANGE) {
            spawnAtLocation(ModItems.ORANGE_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.YELLOW) {
            spawnAtLocation(ModItems.YELLOW_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIME) {
            spawnAtLocation(ModItems.LIME_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GREEN) {
            spawnAtLocation(ModItems.GREEN_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.CYAN) {
            spawnAtLocation(ModItems.CYAN_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_BLUE) {
            spawnAtLocation(ModItems.LIGHT_BLUE_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLUE) {
            spawnAtLocation(ModItems.BLUE_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.PURPLE) {
            spawnAtLocation(ModItems.PURPLE_REINFORCED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.MAGENTA) {
            spawnAtLocation(ModItems.MAGENTA_REINFORCED_COLLAR.get());
        } else {
            spawnAtLocation(ModItems.PINK_REINFORCED_COLLAR.get());
        }

        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        getAttribute(Attributes.ARMOR).setBaseValue(0.0);
    }

    protected void removeGoldPlatedCollar() {
        if (this.getCollar() == CollarVariant.WHITE) {
            spawnAtLocation(ModItems.WHITE_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_GRAY) {
            spawnAtLocation(ModItems.LIGHT_GRAY_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GRAY) {
            spawnAtLocation(ModItems.GRAY_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLACK) {
            spawnAtLocation(ModItems.BLACK_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BROWN) {
            spawnAtLocation(ModItems.BROWN_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.RED) {
            spawnAtLocation(ModItems.RED_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.ORANGE) {
            spawnAtLocation(ModItems.ORANGE_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.YELLOW) {
            spawnAtLocation(ModItems.YELLOW_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIME) {
            spawnAtLocation(ModItems.LIME_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GREEN) {
            spawnAtLocation(ModItems.GREEN_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.CYAN) {
            spawnAtLocation(ModItems.CYAN_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_BLUE) {
            spawnAtLocation(ModItems.LIGHT_BLUE_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLUE) {
            spawnAtLocation(ModItems.BLUE_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.PURPLE) {
            spawnAtLocation(ModItems.PURPLE_GOLD_PLATED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.MAGENTA) {
            spawnAtLocation(ModItems.MAGENTA_GOLD_PLATED_COLLAR.get());
        } else {
            spawnAtLocation(ModItems.PINK_GOLD_PLATED_COLLAR.get());
        }

        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        getAttribute(Attributes.ARMOR).setBaseValue(0.0);
        getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(0.0);
    }

    protected void removeIronInfusedCollar() {
        if (this.getCollar() == CollarVariant.WHITE) {
            spawnAtLocation(ModItems.WHITE_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_GRAY) {
            spawnAtLocation(ModItems.LIGHT_GRAY_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GRAY) {
            spawnAtLocation(ModItems.GRAY_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLACK) {
            spawnAtLocation(ModItems.BLACK_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BROWN) {
            spawnAtLocation(ModItems.BROWN_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.RED) {
            spawnAtLocation(ModItems.RED_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.ORANGE) {
            spawnAtLocation(ModItems.ORANGE_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.YELLOW) {
            spawnAtLocation(ModItems.YELLOW_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIME) {
            spawnAtLocation(ModItems.LIME_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GREEN) {
            spawnAtLocation(ModItems.GREEN_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.CYAN) {
            spawnAtLocation(ModItems.CYAN_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_BLUE) {
            spawnAtLocation(ModItems.LIGHT_BLUE_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLUE) {
            spawnAtLocation(ModItems.BLUE_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.PURPLE) {
            spawnAtLocation(ModItems.PURPLE_IRON_INFUSED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.MAGENTA) {
            spawnAtLocation(ModItems.MAGENTA_IRON_INFUSED_COLLAR.get());
        } else {
            spawnAtLocation(ModItems.PINK_IRON_INFUSED_COLLAR.get());
        }

        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        getAttribute(Attributes.ARMOR).setBaseValue(0.0);
        getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(0.0);
    }

    protected void removeDiamondCrustedCollar() {
        if (this.getCollar() == CollarVariant.WHITE) {
            spawnAtLocation(ModItems.WHITE_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_GRAY) {
            spawnAtLocation(ModItems.LIGHT_GRAY_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GRAY) {
            spawnAtLocation(ModItems.GRAY_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLACK) {
            spawnAtLocation(ModItems.BLACK_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BROWN) {
            spawnAtLocation(ModItems.BROWN_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.RED) {
            spawnAtLocation(ModItems.RED_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.ORANGE) {
            spawnAtLocation(ModItems.ORANGE_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.YELLOW) {
            spawnAtLocation(ModItems.YELLOW_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIME) {
            spawnAtLocation(ModItems.LIME_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GREEN) {
            spawnAtLocation(ModItems.GREEN_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.CYAN) {
            spawnAtLocation(ModItems.CYAN_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_BLUE) {
            spawnAtLocation(ModItems.LIGHT_BLUE_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLUE) {
            spawnAtLocation(ModItems.BLUE_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.PURPLE) {
            spawnAtLocation(ModItems.PURPLE_DIAMOND_CRUSTED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.MAGENTA) {
            spawnAtLocation(ModItems.MAGENTA_DIAMOND_CRUSTED_COLLAR.get());
        } else {
            spawnAtLocation(ModItems.PINK_DIAMOND_CRUSTED_COLLAR.get());
        }

        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        getAttribute(Attributes.ARMOR).setBaseValue(0.0);
        getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(0.0);
    }

    protected void removeNetheriteLacedCollar() {
        if (this.getCollar() == CollarVariant.WHITE) {
            spawnAtLocation(ModItems.WHITE_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_GRAY) {
            spawnAtLocation(ModItems.LIGHT_GRAY_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GRAY) {
            spawnAtLocation(ModItems.GRAY_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLACK) {
            spawnAtLocation(ModItems.BLACK_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BROWN) {
            spawnAtLocation(ModItems.BROWN_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.RED) {
            spawnAtLocation(ModItems.RED_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.ORANGE) {
            spawnAtLocation(ModItems.ORANGE_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.YELLOW) {
            spawnAtLocation(ModItems.YELLOW_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIME) {
            spawnAtLocation(ModItems.LIME_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.GREEN) {
            spawnAtLocation(ModItems.GREEN_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.CYAN) {
            spawnAtLocation(ModItems.CYAN_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.LIGHT_BLUE) {
            spawnAtLocation(ModItems.LIGHT_BLUE_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.BLUE) {
            spawnAtLocation(ModItems.BLUE_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.PURPLE) {
            spawnAtLocation(ModItems.PURPLE_NETHERITE_LACED_COLLAR.get());
        } else if (this.getCollar() == CollarVariant.MAGENTA) {
            spawnAtLocation(ModItems.MAGENTA_NETHERITE_LACED_COLLAR.get());
        } else {
            spawnAtLocation(ModItems.PINK_NETHERITE_LACED_COLLAR.get());
        }

        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        getAttribute(Attributes.ARMOR).setBaseValue(0.0);
        getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(0.0);
    }

    /* NEUTRALMOB NECESSITIES */
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int pTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, pTime);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner) {
        if (!(pTarget instanceof Creeper) && !(pTarget instanceof Ghast)) {
            if (pTarget instanceof TamableAnimal) {
                if (pTarget instanceof Wolf || pTarget instanceof AbstractDog) {
                    TamableAnimal tamable = (TamableAnimal) pTarget;
                    return !(tamable.isTame() && tamable.getOwner() == pOwner);
                } else {
                    return !((TamableAnimal) pTarget).isTame();
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /* LOOT TABLES */
    @Override
    public ResourceKey<LootTable> getDefaultLootTable() {
        if (this.getCollar() == CollarVariant.NONE) {
            return this.getType().getDefaultLootTable();
        } else if (this.getArmor() == ArmorVariant.NONE) {
            return switch (this.getCollar()) {
                default -> ModBuiltInLootTables.WHITE_COLLAR;
                case LIGHT_GRAY -> ModBuiltInLootTables.LIGHT_GRAY_COLLAR;
                case GRAY -> ModBuiltInLootTables.GRAY_COLLAR;
                case BLACK -> ModBuiltInLootTables.BLACK_COLLAR;
                case BROWN -> ModBuiltInLootTables.BROWN_COLLAR;
                case RED -> ModBuiltInLootTables.RED_COLLAR;
                case ORANGE -> ModBuiltInLootTables.ORANGE_COLLAR;
                case YELLOW -> ModBuiltInLootTables.YELLOW_COLLAR;
                case LIME -> ModBuiltInLootTables.LIME_COLLAR;
                case GREEN -> ModBuiltInLootTables.GREEN_COLLAR;
                case CYAN -> ModBuiltInLootTables.CYAN_COLLAR;
                case LIGHT_BLUE -> ModBuiltInLootTables.LIGHT_BLUE_COLLAR;
                case BLUE -> ModBuiltInLootTables.BLUE_COLLAR;
                case PURPLE -> ModBuiltInLootTables.PURPLE_COLLAR;
                case MAGENTA -> ModBuiltInLootTables.MAGENTA_COLLAR;
                case PINK -> ModBuiltInLootTables.PINK_COLLAR;
            };
        } else if (this.getArmor() == ArmorVariant.REINFORCED) {
            return switch (this.getCollar()) {
                default -> ModBuiltInLootTables.WHITE_REINFORCED_COLLAR;
                case LIGHT_GRAY -> ModBuiltInLootTables.LIGHT_GRAY_REINFORCED_COLLAR;
                case GRAY -> ModBuiltInLootTables.GRAY_REINFORCED_COLLAR;
                case BLACK -> ModBuiltInLootTables.BLACK_REINFORCED_COLLAR;
                case BROWN -> ModBuiltInLootTables.BROWN_REINFORCED_COLLAR;
                case RED -> ModBuiltInLootTables.RED_REINFORCED_COLLAR;
                case ORANGE -> ModBuiltInLootTables.ORANGE_REINFORCED_COLLAR;
                case YELLOW -> ModBuiltInLootTables.YELLOW_REINFORCED_COLLAR;
                case LIME -> ModBuiltInLootTables.LIME_REINFORCED_COLLAR;
                case GREEN -> ModBuiltInLootTables.GREEN_REINFORCED_COLLAR;
                case CYAN -> ModBuiltInLootTables.CYAN_REINFORCED_COLLAR;
                case LIGHT_BLUE -> ModBuiltInLootTables.LIGHT_BLUE_REINFORCED_COLLAR;
                case BLUE -> ModBuiltInLootTables.BLUE_REINFORCED_COLLAR;
                case PURPLE -> ModBuiltInLootTables.PURPLE_REINFORCED_COLLAR;
                case MAGENTA -> ModBuiltInLootTables.MAGENTA_REINFORCED_COLLAR;
                case PINK -> ModBuiltInLootTables.PINK_REINFORCED_COLLAR;
            };
        } else if (this.getArmor() == ArmorVariant.GOLD_PLATED) {
            return switch (this.getCollar()) {
                default -> ModBuiltInLootTables.WHITE_GOLD_PLATED_COLLAR;
                case LIGHT_GRAY -> ModBuiltInLootTables.LIGHT_GRAY_GOLD_PLATED_COLLAR;
                case GRAY -> ModBuiltInLootTables.GRAY_GOLD_PLATED_COLLAR;
                case BLACK -> ModBuiltInLootTables.BLACK_GOLD_PLATED_COLLAR;
                case BROWN -> ModBuiltInLootTables.BROWN_GOLD_PLATED_COLLAR;
                case RED -> ModBuiltInLootTables.RED_GOLD_PLATED_COLLAR;
                case ORANGE -> ModBuiltInLootTables.ORANGE_GOLD_PLATED_COLLAR;
                case YELLOW -> ModBuiltInLootTables.YELLOW_GOLD_PLATED_COLLAR;
                case LIME -> ModBuiltInLootTables.LIME_GOLD_PLATED_COLLAR;
                case GREEN -> ModBuiltInLootTables.GREEN_GOLD_PLATED_COLLAR;
                case CYAN -> ModBuiltInLootTables.CYAN_GOLD_PLATED_COLLAR;
                case LIGHT_BLUE -> ModBuiltInLootTables.LIGHT_BLUE_GOLD_PLATED_COLLAR;
                case BLUE -> ModBuiltInLootTables.BLUE_GOLD_PLATED_COLLAR;
                case PURPLE -> ModBuiltInLootTables.PURPLE_GOLD_PLATED_COLLAR;
                case MAGENTA -> ModBuiltInLootTables.MAGENTA_GOLD_PLATED_COLLAR;
                case PINK -> ModBuiltInLootTables.PINK_GOLD_PLATED_COLLAR;
            };
        } else if (this.getArmor() == ArmorVariant.IRON_INFUSED) {
            return switch (this.getCollar()) {
                default -> ModBuiltInLootTables.WHITE_IRON_INFUSED_COLLAR;
                case LIGHT_GRAY -> ModBuiltInLootTables.LIGHT_GRAY_IRON_INFUSED_COLLAR;
                case GRAY -> ModBuiltInLootTables.GRAY_IRON_INFUSED_COLLAR;
                case BLACK -> ModBuiltInLootTables.BLACK_IRON_INFUSED_COLLAR;
                case BROWN -> ModBuiltInLootTables.BROWN_IRON_INFUSED_COLLAR;
                case RED -> ModBuiltInLootTables.RED_IRON_INFUSED_COLLAR;
                case ORANGE -> ModBuiltInLootTables.ORANGE_IRON_INFUSED_COLLAR;
                case YELLOW -> ModBuiltInLootTables.YELLOW_IRON_INFUSED_COLLAR;
                case LIME -> ModBuiltInLootTables.LIME_IRON_INFUSED_COLLAR;
                case GREEN -> ModBuiltInLootTables.GREEN_IRON_INFUSED_COLLAR;
                case CYAN -> ModBuiltInLootTables.CYAN_IRON_INFUSED_COLLAR;
                case LIGHT_BLUE -> ModBuiltInLootTables.LIGHT_BLUE_IRON_INFUSED_COLLAR;
                case BLUE -> ModBuiltInLootTables.BLUE_IRON_INFUSED_COLLAR;
                case PURPLE -> ModBuiltInLootTables.PURPLE_IRON_INFUSED_COLLAR;
                case MAGENTA -> ModBuiltInLootTables.MAGENTA_IRON_INFUSED_COLLAR;
                case PINK -> ModBuiltInLootTables.PINK_IRON_INFUSED_COLLAR;
            };
        } else if (this.getArmor() == ArmorVariant.DIAMOND_CRUSTED) {
            return switch (this.getCollar()) {
                default -> ModBuiltInLootTables.WHITE_DIAMOND_CRUSTED_COLLAR;
                case LIGHT_GRAY -> ModBuiltInLootTables.LIGHT_GRAY_DIAMOND_CRUSTED_COLLAR;
                case GRAY -> ModBuiltInLootTables.GRAY_DIAMOND_CRUSTED_COLLAR;
                case BLACK -> ModBuiltInLootTables.BLACK_DIAMOND_CRUSTED_COLLAR;
                case BROWN -> ModBuiltInLootTables.BROWN_DIAMOND_CRUSTED_COLLAR;
                case RED -> ModBuiltInLootTables.RED_DIAMOND_CRUSTED_COLLAR;
                case ORANGE -> ModBuiltInLootTables.ORANGE_DIAMOND_CRUSTED_COLLAR;
                case YELLOW -> ModBuiltInLootTables.YELLOW_DIAMOND_CRUSTED_COLLAR;
                case LIME -> ModBuiltInLootTables.LIME_DIAMOND_CRUSTED_COLLAR;
                case GREEN -> ModBuiltInLootTables.GREEN_DIAMOND_CRUSTED_COLLAR;
                case CYAN -> ModBuiltInLootTables.CYAN_DIAMOND_CRUSTED_COLLAR;
                case LIGHT_BLUE -> ModBuiltInLootTables.LIGHT_BLUE_DIAMOND_CRUSTED_COLLAR;
                case BLUE -> ModBuiltInLootTables.BLUE_DIAMOND_CRUSTED_COLLAR;
                case PURPLE -> ModBuiltInLootTables.PURPLE_DIAMOND_CRUSTED_COLLAR;
                case MAGENTA -> ModBuiltInLootTables.MAGENTA_DIAMOND_CRUSTED_COLLAR;
                case PINK -> ModBuiltInLootTables.PINK_DIAMOND_CRUSTED_COLLAR;
            };
        } else if (this.getArmor() == ArmorVariant.NETHERITE_LACED) {
            return switch (this.getCollar()) {
                default -> ModBuiltInLootTables.WHITE_NETHERITE_LACED_COLLAR;
                case LIGHT_GRAY -> ModBuiltInLootTables.LIGHT_GRAY_NETHERITE_LACED_COLLAR;
                case GRAY -> ModBuiltInLootTables.GRAY_NETHERITE_LACED_COLLAR;
                case BLACK -> ModBuiltInLootTables.BLACK_NETHERITE_LACED_COLLAR;
                case BROWN -> ModBuiltInLootTables.BROWN_NETHERITE_LACED_COLLAR;
                case RED -> ModBuiltInLootTables.RED_NETHERITE_LACED_COLLAR;
                case ORANGE -> ModBuiltInLootTables.ORANGE_NETHERITE_LACED_COLLAR;
                case YELLOW -> ModBuiltInLootTables.YELLOW_NETHERITE_LACED_COLLAR;
                case LIME -> ModBuiltInLootTables.LIME_NETHERITE_LACED_COLLAR;
                case GREEN -> ModBuiltInLootTables.GREEN_NETHERITE_LACED_COLLAR;
                case CYAN -> ModBuiltInLootTables.CYAN_NETHERITE_LACED_COLLAR;
                case LIGHT_BLUE -> ModBuiltInLootTables.LIGHT_BLUE_NETHERITE_LACED_COLLAR;
                case BLUE -> ModBuiltInLootTables.BLUE_NETHERITE_LACED_COLLAR;
                case PURPLE -> ModBuiltInLootTables.PURPLE_NETHERITE_LACED_COLLAR;
                case MAGENTA -> ModBuiltInLootTables.MAGENTA_NETHERITE_LACED_COLLAR;
                case PINK -> ModBuiltInLootTables.PINK_NETHERITE_LACED_COLLAR;
            };
        } else {
            return this.getType().getDefaultLootTable();
        }
    }

    /* SPAWN PLACEMENTS */
    public static boolean checkDogSpawnRules(EntityType<? extends AbstractDog> dog, LevelAccessor pLevel,
                                             MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pPos.getY() > pLevel.getSeaLevel() && isBrightEnoughToSpawn(pLevel, pPos) &&
                !isWater(pLevel, pPos);
    }

    private static boolean isWater(BlockGetter getter, BlockPos pos) {
        BlockPos pos2 = pos.above();
        return getter.getFluidState(pos).is(Fluids.WATER) || getter.getFluidState(pos2).is(Fluids.WATER);
    }
}
