package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.MiniSchnauzerVariant;
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
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Random;
import java.util.function.Predicate;

public class MiniSchnauzerEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(MiniSchnauzerEntity.class, EntityDataSerializers.INT);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT || entitytype == EntityType.ZOMBIE_VILLAGER;
    };

    public MiniSchnauzerEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
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
        MiniSchnauzerEntity baby = ModEntityTypes.MINI_SCHNAUZER.get().create(serverLevel);

        determineBabyVariant(baby, (MiniSchnauzerEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true, true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        RawAnimation sitting = RawAnimation.begin().thenLoop("animation.schnauzer.sitting");
        RawAnimation angryWalk = RawAnimation.begin().thenLoop("animation.schnauzer.angrywalk");
        RawAnimation walk = RawAnimation.begin().thenLoop("animation.schnauzer.walk");
        RawAnimation angryIdle = RawAnimation.begin().thenLoop("animation.schnauzer.angryidle");
        RawAnimation idle = RawAnimation.begin().thenLoop("animation.schnauzer.idle");

        controllers.add(
                new AnimationController<>(this, 10, state ->
                        state.setAndContinue(this.isSitting() ? sitting :
                                (this.isAngry() || this.isAggressive() && state.isMoving()) ? angryWalk :
                                        state.isMoving() ? walk :(this.isAngry() || this.isAggressive()) ?
                                                angryIdle : idle))
        );
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

        Item itemForTaming = ModItems.RABBIT_TREAT.get();

        if (item == itemForTaming && !isTame()) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !EventHooks.onAnimalTame(this, player)) {
                    if (!this.level().isClientSide) {
                        super.tame(player);
                        this.navigation.recomputePath();
                        this.setTarget(null);
                        this.level().broadcastEntityEvent(this, (byte)7);
                        setSitting(true);
                        this.setHealth(this.getMaxHealth());
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        if (item == ModItems.GENE_TESTER.get()) {
            if (this.level().isClientSide) {
                Component message;
                if (this.getVariant() == MiniSchnauzerVariant.PEPPER_SALT) {
                    message = Component.literal("This Miniature Schnauzer demonstrates a recessive trait.");
                } else if (this.getVariant() == MiniSchnauzerVariant.BLACK_SILVER) {
                    message = Component.literal("This Miniature Schnauzer demonstrates an incompletely dominant trait.");
                } else {
                    message = Component.literal("This Miniature Schnauzer doesn't have any recessive traits.");
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
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
    }

    @Override
    public void setTame (boolean tamed, boolean applyTamingSideEffects) {
        super.setTame(tamed, applyTamingSideEffects);
        if (applyTamingSideEffects) {
            this.applyTamingSideEffects();
        }
    }

    @Override
    protected void applyTamingSideEffects() {
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(26.0);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
        getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3D);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
    }

    /* VARIANTS */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(9) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 6) {
            var = 0; // PEPPER_SALT
        } else if (determine < 9){
            var = 1; // SILVER_BLACK
        } else {
            var = 2; // BLACK
        }

        // assign chosen variant and finish the method
        MiniSchnauzerVariant variant = MiniSchnauzerVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    public MiniSchnauzerVariant getVariant() {
        return MiniSchnauzerVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(MiniSchnauzerVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    private void determineBabyVariant(MiniSchnauzerEntity baby, MiniSchnauzerEntity otherParent) {
        // determine if baby is highly diluted (pepper & salt), lightly diluted (black & silver), or
        // not diluted (black)
        if (this.getTypeVariant() == 0 && otherParent.getTypeVariant() == 0) {
            // if both parents are highly diluted, baby will be highly diluted
            baby.setVariant(MiniSchnauzerVariant.PEPPER_SALT);
        } else if ((this.getTypeVariant() == 0 && otherParent.getTypeVariant() == 1) ||
                (this.getTypeVariant() == 1 && otherParent.getTypeVariant() == 0)) {
            // if one parent is highly diluted and the other is lightly diluted, baby has 50% chance to be
            // highly diluted and 50% chance to be lowly diluted
            if (this.random.nextBoolean()) {
                baby.setVariant(MiniSchnauzerVariant.PEPPER_SALT);
            } else {
                baby.setVariant(MiniSchnauzerVariant.BLACK_SILVER);
            }
        } else if (this.getTypeVariant() == 0 || otherParent.getTypeVariant() == 0) {
            // if only one parent is highly diluted, baby will be lightly diluted
            baby.setVariant(MiniSchnauzerVariant.BLACK_SILVER);
        } else if (this.getTypeVariant() == 1 && otherParent.getTypeVariant() == 1) {
            // if both parents are lightly diluted, baby has 25% chance to be black, 50% chance to be lightly
            // diluted, and 25% chance to be highly diluted
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setVariant(MiniSchnauzerVariant.BLACK);
            } else if (determine < 4) {
                baby.setVariant(MiniSchnauzerVariant.BLACK_SILVER);
            } else {
                baby.setVariant(MiniSchnauzerVariant.PEPPER_SALT);
            }
        } else if (this.getTypeVariant() == 1 || otherParent.getTypeVariant() == 1) {
            // if only one parent is lightly diluted, baby has 50/50 chance to be lightly diluted
            if (this.random.nextBoolean()) {
                baby.setVariant(MiniSchnauzerVariant.BLACK_SILVER);
            } else {
                baby.setVariant(MiniSchnauzerVariant.BLACK);
            }
        } else {
            // if neither parent is diluted, baby will not be diluted
            baby.setVariant(MiniSchnauzerVariant.BLACK);
        }
    }
}