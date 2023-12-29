package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.RottweilerVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
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

public class RottweilerEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(RottweilerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_TAN =
            SynchedEntityData.defineId(RottweilerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_TAN =
            SynchedEntityData.defineId(RottweilerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_MAHOGANY =
            SynchedEntityData.defineId(RottweilerEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
            return entitytype == EntityType.PIG || entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER || entitytype == EntityType.ZOMBIE_HORSE;
    };

    public RottweilerEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        RottweilerEntity baby = ModEntityTypes.ROTTWEILER.get().create(serverLevel);

        determineBabyVariant(baby, (RottweilerEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends RottweilerEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rottweiler.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rottweiler.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rottweiler.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rottweiler.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rottweiler.idle"));
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
                Component message;
                if (this.isTan() && this.isMahogany()) {
                    message = Component.literal("This Rottweiler demonstrates a recessive trait. They also have the alleles for a rare point color.");
                } else if (this.isTan()) {
                    message = Component.literal("This Rottweiler demonstrates a recessive trait.");
                } else if (this.carriesTan() && this.isMahogany()) {
                    message = Component.literal("This Rottweiler demonstrates a rare point color, and carries a recessive trait.");
                } else if (this.carriesTan()) {
                    message = Component.literal("This Rottweiler carries a recessive trait.");
                } else if (this.isMahogany()) {
                    message = Component.literal("This Rottweiler demonstrates a rare point color.");
                } else {
                    message = Component.literal("This Rottweiler doesn't have any recessive traits.");
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
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.entityData.set(IS_TAN, tag.getBoolean("IsTan"));
        this.entityData.set(CARRIES_TAN, tag.getBoolean("CarriesTan"));
        this.entityData.set(IS_MAHOGANY, tag.getBoolean("IsMahogany"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsTan", this.isTan());
        tag.putBoolean("CarriesTan", this.carriesTan());
        tag.putBoolean("IsMahogany", this.isMahogany());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_TAN, true);
        this.entityData.define(CARRIES_TAN, true);
        this.entityData.define(IS_MAHOGANY, false);
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
        int determine = r.nextInt(12) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 9) {
            var = 0; // TAN
            setTanStatus(true, true);
        } else {
            setTanStatus(this.random.nextBoolean(), false);
            if (determine < 12 || r.nextBoolean()) {
                var = 1; // RUST
            } else {
                var = 2; // MAHOGANY
            }
        }

        setMahogany(var == 2);

        // assign chosen variant and finish the method
        RottweilerVariant variant = RottweilerVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public RottweilerVariant getVariant() {
        return RottweilerVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(RottweilerVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isTan() {
        return this.entityData.get(IS_TAN);
    }

    public boolean carriesTan() {
        return this.entityData.get(CARRIES_TAN);
    }

    private void setTanStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_TAN, carrier);
        this.entityData.set(IS_TAN, is);
    }

    public boolean isMahogany() {
        return this.entityData.get(IS_MAHOGANY);
    }

    private void setMahogany(boolean is) {
        this.entityData.set(IS_MAHOGANY, is);
    }

    private void determineBabyVariant(RottweilerEntity baby, RottweilerEntity otherParent) {
        // determine if baby is tan or rust
        if (this.isTan() && otherParent.isTan()) {
            // if both parents are tan baby will be tan
            baby.setTanStatus(true, true);
        } else if ((this.isTan() && otherParent.carriesTan()) || (this.carriesTan() && otherParent.isTan())) {
            // if one parent is tan and one parent is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be tan
            baby.setTanStatus(true, this.random.nextBoolean());
        } else if (this.isTan() || otherParent.isTan()) {
            // if only one parent is tan, baby will be a carrier
            baby.setTanStatus(true, false);
        } else if (this.carriesTan() && otherParent.carriesTan()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier,
            // and 25% chance to be tan
            int determine = this.random.nextInt(4) + 1;
            baby.setTanStatus(determine > 1, determine == 4);
        } else if (this.carriesTan() || otherParent.carriesTan()) {
            // if only one parent is a carrier, baby has 50/50 chance to carry
            baby.setTanStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setTanStatus(false, false);
        }

        // determine if baby has the mahogany variant
        if (this.isMahogany() && otherParent.isMahogany()) {
            // if both parents have the variant, baby will have the variant
            baby.setMahogany(true);
        } else if (this.isMahogany() || otherParent.isMahogany()) {
            // if only one parent has mahogany, baby has 50/50 chance to have it
            baby.setMahogany(this.random.nextBoolean());
        } else {
            // if neither parent has mahogany, baby will not have mahogany
            baby.setMahogany(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isTan()) {
            baby.setVariant(RottweilerVariant.TAN);
        } else if (baby.isMahogany()) {
            baby.setVariant(RottweilerVariant.MAHOGANY);
        } else {
            baby.setVariant(RottweilerVariant.RUST);
        }
    }
}