package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.RussellTerrierVariant;
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

public class RussellTerrierEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(RussellTerrierEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_TAN =
            SynchedEntityData.defineId(RussellTerrierEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_TAN =
            SynchedEntityData.defineId(RussellTerrierEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_TRICOLOR =
            SynchedEntityData.defineId(RussellTerrierEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_TRICOLOR =
            SynchedEntityData.defineId(RussellTerrierEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> INTENSITY =
            SynchedEntityData.defineId(RussellTerrierEntity.class, EntityDataSerializers.INT);
    // INTENSITY explanation: Determines between Brown (1), Tan (2), and Cream (3). For Tricolors, 1 is Brown
    // and 2 & 3 are Tan

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.FOX;
    };

    public RussellTerrierEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        RussellTerrierEntity baby = ModEntityTypes.RUSSELL_TERRIER.get().create(serverLevel);

        determineBabyVariant(baby, (RussellTerrierEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);
        
        return baby;
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState state) {
        if (this.isSitting()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.russell_terrier.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.russell_terrier.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.russell_terrier.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.russell_terrier.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.russell_terrier.idle", Animation.LoopType.LOOP));
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
                Component part1;
                Component message;
                if (this.isTan()) {
                    if (this.isTricolor()) {
                        part1 = Component.literal("This Russell Terrier demonstrates two recessive traits.");
                    } else if (this.isTricolorCarrier()) {
                        part1 = Component.literal("This Russell Terrier demonstrates recessive tan fur, and carries tricolor fur.");
                    } else {
                        part1 = Component.literal("This Russell Terrier demonstrates recessive tan fur.");
                    }
                } else if (this.isTanCarrier()) {
                    if (this.isTricolor()) {
                        part1 = Component.literal("This Russell Terrier carries the tan fur trait, and has the alleles to be tricolor.");
                    } else if (this.isTricolorCarrier()) {
                        part1 = Component.literal("This Russell Terrier carries two recessive traits.");
                    } else {
                        part1 = Component.literal("This Russell Terrier carries the recessive tan fur trait.");
                    }
                } else {
                    if (this.isTricolor()) {
                        part1 = Component.literal("This Russell Terrier has the alleles for the tricolor trait.");
                    } else if (this.isTricolorCarrier()) {
                        part1 = Component.literal("This Russell Terrier carries the tricolor trait.");
                    } else {
                        part1 = Component.literal("This Russell Terrier doesn't have any recessive traits.");
                    }
                }

                if (this.getIntensity() == 1) {
                    message = Component.literal(part1.getString() + " They have the alleles for dark fur.");
                } else if (this.getIntensity() == 2) {
                    message = Component.literal(part1.getString() + " They have the alleles for medium fur.");
                } else {
                    message = Component.literal(part1.getString() + " They have the alleles for light fur.");
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
        this.entityData.set(IS_TRICOLOR, tag.getBoolean("IsTricolor"));
        this.entityData.set(CARRIES_TRICOLOR, tag.getBoolean("CarriesTricolor"));
        this.entityData.set(INTENSITY, tag.getInt("Intensity"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsTan", this.isTan());
        tag.putBoolean("CarriesTan", this.isTanCarrier());
        tag.putBoolean("IsTricolor", this.isTricolor());
        tag.putBoolean("CarriesTricolor", this.isTricolorCarrier());
        tag.putInt("Intensity", this.getIntensity());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_TAN, true);
        this.entityData.define(CARRIES_TAN, true);
        this.entityData.define(IS_TRICOLOR, false);
        this.entityData.define(CARRIES_TRICOLOR, false);
        this.entityData.define(INTENSITY, 1);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(2.0D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group,
                                        @Nullable CompoundTag tag) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(5) + 1;
        int rarity = r.nextInt(10) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (rarity < 7) {
            if (determine < 4) {
                var = 0; // BROWN
                setTanStatus(true, true);
                setIntensity(1);
                setTricolorStatus(carrier == 2, false);
            } else {
                var = 1; // BLACK
                setTanStatus(carrier == 1, false);
                setTricolorStatus(carrier == 2, carrier == 2 && r.nextInt(10) + 1 == 10);
                if (r.nextInt(9) + 1 < 7) {
                    setIntensity(1);
                } else if (r.nextInt(5) + 1 < 4) {
                    setIntensity(2);
                } else {
                    setIntensity(3);
                }
            }
        } else if (rarity < 10) {
            setTanStatus(true, true);
            setTricolorStatus(carrier == 2, false);
            if (determine < 4) {
                var = 2; // TAN
                setIntensity(2);
            } else {
                var = 3; // CREAM
                setIntensity(3);
            }
        } else {
            setTanStatus(true, true);
            setTricolorStatus(true, true);
            if (determine < 4) {
                var = 4; // TRI_BROWN
                setIntensity(1);
            } else {
                var = 5; // TRI_TAN
                if (r.nextInt(5) + 1 < 4) {
                    setIntensity(2);
                } else {
                    setIntensity(3);
                }
            }
        }

        // assign chosen variant and finish the method
        RussellTerrierVariant variant = RussellTerrierVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public RussellTerrierVariant getVariant() {
        return RussellTerrierVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(RussellTerrierVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isTan() {
        return this.entityData.get(IS_TAN);
    }

    public boolean isTanCarrier() {
        return this.entityData.get(CARRIES_TAN);
    }

    private void setTanStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_TAN, carrier);
        this.entityData.set(IS_TAN, is);
    }

    public boolean isTricolor() {
        return this.entityData.get(IS_TRICOLOR);
    }

    public boolean isTricolorCarrier() {
        return this.entityData.get(CARRIES_TRICOLOR);
    }

    private void setTricolorStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_TRICOLOR, carrier);
        this.entityData.set(IS_TRICOLOR, is);
    }

    public int getIntensity() {
        return this.entityData.get(INTENSITY);
    }

    private void setIntensity(int intensity) {
        this.entityData.set(INTENSITY, intensity);
    }

    private void determineBabyVariant(RussellTerrierEntity baby, RussellTerrierEntity otherParent) {
        // determine if baby is black or tan
        if (this.isTan() && otherParent.isTan()) {
            // if both parents are tan, baby will be tan
            baby.setTanStatus(true, true);
        } else if ((this.isTan() && otherParent.isTanCarrier()) || (this.isTanCarrier() && otherParent.isTan())) {
            // if one parent is tan and the other is a carrier, baby has 50% chance to be tan and 50% chance to
            // be a carrier
            baby.setTanStatus(true, this.random.nextBoolean());
        } else if (this.isTan() || otherParent.isTan()) {
            // if one parent is tan and the other is not a carrier, baby will be a carrier
            baby.setTanStatus(true, false);
        } else if (this.isTanCarrier() && otherParent.isTanCarrier()) {
            // if both parents are carriers, baby has 25% chance not to be a carrier, 50% chance to be a carrier,
            // and 25% chance to be tan
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setTanStatus(false, false);
            } else {
                baby.setTanStatus(true, determine == 4);
            }
        } else if (this.isTanCarrier() || otherParent.isTanCarrier()) {
            // if one parent is a carrier, baby has 50% chance to be a carrier and 50% chance to be pure black
            baby.setTanStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setTanStatus(false, false);
        }

        // determine if baby is tricolor or not
        if (this.isTricolor() && otherParent.isTricolor()) {
            // if both parents are tricolor, baby will be tricolor
            baby.setTricolorStatus(true, true);
        } else if ((this.isTricolor() && otherParent.isTricolorCarrier()) || (this.isTricolorCarrier() && otherParent.isTricolor())) {
            // if one parent is tri and the other is a carrier, baby has 50% chance to be tricolor and 50%
            // chance to be a carrier
            baby.setTricolorStatus(true, this.random.nextBoolean());
        } else if (this.isTricolor() || otherParent.isTricolor()) {
            // if one parent is tricolor and the other is not a carrier, baby will be a carrier
            baby.setTricolorStatus(true, false);
        } else if (this.isTricolorCarrier() && otherParent.isTricolorCarrier()) {
            // if both parents are carriers, baby has 25% chance not to be a carrier, 50% chance to be a carrier,
            // and 25% chance to be tricolor
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setTricolorStatus(false, false);
            } else {
                baby.setTricolorStatus(true, determine == 4);
            }
        } else if (this.isTricolorCarrier() || otherParent.isTricolorCarrier()) {
            // if one parent is a carrier, baby has 50% chance to be a carrier
            baby.setTricolorStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setTricolorStatus(false, false);
        }

        // determine baby's intensity
        if (this.getIntensity() == 2 && otherParent.getIntensity() == 2) {
            // if both parents have Intensity 2 (tan), baby has 25% chance to have Intensity 1, 50% chance to
            // have intensity 2, and 25% chance to have intensity 3
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setIntensity(1);
            } else if (determine < 4) {
                baby.setIntensity(2);
            } else {
                baby.setIntensity(3);
            }
        } else if (this.getIntensity() == otherParent.getIntensity()) {
            // if both parents have the same Intensity, baby will have the same intensity
            baby.setIntensity(this.getIntensity());
        } else if ((this.getIntensity() == 1 && otherParent.getIntensity() == 3) ||
                (this.getIntensity() == 3 && otherParent.getIntensity() == 1)) {
            // if one parent has Intensity 1 and the other has Intensity 3, baby will be Intensity 2
            baby.setIntensity(2);
        } else {
            // if one parent has Intensity 2 and the other is Intensity 1 or 3, baby will have 50/50 chance of each
            if (this.random.nextBoolean()) {
                baby.setIntensity(this.getIntensity());
            } else {
                baby.setIntensity(otherParent.getIntensity());
            }
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isTan() && baby.isTricolor()) {
            if (baby.getIntensity() == 1) {
                baby.setVariant(RussellTerrierVariant.TRI_BROWN);
            } else {
                baby.setVariant(RussellTerrierVariant.TRI_TAN);
            }
        } else if (baby.isTan()) {
            if (baby.getIntensity() == 1) {
                baby.setVariant(RussellTerrierVariant.BROWN);
            } else if (baby.getIntensity() == 2) {
                baby.setVariant(RussellTerrierVariant.TAN);
            } else {
                baby.setVariant(RussellTerrierVariant.CREAM);
            }
        } else {
            baby.setVariant(RussellTerrierVariant.BLACK);
        }
    }
}
