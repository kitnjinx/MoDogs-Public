package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.SaintBernardVariant;
import com.kitnjinx.modogs.item.ModItems;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.pathfinder.BlockPathTypes;
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
    private static final EntityDataAccessor<Boolean> SHOW_BARREL =
            SynchedEntityData.defineId(SaintBernardEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.ZOMBIE_HORSE || entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER;
    };

    public SaintBernardEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
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

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
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

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.entityData.set(SHOW_BARREL, tag.getBoolean("Barrel"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("Barrel", this.getBarrel());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
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
        int determine = r.nextInt(19) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 7) {
            var = 0;
        } else if (determine < 13) {
            var = 1;
        } else if (determine < 17) {
            var = 2;
        } else if (determine < 19) {
            var = 3;
        } else {
            var = 4;
        }

        // assign chosen variant and finish the method
        SaintBernardVariant variant = SaintBernardVariant.byId(var);
        // Basic variant setter, equal chance
        // SaintBernardVariant variant = Util.getRandom(SaintBernardVariant.values(), this.random);
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

    public boolean getBarrel() {
        return this.entityData.get(SHOW_BARREL);
    }

    private void setBarrel(boolean show) {
        this.entityData.set(SHOW_BARREL, show);
    }

    private void determineBabyVariant(SaintBernardEntity baby, SaintBernardEntity otherParent) {
        boolean orangeCheck;
        boolean redCheck;

        if (this.getVariant() == SaintBernardVariant.RED && otherParent.getVariant() == SaintBernardVariant.YELLOW_BROWN) {
            orangeCheck = true;
        } else if (this.getVariant() == SaintBernardVariant.YELLOW_BROWN && otherParent.getVariant() == SaintBernardVariant.RED) {
            orangeCheck = true;
        } else {
            orangeCheck = false;
        }

        if (this.getVariant() == SaintBernardVariant.ORANGE) {
            if (otherParent.getVariant() == SaintBernardVariant.BROWN || otherParent.getVariant() == SaintBernardVariant.MAHOGANY) {
                redCheck = true;
            } else {
                redCheck = false;
            }
        } else if (otherParent.getVariant() == SaintBernardVariant.ORANGE) {
            if (this.getVariant() == SaintBernardVariant.BROWN || this.getVariant() == SaintBernardVariant.MAHOGANY) {
                redCheck = true;
            } else {
                redCheck = false;
            }
        } else {
            redCheck = false;
        }

        if (orangeCheck) {
            Random r = new Random();
            int determine = r.nextInt(5) + 1;

            if (determine == 5) {
                baby.setVariant(SaintBernardVariant.ORANGE);
            } else if (determine > 2) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(otherParent.getVariant());
            }
        } else if (redCheck) {
            Random r = new Random();
            int determine = r.nextInt(5) + 1;

            if (determine == 5) {
                baby.setVariant(SaintBernardVariant.RED);
            } else if (determine > 2) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(otherParent.getVariant());
            }
        } else {
            // Determines variant based on parents
            if (this.random.nextBoolean()) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(otherParent.getVariant());
            }
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
