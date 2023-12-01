package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.SaintBernardVariant;
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
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

public class SaintBernardEntity extends AbstractDog {

    // handles coat variant & collar barrel
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(SaintBernardEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHADE =
            SynchedEntityData.defineId(SaintBernardEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_GOLDEN =
            SynchedEntityData.defineId(SaintBernardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_GOLDEN =
            SynchedEntityData.defineId(SaintBernardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOW_BARREL =
            SynchedEntityData.defineId(SaintBernardEntity.class, EntityDataSerializers.BOOLEAN);

    // SHADE explanation: how rich/true the color is. 0 = Brown, 1 = Red, 2 = Mahogany
    // If dog's IS_GOLDEN is true, then Shade: 0/1 = Orange, 2 = Yellow_Brown

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.ZOMBIE_HORSE || entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER;
    };

    public SaintBernardEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 22.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.ARMOR, 2.0f)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f).build();
    }

    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Zombie.class, true, PREY_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, ZombieVillager.class, true, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        SaintBernardEntity baby = ModEntityTypes.SAINT_BERNARD.get().create(serverLevel);

        determineBabyVariant(baby, (SaintBernardEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);
        baby.setBarrel(false);
        
        return baby;
    }

    private <E extends SaintBernardEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.saint_bernard.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.saint_bernard.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.saint_bernard.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.saint_bernard.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.saint_bernard.idle"));
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

        if (item == Items.BARREL && isTame() && this.isOwnedBy(player) && this.getCollar() != CollarVariant.NONE) {
            setBarrel(true);

            return InteractionResult.SUCCESS;
        }

