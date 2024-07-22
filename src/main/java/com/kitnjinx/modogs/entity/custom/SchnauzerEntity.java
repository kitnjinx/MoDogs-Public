package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.SchnauzerVariant;
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

public class SchnauzerEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(SchnauzerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_DILUTE =
            SynchedEntityData.defineId(SchnauzerEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COW || entitytype == EntityType.SHEEP || entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER || entitytype == EntityType.ZOMBIE_HORSE;
    };

    public SchnauzerEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        SchnauzerEntity baby = ModEntityTypes.SCHNAUZER.get().create(serverLevel);

        determineBabyVariant(baby, (SchnauzerEntity) otherParent);

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
        return 0.2F;
    }

    /* Tamable */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = ModItems.BEEF_TREAT.get();
        Item itemForTaming2 = ModItems.MUTTON_TREAT.get();

        if ((item == itemForTaming || item == itemForTaming2) && !isTame()) {
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
                if (this.getVariant() == SchnauzerVariant.PEPPER_SALT) {
                    message = Component.literal("This Schnauzer demonstrates a recessive trait.");
                } else if (this.getCarrier()) {
                    message = Component.literal("This Schnauzer carries a recessive trait.");
                } else {
                    message = Component.literal("This Schnauzer doesn't have any recessive traits.");
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
        this.entityData.set(CARRIES_DILUTE, tag.getBoolean("Carrier"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("Carrier", this.getCarrier());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(CARRIES_DILUTE, false);
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
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4f);
    }

    /* VARIANTS */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(4) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine != 4) {
            var = 0; // BLACK
        } else {
            var = 1; // PEPPER_SALT
        }

        setCarrier(var == 1 || carrier == 4);

        // assign chosen variant and finish the method
        SchnauzerVariant variant = SchnauzerVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    public SchnauzerVariant getVariant() {
        return SchnauzerVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(SchnauzerVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean getCarrier() {
        return this.entityData.get(CARRIES_DILUTE);
    }

    private void setCarrier(boolean status) {
        this.entityData.set(CARRIES_DILUTE, status);
    }

    private void determineBabyVariant(SchnauzerEntity baby, SchnauzerEntity otherParent) {
        // determine if baby is diluted, a carrier, or black
        if (this.getTypeVariant() == 1 && otherParent.getTypeVariant() == 1) {
            // if both parents are diluted, baby will be diluted
            baby.setCarrier(true);
            baby.setVariant(SchnauzerVariant.PEPPER_SALT);
        } else if ((this.getTypeVariant() == 1 && otherParent.getCarrier()) ||
                (this.getCarrier() && otherParent.getTypeVariant() == 1)) {
            // if one parent is diluted and the other is a carrier, baby has 50% chance to be diluted and 50%
            // chance to be a carrier
            baby.setCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(SchnauzerVariant.PEPPER_SALT);
            } else {
                baby.setVariant(SchnauzerVariant.BLACK);
            }
        } else if (this.getTypeVariant() == 1 || otherParent.getTypeVariant() == 1) {
            // if only one parent is diluted, baby will be a carrier
            baby.setCarrier(true);
            baby.setVariant(SchnauzerVariant.BLACK);
        } else if (this.getCarrier() && otherParent.getCarrier()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be diluted
            int determine = this.random.nextInt(4) + 1;
            baby.setCarrier(determine > 1);
            if (determine == 4) {
                baby.setVariant(SchnauzerVariant.PEPPER_SALT);
            } else {
                baby.setVariant(SchnauzerVariant.BLACK);
            }
        } else if (this.getCarrier() || otherParent.getCarrier()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setCarrier(this.random.nextBoolean());
            baby.setVariant(SchnauzerVariant.BLACK);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setCarrier(false);
            baby.setVariant(SchnauzerVariant.BLACK);
        }
    }
}