package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CockerSpanielVariant;
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
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;
import java.util.function.Predicate;

public class CockerSpanielEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(CockerSpanielEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_BROWN =
            SynchedEntityData.defineId(CockerSpanielEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_BLACK =
            SynchedEntityData.defineId(CockerSpanielEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BLACK =
            SynchedEntityData.defineId(CockerSpanielEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_DILUTE =
            SynchedEntityData.defineId(CockerSpanielEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.CHICKEN;
    };

    public CockerSpanielEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        CockerSpanielEntity baby = ModEntityTypes.COCKER_SPANIEL.get().create(serverLevel);

        determineBabyVariant(baby, (CockerSpanielEntity) otherParent);

        // Determines if the baby is tamed based on parent
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
            state.getController().setAnimation(RawAnimation.begin().then("animation.cocker_spaniel.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.cocker_spaniel.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.cocker_spaniel.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.cocker_spaniel.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.cocker_spaniel.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller",
                0, this::predicate));
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

        Item itemForTaming = ModItems.CHICKEN_TREAT.get();

        if (item == itemForTaming && !isTame()) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
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
                if (this.getVariant() == CockerSpanielVariant.BUFF || this.getVariant() == CockerSpanielVariant.SILVER) {
                    if (this.isBlack()) {
                        message = Component.literal("This Cocker Spaniel demonstrates two recessive traits.");
                    } else if (this.carriesBlack()) {
                        message = Component.literal("This Cocker Spaniel demonstrates the dilution trait and carries the recessive black fur trait.");
                    } else {
                        message = Component.literal("This Cocker Spaniel demonstrates the dilution trait.");
                    }
                } else if (this.carriesDilute()) {
                    if (this.isBlack()) {
                        message= Component.literal("This Cocker Spaniel demonstrates the black fur trait and carries the recessive dilution trait.");
                    } else if (this.carriesBlack() && this.isBrown()) {
                        message = Component.literal("This Cocker Spaniel demonstrates a rare variant of red fur. They also carry two recessive traits.");
                    } else if (this.isBrown()) {
                        message = Component.literal("This Cocker Spaniel demonstrates a rare variant of red fur and carries the dilution trait.");
                    } else if (this.carriesBlack()) {
                        message = Component.literal("This Cocker Spaniel carries two recessive traits.");
                    } else {
                        message = Component.literal("This Cocker Spaniel carries the recessive dilution trait.");
                    }
                } else {
                    if (this.isBlack()) {
                        message = Component.literal("This Cocker Spaniel demonstrates the recessive black fur trait.");
                    } else if (this.carriesBlack() && this.isBrown()) {
                        message = Component.literal("This Cocker Spaniel demonstrates a rare variant of red fur and carries the recessive black fur trait.");
                    } else if (this.carriesBlack()) {
                        message = Component.literal("This Cocker Spaniel carries the recessive black fur trait.");
                    } else if (this.isBrown()) {
                        message = Component.literal("This Cocker Spaniel demonstrates a rare variant of red fur.");
                    } else {
                        message = Component.literal("This Cocker Spaniel doesn't have any recessive traits.");
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
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.entityData.set(IS_BROWN, tag.getBoolean("IsBrown"));
        this.entityData.set(IS_BLACK, tag.getBoolean("IsBlack"));
        this.entityData.set(CARRIES_BLACK, tag.getBoolean("CarriesBlack"));
        this.entityData.set(CARRIES_DILUTE, tag.getBoolean("CarriesDilute"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsBrown", this.isBrown());
        tag.putBoolean("IsBlack", this.isBlack());
        tag.putBoolean("CarriesBlack", this.carriesBlack());
        tag.putBoolean("CarriesDilute", this.carriesDilute());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_BROWN, false);
        this.entityData.define(IS_BLACK, false);
        this.entityData.define(CARRIES_BLACK, false);
        this.entityData.define(CARRIES_DILUTE, false);
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
        int black = r.nextInt(5) + 1;
        int determine = r.nextInt(8) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (black < 5) {
            setBlackStatus(carrier == 1, false);
            if (determine < 5) {
                var = 0; // RED
                setBrown(false);
                setDilute(carrier == 2);
            } else if (determine < 8) {
                var = 1; // BROWN
                setBrown(true);
                setDilute(carrier == 2);
            } else {
                var = 2; // BUFF
                setBrown(true);
                setDilute(true);
            }
        } else {
            setBlackStatus(true, true);
            setBrown(carrier == 1);
            if (determine < 7) {
                var = 3; // BLACK
                setDilute(carrier == 2);
            } else {
                var = 4; // SILVER
                setDilute(true);
            }
        }

        // assign chosen variant and finish the method
        CockerSpanielVariant variant = CockerSpanielVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public CockerSpanielVariant getVariant() {
        return CockerSpanielVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(CockerSpanielVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isBrown() {
        return this.entityData.get(IS_BROWN);
    }

    private void setBrown(boolean is) {
        this.entityData.set(IS_BROWN, is);
    }

    public boolean isBlack() {
        return this.entityData.get(IS_BLACK);
    }

    public boolean carriesBlack() {
        return this.entityData.get(CARRIES_BLACK);
    }

    private void setBlackStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_BLACK, carrier);
        this.entityData.set(IS_BLACK, is);
    }

    public boolean carriesDilute() {
        return this.entityData.get(CARRIES_DILUTE);
    }

    private void setDilute(boolean carrier) {
        this.entityData.set(CARRIES_DILUTE, carrier);
    }

    private void determineBabyVariant(CockerSpanielEntity baby, CockerSpanielEntity otherParent) {
        // determine if baby is black or red based
        if (this.isBlack() && otherParent.isBlack()) {
            // if both parents are black, baby will be black
            baby.setBlackStatus(true, true);
        } else if ((this.isBlack() && otherParent.carriesBlack()) || (this.carriesBlack() && otherParent.isBlack())) {
            // if one parent is black and the other is a carrier, baby has 50% chance to be black and 50% chance
            // to be a carrier
            baby.setBlackStatus(true, this.random.nextBoolean());
        } else if (this.isBlack() || otherParent.isBlack()) {
            // if only one parent is black, baby will be a carrier
            baby.setBlackStatus(true, false);
        } else if (this.carriesBlack() && otherParent.carriesBlack()) {
            // if both parents carry black, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be black
            int determine = this.random.nextInt(4) + 1;
            baby.setBlackStatus(determine > 1, determine == 4);
        } else if (this.carriesBlack() || otherParent.carriesBlack()) {
            // if only one parent carries black, baby has 50/50 chance to be a carrier
            baby.setBlackStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setBlackStatus(false, false);
        }

        // determine if baby has the brown variant or not
        if (this.isBrown() && otherParent.isBrown()) {
            // if both parents are brown, baby will be brown
            baby.setBrown(true);
        } else if (this.isBrown() || otherParent.isBrown()) {
            // if only one parent is brown, baby has 50/50 chance to be brown
            baby.setBrown(this.random.nextBoolean());
        } else {
            // if neither parent is brown, baby will not be brown
            baby.setBrown(false);
        }

        // determine if baby is dilute, a dilute carrier, or neither
        // parDilute booleans determine if the parents show dilute (dd)
        boolean parADilute = this.getVariant() == CockerSpanielVariant.BUFF || this.getVariant() == CockerSpanielVariant.SILVER;
        boolean parBDilute = otherParent.getVariant() == CockerSpanielVariant.BUFF || otherParent.getVariant() == CockerSpanielVariant.SILVER;
        boolean babyDilute;
        if (parADilute && parBDilute) {
            // if both parents are diluted, the baby will be diluted
            babyDilute = true;
            baby.setDilute(true);
        } else if ((parADilute && otherParent.carriesDilute()) || (parBDilute && this.carriesDilute())) {
            // if one parent is diluted and the other is a carrier, baby has 50% chance to be diluted and 50%
            // chance to carry dilute
            babyDilute = this.random.nextBoolean();
            baby.setDilute(true);
        } else if (parADilute || parBDilute) {
            // if only one parent is diluted, baby will be a carrier
            babyDilute = false;
            baby.setDilute(true);
        } else if (this.carriesDilute() && otherParent.carriesDilute()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be diluted
            int determine = this.random.nextInt(4) + 1;
            babyDilute = determine == 4;
            baby.setDilute(determine > 1);
        } else if (this.carriesDilute() || otherParent.carriesDilute()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            babyDilute = false;
            baby.setDilute(this.random.nextBoolean());
        } else {
            // if neither parent is a carrier, baby will not have dilute
            babyDilute = false;
            baby.setDilute(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (babyDilute && baby.isBlack()) {
            baby.setVariant(CockerSpanielVariant.SILVER);
        } else if (babyDilute) {
            baby.setVariant(CockerSpanielVariant.BUFF);
        } else if (baby.isBlack()) {
            baby.setVariant(CockerSpanielVariant.BLACK);
        } else if (baby.isBrown()) {
            baby.setVariant(CockerSpanielVariant.BROWN);
        } else {
            baby.setVariant(CockerSpanielVariant.RED);
        }
    }
}