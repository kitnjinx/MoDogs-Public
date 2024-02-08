package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.HuskyVariant;
import com.kitnjinx.modogs.entity.variant.pattern_variation.HuskyEyeVariant;
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

public class HuskyEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(HuskyEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EYE_COLOR_VARIANT =
            SynchedEntityData.defineId(HuskyEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_RED =
            SynchedEntityData.defineId(HuskyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_RED =
            SynchedEntityData.defineId(HuskyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SOLID =
            SynchedEntityData.defineId(HuskyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_SOLID =
            SynchedEntityData.defineId(HuskyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_WHITE =
            SynchedEntityData.defineId(HuskyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_WHITE =
            SynchedEntityData.defineId(HuskyEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.SHEEP;
    };

    public HuskyEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        HuskyEntity baby = ModEntityTypes.HUSKY.get().create(serverLevel);

        determineBabyVariant(baby, (HuskyEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.husky.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.husky.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.husky.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.husky.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.husky.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller",
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

        if (item == ModItems.GENE_TESTER.get()) {
            if (this.level.isClientSide) {
                Component message;
                if (this.isRed() && this.isSolid()) {
                    if (this.isWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive white fur trait. They also have the alleles for solid red fur.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive red fur and solid pattern traits. They also carry the white fur trait.");
                    } else {
                        message = Component.literal("This Husky demonstrates the recessive red fur and solid pattern traits.");
                    }
                } else if (this.isRed() && this.carriesSolid()) {
                    if (this.isWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive white fur trait. They also have the alleles for red fur and carry the solid pattern trait.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Husky demonstrates the red fur trait. They also carry the white fur and solid pattern traits.");
                    } else {
                        message = Component.literal("This Husky demonstrates the red fur trait. They also carry the solid pattern trait.");
                    }
                } else if (this.isRed()) {
                    if (this.isWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive white fur trait. They also have the alleles for red fur.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive red fur trait. They also carry the white fur trait.");
                    } else {
                        message = Component.literal("This Husky demonstrates the recessive red fur trait.");
                    }
                } else if (this.isSolid() && this.carriesRed()) {
                    if (this.isWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive white fur trait. They also have the alleles for solid-colored fur and carry the red fur trait.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive solid pattern trait. They also carry the red and white fur traits.");
                    } else {
                        message = Component.literal("This Husky demonstrates the recessive solid pattern trait. They also carry the red fur trait.");
                    }
                } else if (this.isSolid()) {
                    if (this.isWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive white fur trait. They also have the alleles for solid-colored fur.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive solid pattern trait. They also carry the white fur trait.");
                    } else {
                        message = Component.literal("This Husky demonstrates the recessive solid pattern trait.");
                    }
                } else if (this.carriesRed() && this.carriesSolid()) {
                    if (this.isWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive white fur trait. They also carry the red fur and solid pattern traits.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Husky carries three recessive traits.");
                    } else {
                        message = Component.literal("This Husky carries the red fur and solid pattern traits.");
                    }
                } else if (this.carriesSolid()) {
                    if (this.isWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive white fur trait. They also carry the solid pattern trait.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Husky carries the white fur and solid pattern traits.");
                    } else {
                        message = Component.literal("This Husky carries the solid pattern trait.");
                    }
                } else if (this.carriesRed()) {
                    if (this.isWhite()) {
                        message = Component.literal("This Husky demonstrates the recessive white fur trait. They also carry the red fur trait.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Husky carries the red and white fur traits.");
                    } else {
                        message = Component.literal("This Husky carries the red fur trait.");
                    }
                } else if (this.isWhite()) {
                    message = Component.literal("This Husky demonstrates the recessive white fur trait.");
                } else if (this.carriesWhite()) {
                    message = Component.literal("This Husky carries the white fur trait.");
                } else {
                    message = Component.literal("This Husky doesn't have any recessive traits.");
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
        this.entityData.set(EYE_COLOR_VARIANT, tag.getInt("EyeVariant"));
        this.entityData.set(IS_RED, tag.getBoolean("IsRed"));
        this.entityData.set(CARRIES_RED, tag.getBoolean("CarriesRed"));
        this.entityData.set(IS_SOLID, tag.getBoolean("IsSolid"));
        this.entityData.set(CARRIES_SOLID, tag.getBoolean("CarriesSolid"));
        this.entityData.set(IS_WHITE, tag.getBoolean("IsWhite"));
        this.entityData.set(CARRIES_WHITE, tag.getBoolean("CarriesWhite"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("EyeVariant", this.getEyeTypeVariant());
        tag.putBoolean("IsRed", this.isRed());
        tag.putBoolean("CarriesRed", this.carriesRed());
        tag.putBoolean("IsSolid", this.isSolid());
        tag.putBoolean("CarriesSolid", this.carriesSolid());
        tag.putBoolean("IsWhite", this.isWhite());
        tag.putBoolean("CarriesWhite", this.carriesWhite());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(EYE_COLOR_VARIANT, 0);
        this.entityData.define(IS_RED, false);
        this.entityData.define(CARRIES_RED, false);
        this.entityData.define(IS_SOLID, true);
        this.entityData.define(CARRIES_SOLID, true);
        this.entityData.define(IS_WHITE, false);
        this.entityData.define(CARRIES_WHITE, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(22.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(2D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group,
                                        @Nullable CompoundTag tag) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(27) + 1;
        int carrier = r.nextInt(8) + 1;
        int eye = r.nextInt(7) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 19) {
            setRedStatus(carrier == 1, false);
            setWhiteStatus(carrier == 2, false);
            if (determine < 11) {
                var = 0; // BLACK
                setSolidStatus(true, true);
            } else {
                var = 1; // GRAY
                setSolidStatus(carrier == 8, false);
            }
        } else if (determine < 27) {
            setRedStatus(true, true);
            setWhiteStatus(carrier == 1, false);
            if (determine < 24) {
                var = 2; // RED
                setSolidStatus(true, true);
            } else {
                var = 3; // SABLE
                setSolidStatus(carrier == 8, false);
            }
        } else {
            var = 4; // WHITE
            setWhiteStatus(true, true);
            int base = r.nextInt(13) + 1;
            int solid = r.nextInt(13) + 1;
            setRedStatus(base < 7, base < 5);
            setSolidStatus(solid < 11, solid < 9);
        }

        // determine husky's eye color
        if (eye < 5) {
            setEyeVariant(HuskyEyeVariant.byId(r.nextInt(2)));
        } else if (eye < 7) {
            setEyeVariant(HuskyEyeVariant.byId(r.nextInt(2) + 2));
        } else {
            setEyeVariant(HuskyEyeVariant.byId(r.nextInt(2) + 4));
        }

        // assign chosen variant and finish the method
        HuskyVariant variant = HuskyVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public HuskyVariant getVariant() {
        return HuskyVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(HuskyVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public HuskyEyeVariant getEyeVariant() {
        return HuskyEyeVariant.byId(this.getEyeTypeVariant() & 255);
    }

    private int getEyeTypeVariant() {
        return this.entityData.get(EYE_COLOR_VARIANT);
    }

    private void setEyeVariant(HuskyEyeVariant variant) {
        this.entityData.set(EYE_COLOR_VARIANT, variant.getId() & 255);
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

    public boolean isSolid() {
        return this.entityData.get(IS_SOLID);
    }

    public boolean carriesSolid() {
        return this.entityData.get(CARRIES_SOLID);
    }

    private void setSolidStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_SOLID, carrier);
        this.entityData.set(IS_SOLID, is);
    }

    public boolean isWhite() {
        return this.entityData.get(IS_WHITE);
    }

    public boolean carriesWhite() {
        return this.entityData.get(CARRIES_WHITE);
    }

    private void setWhiteStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_WHITE, carrier);
        this.entityData.set(IS_WHITE, is);
    }

    private void determineBabyVariant(HuskyEntity baby, HuskyEntity otherParent) {
        // determine if baby is red or black
        if (this.isRed() && otherParent.isRed()) {
            // if both parents are red, baby will be red
            baby.setRedStatus(true, true);
        } else if ((this.isRed() && otherParent.carriesRed()) || (this.carriesRed() && otherParent.isRed())) {
            // if one parent is red and the other is a carrier, baby has 50% chance to be a carrier and
            // 50% chance to be red
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
            // if neither parent has red, baby will not have red
            baby.setRedStatus(false, false);
        }

        // determine if baby is solid or has a saddle marking
        if (this.isSolid() && otherParent.isSolid()) {
            // if both parents are solid, baby will be solid
            baby.setSolidStatus(true, true);
        } else if ((this.isSolid() && otherParent.carriesSolid()) || (this.carriesSolid() && otherParent.isSolid())) {
            // if one parent is solid and the other is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be solid
            baby.setSolidStatus(true, this.random.nextBoolean());
        } else if (this.isSolid() || otherParent.isSolid()) {
            // if only one parent is solid, baby will be a carrier
            baby.setSolidStatus(true, false);
        } else if (this.carriesSolid() && otherParent.carriesSolid()) {
            // if both parents carry solid, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be solid
            int determine = this.random.nextInt(4) + 1;
            baby.setSolidStatus(determine > 1, determine == 4);
        } else if (this.carriesSolid() || otherParent.carriesSolid()) {
            // if only one parent carries solid, baby has 50/50 chance to be a carrier
            baby.setSolidStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setSolidStatus(false, false);
        }

        // determine if baby is pure white or not
        if (this.isWhite() && otherParent.isWhite()) {
            // if both parents are white, baby will be white
            baby.setWhiteStatus(true, true);
        } else if ((this.isWhite() && otherParent.carriesWhite()) || (this.carriesWhite() && otherParent.isWhite())) {
            // if one parent is white and the other is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be white
            baby.setWhiteStatus(true, this.random.nextBoolean());
        } else if (this.isWhite() || otherParent.isWhite()) {
            // if only one parent is white, baby will be a carrier
            baby.setWhiteStatus(true, false);
        } else if (this.carriesWhite() && otherParent.carriesWhite()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be white
            int determine = this.random.nextInt(4) + 1;
            baby.setWhiteStatus(determine > 1, determine == 4);
        } else if (this.carriesWhite() || otherParent.carriesWhite()) {
            // if only one parent carries white, baby has 50/50 chance to be a carrier
            baby.setWhiteStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not have white
            baby.setWhiteStatus(false, false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isWhite()) {
            baby.setVariant(HuskyVariant.WHITE);
        } else if (baby.isSolid() && baby.isRed()) {
            baby.setVariant(HuskyVariant.RED);
        } else if (baby.isSolid()) {
            baby.setVariant(HuskyVariant.BLACK);
        } else if (baby.isRed()) {
            baby.setVariant(HuskyVariant.SABLE);
        } else {
            baby.setVariant(HuskyVariant.GRAY);
        }

        // determine baby's eye color based on parents
        boolean parAHet = this.getEyeTypeVariant() == 4 || this.getEyeTypeVariant() == 5;
        boolean parBHet = otherParent.getEyeTypeVariant() == 4 || otherParent.getEyeTypeVariant() == 5;

        if (parAHet && parBHet) {
            // if both parents have heterochromia, baby has 50% chance to have heterochromia, 25% chance to
            // have brown eyes, and 25% chance to have blue eyes
            int determine = this.random.nextInt(4) + 1;
            if (determine < 3 && this.getEyeVariant() == otherParent.getEyeVariant()) {
                baby.setEyeVariant(this.getEyeVariant());
            } else if (determine < 3) {
                baby.setEyeVariant(HuskyEyeVariant.byId(this.random.nextInt(2) + 4));
            } else if (determine < 4) {
                baby.setEyeVariant(HuskyEyeVariant.byId(this.random.nextInt(2) + 2));
            } else {
                baby.setEyeVariant(HuskyEyeVariant.byId(this.random.nextInt(2)));
            }
        } else if (this.getEyeVariant() == otherParent.getEyeVariant()) {
            // if both parents have the same color & shade of eyes, baby will be the same
            baby.setEyeVariant(this.getEyeVariant());
        } else {
            boolean parABlue = this.getEyeTypeVariant() == 0 || this.getEyeTypeVariant() == 1;
            boolean parBBlue = otherParent.getEyeTypeVariant() == 0 || otherParent.getEyeTypeVariant() == 1;

            if (parABlue && parBBlue) {
                // if both parents have blue eyes, baby will have blue eyes
                baby.setEyeVariant(HuskyEyeVariant.byId(this.random.nextInt(2)));
            } else if (parABlue || parBBlue) {
                // if one parent has blue eyes and one has brown, baby will have heterochromia
                baby.setEyeVariant(HuskyEyeVariant.byId(this.random.nextInt(2) + 4));
            } else {
                // if both parents have brown eyes, baby will have brown eyes
                baby.setEyeVariant(HuskyEyeVariant.byId(this.random.nextInt(2) + 2));
            }
        }
    }
}