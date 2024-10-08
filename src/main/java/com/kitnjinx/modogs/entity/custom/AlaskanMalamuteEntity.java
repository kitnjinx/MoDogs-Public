package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.AlaskanMalamuteVariant;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
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

public class AlaskanMalamuteEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(AlaskanMalamuteEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_RED =
            SynchedEntityData.defineId(AlaskanMalamuteEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_RED =
            SynchedEntityData.defineId(AlaskanMalamuteEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SOLID =
            SynchedEntityData.defineId(AlaskanMalamuteEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_SOLID =
            SynchedEntityData.defineId(AlaskanMalamuteEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_PALE =
            SynchedEntityData.defineId(AlaskanMalamuteEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SILVER =
            SynchedEntityData.defineId(AlaskanMalamuteEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COW;
    };

    public AlaskanMalamuteEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.ARMOR, 0.0f)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f).build();
    }

    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        AlaskanMalamuteEntity baby = ModEntityTypes.ALASKAN_MALAMUTE.get().create(serverLevel);

        determineBabyVariant(baby, (AlaskanMalamuteEntity) otherParent);

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
        RawAnimation sitting = RawAnimation.begin().thenLoop("animation.alaskan_malamute.sitting");
        RawAnimation angryWalk = RawAnimation.begin().thenLoop("animation.alaskan_malamute.angrywalk");
        RawAnimation walk = RawAnimation.begin().thenLoop("animation.alaskan_malamute.walk");
        RawAnimation angryIdle = RawAnimation.begin().thenLoop("animation.alaskan_malamute.angryidle");
        RawAnimation idle = RawAnimation.begin().thenLoop("animation.alaskan_malamute.idle");

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
        Item itemForTaming2 = ModItems.SALMON_TREAT.get();

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
                Component message = determineGeneTesterMessage();
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
        this.entityData.set(IS_RED, tag.getBoolean("IsRed"));
        this.entityData.set(CARRIES_RED, tag.getBoolean("RedCarrier"));
        this.entityData.set(IS_SOLID, tag.getBoolean("IsSolid"));
        this.entityData.set(CARRIES_SOLID, tag.getBoolean("SolidCarrier"));
        this.entityData.set(IS_PALE, tag.getBoolean("IsPale"));
        this.entityData.set(IS_SILVER, tag.getBoolean("IsSilver"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsRed", this.isRed());
        tag.putBoolean("RedCarrier", this.isRedCarrier());
        tag.putBoolean("IsSolid", this.isSolid());
        tag.putBoolean("SolidCarrier", this.isSolidCarrier());
        tag.putBoolean("IsPale", this.isPale());
        tag.putBoolean("IsSilver", this.isSilver());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(IS_RED, false);
        builder.define(CARRIES_RED, false);
        builder.define(IS_SOLID, false);
        builder.define(CARRIES_SOLID, false);
        builder.define(IS_PALE, false);
        builder.define(IS_SILVER, false);
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
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(7D);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
    }

    /* VARIANTS */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(16);
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (getRandom().nextBoolean()) {
            var = 0; // GRAY
            setRedStatus(carrier == 1, false);
            setSolidStatus(carrier == 2, false);
        } else {
            if (determine < 5) {
                var = 1; // SABLE
                setRedStatus(true, true);
                setSolidStatus(carrier < 3, false);
            } else if (determine < 10) {
                var = 2; // SEAL
                setRedStatus(true, true);
                setSolidStatus(carrier == 1, false);
            } else if (determine < 13) {
                var = 3; // BLACK
                setRedStatus(carrier < 3, false);
                setSolidStatus(true, true);
            } else if (determine < 15) {
                var = 4; // RED
                setRedStatus(true, true);
                setSolidStatus(true, true);
            } else {
                var = 5; // SILVER
                setRedStatus(carrier < 3, false);
                setSolidStatus(true, true);
            }
        }

        if (var == 2) {
            setPale(true);
            setSilver(false);
        } else if (var == 5) {
            setPale(false);
            setSilver(true);
        } else {
            setPale(false);
            setSilver(false);
        }

        // assign chosen variant and finish the method
        AlaskanMalamuteVariant variant = AlaskanMalamuteVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group);
    }

    public AlaskanMalamuteVariant getVariant() {
        return AlaskanMalamuteVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(AlaskanMalamuteVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isRed() {
        return this.entityData.get(IS_RED);
    }

    public boolean isRedCarrier() {
        return this.entityData.get(CARRIES_RED);
    }

    private void setRedStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_RED, carrier);
        this.entityData.set(IS_RED, is);
    }

    public boolean isSolid() {
        return this.entityData.get(IS_SOLID);
    }

    public boolean isSolidCarrier() {
        return this.entityData.get(CARRIES_SOLID);
    }

    private void setSolidStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_SOLID, carrier);
        this.entityData.set(IS_SOLID, is);
    }

    public boolean isPale() {
        return this.entityData.get(IS_PALE);
    }

    private void setPale(boolean is) {
        this.entityData.set(IS_PALE, is);
    }

    public boolean isSilver() {
        return this.entityData.get(IS_SILVER);
    }

    private void setSilver(boolean is) {
        this.entityData.set(IS_SILVER, is);
    }

    private void determineBabyVariant(AlaskanMalamuteEntity baby, AlaskanMalamuteEntity otherParent) {
        // determine if baby is red or black
        if (this.isRed() && otherParent.isRed()) {
            // if both parents are red, baby will be red
            baby.setRedStatus(true, true);
        } else if ((this.isRed() && otherParent.isRedCarrier()) || (this.isRedCarrier() && otherParent.isRed())) {
            // if one parent is red and one parent is a carrier, baby will have 50% chance to be red and 50%
            // chance to be a carrier
            baby.setRedStatus(true, this.random.nextBoolean());
        } else if (this.isRed() || otherParent.isRed()) {
            // if one parent is red and the other is not a carrier, baby will be a carrier
            baby.setRedStatus(true, false);
        } else if (this.isRedCarrier() && otherParent.isRedCarrier()) {
            // if both parents are carriers, baby will have 25% chance not to carry, 50% chance to be a carrier,
            // and 25% chance to be red
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setRedStatus(false, false);
            } else {
                baby.setRedStatus(true, determine == 4);
            }
        } else if (this.isRedCarrier() || otherParent.isRedCarrier()) {
            // if one parent is a carrier, baby will have 50/50 chance to be a carrier
            baby.setRedStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setRedStatus(false, false);
        }

        // determine if baby is solid-colored or has the saddle marking
        if (this.isSolid() && otherParent.isSolid()) {
            // if both parents are solid, baby will be solid
            baby.setSolidStatus(true, true);
        } else if ((this.isSolid() && otherParent.isSolidCarrier()) ||
                (this.isSolidCarrier() && otherParent.isSolid())) {
            // if one parent is solid and one parent is a carrier, baby will have 50% chance to solid red and 50%
            // chance to be a carrier
            baby.setSolidStatus(true, this.random.nextBoolean());
        } else if (this.isSolid() || otherParent.isSolid()) {
            // if one parent is solid and the other is not a carrier, baby will be a carrier
            baby.setSolidStatus(true, false);
        } else if (this.isSolidCarrier() && otherParent.isSolidCarrier()) {
            // if both parents are carriers, baby will have 25% chance not to carry, 50% chance to be a carrier,
            // and 25% chance to be solid
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setSolidStatus(false, false);
            } else {
                baby.setSolidStatus(true, determine == 4);
            }
        } else if (this.isSolidCarrier() || otherParent.isSolidCarrier()) {
            // if one parent is a carrier, baby will have 50/50 chance to be a carrier
            baby.setSolidStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setSolidStatus(false, false);
        }

        // determine if baby has pale variant
        if (this.isPale() && otherParent.isPale()) {
            // if both parents are pale, baby will be pale
            baby.setPale(true);
        } else if (this.isPale() || otherParent.isPale()) {
            // if one parent is pale, baby has 50/50 chance to be pale
            baby.setPale(this.random.nextBoolean());
        } else {
            // if neither parent is pale, baby will not be pale
            baby.setPale(false);
        }

        // determine if baby has silver variant
        if (this.isSilver() && otherParent.isSilver()) {
            // if both parents are silver, baby will be silver
            baby.setSilver(true);
        } else if (this.isSilver() || otherParent.isSilver()) {
            // if one parent is silver, baby has 50/50 chance to be silver
            baby.setSilver(this.random.nextBoolean());
        } else {
            // if neither parent is silver, baby will not be silver
            baby.setSilver(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isRed() && baby.isSolid()) {
            baby.setVariant(AlaskanMalamuteVariant.RED);
        } else if (baby.isRed() && baby.isPale()) {
            baby.setVariant(AlaskanMalamuteVariant.SEAL);
        } else if (baby.isRed()) {
            baby.setVariant(AlaskanMalamuteVariant.SABLE);
        } else if (baby.isSolid() && isSilver()) {
            baby.setVariant(AlaskanMalamuteVariant.SILVER);
        } else if (baby.isSolid()) {
            baby.setVariant(AlaskanMalamuteVariant.BLACK);
        } else {
            baby.setVariant(AlaskanMalamuteVariant.GRAY);
        }
    }

    private Component determineGeneTesterMessage() {
        Component message;
        if (this.isRed() && this.isSolid()) {
            if (this.isSilver()) {
                message = Component.literal("This Alaskan Malamute demonstrates two recessive traits. They also have the alleles for silver fur.");
            } else {
                message = Component.literal("This Alaskan Malamute demonstrates two recessive traits.");
            }
        } else if (this.isRed() && this.isPale()) {
            if (this.isSolidCarrier() && this.isSilver()) {
                message = Component.literal("This Alaskan Malamute demonstrates the rare pale variant of the recessive red fur trait. They also carry the solid pattern and have alleles for silver fur.");
            } else if (this.isSolidCarrier()) {
                message = Component.literal("This Alaskan Malamute demonstrates the rare pale variant of the recessive red fur trait. They also carry the solid pattern trait.");
            } else if (this.isSilver()) {
                message = Component.literal("This Alaskan Malamute demonstrates the rare pale variant of the recessive red fur trait. They also have the alleles for silver fur.");
            } else {
                message = Component.literal("This Alaskan Malamute demonstrates the rare pale variant of the recessive red fur trait.");
            }
        } else if (this.isRed()) {
            if (this.isSolidCarrier() && this.isSilver()) {
                message = Component.literal("This Alaskan Malamute demonstrates the recessive red fur trait. They also carry the solid pattern trait and have the alleles for silver fur.");
            } else if (this.isSolidCarrier()) {
                message = Component.literal("This Alaskan Malamute demonstrates the recessive red fur trait. They also carry the solid pattern trait.");
            } else if (this.isSilver()) {
                message = Component.literal("This Alaskan Malamute demonstrates the recessive red fur trait. They also have the alleles for silver fur.");
            } else {
                message = Component.literal("This Alaskan Malamute demonstrates the recessive red fur trait.");
            }
        } else if (this.isSolid() && this.isSilver()) {
            if (this.isRedCarrier()) {
                message = Component.literal("This Alaskan Malamute demonstrates the rare silver variant of the recessive solid pattern trait. They also carry the red fur trait.");
            } else {
                message = Component.literal("This Alaskan Malamute demonstrates the rare silver variant of the recessive solid pattern trait.");
            }
        } else if (this.isSolid()) {
            if (this.isRedCarrier()) {
                message = Component.literal("This Alaskan Malamute demonstrates the recessive solid pattern trait. They also carry the red fur trait.");
            } else {
                message = Component.literal("This Alaskan Malamute demonstrates the recessive solid pattern trait.");
            }
        } else {
            if (this.isRedCarrier() && this.isSolidCarrier()) {
                if (this.isSilver()) {
                    message = Component.literal("This Alaskan Malamute carries two recessive traits. They also have the alleles for silver fur.");
                } else {
                    message = Component.literal("This Alaskan Malamute carries two recessive traits.");
                }
            } else if (this.isRedCarrier()) {
                if (this.isSilver()) {
                    message = Component.literal("This Alaskan Malamute carries the red fur trait. They also have the alleles for silver fur.");
                } else {
                    message = Component.literal("This Alaskan Malamute carries the red fur trait.");
                }
            } else if (this.isSolidCarrier()) {
                if (this.isSilver()) {
                    message = Component.literal("This Alaskan Malamute carries the solid pattern trait. They also have the alleles for silver fur.");
                } else {
                    message = Component.literal("This Alaskan Malamute carries the solid pattern trait.");
                }
            } else {
                if  (this.isSilver()) {
                    message = Component.literal("This Alaskan Malamute has the alleles for silver fur.");
                } else {
                    message = Component.literal("This Alaskan Malamute doesn't have any recessive traits.");
                }
            }
        }

        return message;
    }
}