        if (item == ModItems.GENE_TESTER.get()) {
            if (this.level.isClientSide) {
                TextComponent message;
                if (this.isGolden() && this.getShade() == 2) {
                    message = new TextComponent("This Saint Bernard demonstrates a recessive trait and \"true\" coloration.");
                } else if (this.isGolden() && this.getShade() == 1) {
                    message = new TextComponent("This Saint Bernard demonstrates a recessive trait. They have the potential to breed for \"true\" coloration.");
                } else if (this.isGolden()) {
                    message = new TextComponent("This Saint Bernard demonstrates a recessive trait and \"weak\" coloration.");
                } else if (this.isGoldenCarrier() && this.getShade() == 2) {
                    message = new TextComponent("This Saint Bernard demonstrates \"true\" coloration and carries a recessive trait.");
                } else if (this.isGoldenCarrier() && this.getShade() == 1) {
                    message = new TextComponent("This Saint Bernard demonstrates muddled coloration and carries a recessive trait.");
                } else if (this.isGoldenCarrier()) {
                    message = new TextComponent("This Saint Bernard carries a recessive trait. They have \"weak\" coloration.");
                } else if (this.getShade() == 2) {
                    message = new TextComponent("This Saint Bernard has \"true\" coloration.");
                } else if (this.getShade() == 1) {
                    message = new TextComponent("This Saint Bernard has muddled coloration.");
                } else {
                    message = new TextComponent("This Saint Bernard has \"weak\" coloration.");
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
        this.entityData.set(SHADE, tag.getInt("Shade"));
        this.entityData.set(IS_GOLDEN, tag.getBoolean("IsGolden"));
        this.entityData.set(CARRIES_GOLDEN, tag.getBoolean("GoldenCarrier"));
        this.entityData.set(SHOW_BARREL, tag.getBoolean("Barrel"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("Shade", this.getShade());
        tag.putBoolean("IsGolden", this.isGolden());
        tag.putBoolean("GoldenCarrier", this.isGoldenCarrier());
        tag.putBoolean("Barrel", this.getBarrel());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(SHADE, 0);
        this.entityData.define(IS_GOLDEN, false);
        this.entityData.define(CARRIES_GOLDEN, false);
        this.entityData.define(SHOW_BARREL, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
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
        int determine = r.nextInt(17) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 7) {
            var = 0; // BROWN
            setShade(0);
            setGoldenStatus(carrier == 1, false);
        } else if (determine < 11) {
            var = 1; // RED
            setShade(1);
            setGoldenStatus(carrier == 1, false);
        } else if (determine < 14) {
            var = 2; // ORANGE
            setGoldenStatus(true, true);
            if (r.nextInt(5) + 1 < 4) {
                setShade(0);
            } else {
                setShade(1);
            }
        } else if (determine < 16) {
            var = 3; // YELLOW_BROWN
            setShade(2);
            setGoldenStatus(true, true);
        } else {
            var = 4; // MAHOGANY
            setShade(2);
            setGoldenStatus(carrier == 1, false);
        }

        // assign chosen variant and finish the method
        SaintBernardVariant variant = SaintBernardVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        setBarrel(false);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public SaintBernardVariant getVariant() {
        return SaintBernardVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(SaintBernardVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public int getShade() {
        return this.entityData.get(SHADE);
    }

    private void setShade(int shade) {
        this.entityData.set(SHADE, shade);
    }

    public boolean isGolden() {
        return this.entityData.get(IS_GOLDEN);
    }

    public boolean isGoldenCarrier() {
        return this.entityData.get(CARRIES_GOLDEN);
    }

    private void setGoldenStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_GOLDEN, carrier);
        this.entityData.set(IS_GOLDEN, is);
    }

    public boolean getBarrel() {
        return this.entityData.get(SHOW_BARREL);
    }

    private void setBarrel(boolean show) {
        this.entityData.set(SHOW_BARREL, show);
    }

    private void determineBabyVariant(SaintBernardEntity baby, SaintBernardEntity otherParent) {
        // determine baby's shade
        if (this.getShade() == 1 && otherParent.getShade() == 1) {
            // if both parents are Shade 1, baby has 25% chance to be Shade 0, 50% chance to be 1, and 25% chance
            // to be 2
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setShade(0);
            } else if (determine < 4) {
                baby.setShade(1);
            } else {
                baby.setShade(2);
            }
        } else if (this.getShade() == otherParent.getShade()) {
            // if both parents are Shade 0 or both are 2, baby will match them
            baby.setShade(this.getShade());
        } else if ((this.getShade() == 0 && otherParent.getShade() == 2) ||
                (this.getShade() == 2 && otherParent.getShade() == 0)) {
            // if one parent is Shade 0 and one is Shade 2, baby will be Shade 1
            baby.setShade(1);
        } else {
            // if one parent is Shade 1 and the other parent is Shade 0 or 2, baby will match one of the parents
            if (this.random.nextBoolean()) {
                baby.setShade(this.getShade());
            } else {
                baby.setShade(otherParent.getShade());
            }
        }

        // determine baby's golden status
        if (this.isGolden() && otherParent.isGolden()) {
            // if both parents are golden, baby will be golden
            baby.setGoldenStatus(true, true);
        } else if ((this.isGolden() && otherParent.isGoldenCarrier()) ||
                (this.isGoldenCarrier() && otherParent.isGolden())) {
            // if one parent is golden and the other is a carrier, baby has 50% chance to be a carrier and
            // 50% chance to be golden
            baby.setGoldenStatus(true, this.random.nextBoolean());
        } else if (this.isGolden() || otherParent.isGolden()) {
            // if one parent is golden and the other is not a carrier, baby will be a carrier
            baby.setGoldenStatus(true, false);
        } else if (this.isGoldenCarrier() && otherParent.isGoldenCarrier()) {
            // if both parents are golden carriers, baby has 25% chance to not be a carrier, 50% chance to be
            // a carrier, and 25% chance to be golden
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setGoldenStatus(false, false);
            } else {
                baby.setGoldenStatus(true, determine == 4);
            }
        } else if (this.isGoldenCarrier() || otherParent.isGoldenCarrier()) {
            // if only one parent is a carrier, baby has 50% chance to be a carrier
            baby.setGoldenStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setGoldenStatus(false, false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isGolden() && baby.getShade() == 2) {
            baby.setVariant(SaintBernardVariant.YELLOW_BROWN);
        } else if (baby.isGolden()) {
            baby.setVariant(SaintBernardVariant.ORANGE);
        } else if (baby.getShade() == 2) {
            baby.setVariant(SaintBernardVariant.MAHOGANY);
        } else if (baby.getShade() == 1) {
            baby.setVariant(SaintBernardVariant.RED);
        } else {
            baby.setVariant(SaintBernardVariant.BROWN);
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
        setBarrel(false);
    }

    @Override
    protected void removeReinforcedCollar() {
        super.removeReinforcedCollar();
        getAttribute(Attributes.ARMOR).setBaseValue(2.0);
        setBarrel(false);
    }

    @Override
    protected void removeGoldPlatedCollar() {
        super.removeGoldPlatedCollar();
        getAttribute(Attributes.ARMOR).setBaseValue(2.0);
        setBarrel(false);
    }

    @Override
    protected void removeIronInfusedCollar() {
        super.removeIronInfusedCollar();
        getAttribute(Attributes.ARMOR).setBaseValue(2.0);
        setBarrel(false);
    }

    @Override
    protected void removeDiamondCrustedCollar() {
        super.removeDiamondCrustedCollar();
        getAttribute(Attributes.ARMOR).setBaseValue(2.0);
        setBarrel(false);
    }

    @Override
    protected void removeNetheriteLacedCollar() {
        super.removeNetheriteLacedCollar();
        getAttribute(Attributes.ARMOR).setBaseValue(2.0);
        setBarrel(false);
    }
}
