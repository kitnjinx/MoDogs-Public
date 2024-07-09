package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.MudiVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.pattern_variation.TwoMerleVariant;
import com.kitnjinx.modogs.item.ModItems;
import net.minecraft.Util;
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

public class MudiEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(MudiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_BROWN =
            SynchedEntityData.defineId(MudiEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BROWN =
            SynchedEntityData.defineId(MudiEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_WHITE =
            SynchedEntityData.defineId(MudiEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_MERLE =
            SynchedEntityData.defineId(MudiEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MERLE_PATTERN =
            SynchedEntityData.defineId(MudiEntity.class, EntityDataSerializers.INT);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.SHEEP || entitytype == EntityType.COW;
    };

    public MudiEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 3.0f)
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
        MudiEntity baby = ModEntityTypes.MUDI.get().create(serverLevel);

        determineBabyVariant(baby, (MudiEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.mudi.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.mudi.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.mudi.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.mudi.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.mudi.idle", Animation.LoopType.LOOP));
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

        Item itemForTaming = ModItems.BEEF_TREAT.get();
        Item itemForTaming2 = ModItems.MUTTON_TREAT.get();

        if ((item == itemForTaming || item == itemForTaming2) && !isTame()) {
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
                if (this.isBrown() && this.hasMerle()) {
                    if (this.getVariant() == MudiVariant.WHITE) {
                        message = Component.literal("This Mudi demonstrates the recessive white fur trait. They also have the alleles for brown fur and merle.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Mudi demonstrates the merle and recessive brown fur traits. They also carry the white fur trait.");
                    } else {
                        message = Component.literal("This Mudi demonstrates the merle and recessive brown fur traits.");
                    }
                } else if (this.isBrown()) {
                    if (this.getVariant() == MudiVariant.WHITE) {
                        message = Component.literal("This Mudi demonstrates the recessive white fur trait. They also have the alleles for brown fur.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Mudi demonstrates the recessive brown fur trait and carries the white fur trait.");
                    } else {
                        message = Component.literal("This Mudi demonstrates the recessive brown fur trait.");
                    }
                } else if (this.carriesBrown() && this.hasMerle()) {
                    if (this.getVariant() == MudiVariant.WHITE) {
                        message = Component.literal("This Mudi demonstrates the recessive white fur trait. They also have the merle trait and carry the brown fur trait.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Mudi demonstrates the merle trait, and carries two recessive traits.");
                    } else {
                        message = Component.literal("This Mudi demonstrates the merle trait, and carries the brown fur trait.");
                    }
                } else if (this.carriesBrown()) {
                    if (this.getVariant() == MudiVariant.WHITE) {
                        message = Component.literal("This Mudi demonstrates the recessive white fur trait and carries the brown fur trait.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Mudi carries two recessive traits.");
                    } else {
                        message = Component.literal("This Mudi carries the recessive brown fur trait.");
                    }
                } else if (this.hasMerle()) {
                    if (this.getVariant() == MudiVariant.WHITE) {
                        message = Component.literal("This Mudi demonstrates the recessive white fur trait, and also has the merle trait.");
                    } else if (this.carriesWhite()) {
                        message = Component.literal("This Mudi demonstrates the merle trait and carries the white fur trait.");
                    } else {
                        message = Component.literal("This Mudi demonstrates the merle trait.");
                    }
                } else if (this.getVariant() == MudiVariant.WHITE){
                    message = Component.literal("This Mudi demonstrates the recessive white fur trait.");
                } else if (this.carriesWhite()) {
                    message = Component.literal("This Mudi carries the white fur trait.");
                } else {
                    message = Component.literal("This Mudi doesn't have any recessive traits.");
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
        this.entityData.set(CARRIES_BROWN, tag.getBoolean("CarriesBrown"));
        this.entityData.set(CARRIES_WHITE, tag.getBoolean("CarriesWhite"));
        this.entityData.set(HAS_MERLE, tag.getBoolean("HasMerle"));
        this.entityData.set(MERLE_PATTERN, tag.getInt("MerlePattern"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsBrown", this.isBrown());
        tag.putBoolean("CarriesBrown", this.carriesBrown());
        tag.putBoolean("CarriesWhite", this.carriesWhite());
        tag.putBoolean("HasMerle", this.hasMerle());
        tag.putInt("MerlePattern", this.getMerleVariant());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_BROWN, false);
        this.entityData.define(CARRIES_BROWN, false);
        this.entityData.define(CARRIES_WHITE, false);
        this.entityData.define(HAS_MERLE, false);
        this.entityData.define(MERLE_PATTERN, 0);
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
        int determine = r.nextInt(10) +1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 8) {
            var = 0; // BLACK
            setMerle(r.nextBoolean());
            setBrownStatus(carrier == 1, false);
            setWhite(carrier == 2);
        } else if (determine < 10) {
            var = 1; // BROWN
            setMerle(r.nextInt(5) + 1 == 5);
            setBrownStatus(true, true);
            setWhite(carrier == 1);
        } else {
            var = 2; // WHITE
            setWhite(true);
            if (r.nextInt(5) + 1 < 5) {
                setBrownStatus(carrier == 1, false);
                setMerle(r.nextBoolean());
            } else {
                setBrownStatus(true, true);
                setMerle(r.nextInt(5) + 1 == 5);
            }
        }

        // assign chosen variant and finish the method
        setVariant(MudiVariant.byId(var));
        setMerlePattern(Util.getRandom(TwoMerleVariant.values(), this.random));
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public MudiVariant getVariant() {
        return MudiVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(MudiVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isBrown() {
        return this.entityData.get(IS_BROWN);
    }

    public boolean carriesBrown() {
        return this.entityData.get(CARRIES_BROWN);
    }

    private void setBrownStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_BROWN, carrier);
        this.entityData.set(IS_BROWN, is);
    }

    public boolean carriesWhite() {
        return this.entityData.get(CARRIES_WHITE);
    }

    private void setWhite(boolean carrier) {
        this.entityData.set(CARRIES_WHITE, carrier);
    }

    public boolean hasMerle() {
        return this.entityData.get(HAS_MERLE);
    }

    private void setMerle(boolean has) {
        this.entityData.set(HAS_MERLE, has);
    }

    public TwoMerleVariant getMerlePattern() {
        return TwoMerleVariant.byId(this.getMerleVariant() & 255);
    }

    private int getMerleVariant() {
        return this.entityData.get(MERLE_PATTERN);
    }

    private void setMerlePattern(TwoMerleVariant variant) {
        this.entityData.set(MERLE_PATTERN, variant.getId() & 255);
    }

    private void determineBabyVariant(MudiEntity baby, MudiEntity otherParent) {
        // determine if baby is brown or black
        if (this.isBrown() && otherParent.isBrown()) {
            // if both parents are brown, baby will be brown
            baby.setBrownStatus(true, true);
        } else if ((this.isBrown() && otherParent.carriesBrown()) || (this.carriesBrown() && otherParent.isBrown())) {
            // if one parent is brown and the other is a carrier, baby has 50% chance to be brown and 50%
            // chance to be a carrier
            baby.setBrownStatus(true, this.random.nextBoolean());
        } else if (this.isBrown() || otherParent.isBrown()) {
            // if only one parent is brown, baby will be a carrier
            baby.setBrownStatus(true, false);
        } else if (this.carriesBrown() && otherParent.carriesBrown()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier,
            // and 50% chance to be brown
            int determine = this.random.nextInt(4) + 1;
            baby.setBrownStatus(determine > 1, determine == 4);
        } else if (this.carriesBrown() || otherParent.carriesBrown()) {
            // if only one parent carries brown, baby has 50/50 chance to be a carrier
            baby.setBrownStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setBrownStatus(false, false);
        }

        // determine if baby has merle or not
        if (this.hasMerle() && otherParent.hasMerle()) {
            // if both parents have merle, baby will be merle
            baby.setMerle(true);
        } else if (this.hasMerle() || otherParent.hasMerle()) {
            // if only one parent has merle, baby has 50/50 chance to have merle
            baby.setMerle(this.random.nextBoolean());
        } else {
            // if neither parent has merle, baby won't have merle
            baby.setMerle(false);
        }

        // determine if baby is white or not
        boolean isWhite;
        if (this.getVariant() == MudiVariant.WHITE && otherParent.getVariant() == MudiVariant.WHITE) {
            // if both parents are white, baby will be white
            isWhite = true;
            baby.setWhite(true);
        } else if ((this.getVariant() == MudiVariant.WHITE && otherParent.carriesWhite()) ||
                (this.carriesWhite() && otherParent.getVariant() == MudiVariant.WHITE)) {
            // if one parent is white and the other is a carrier, baby has 50% chance to be white and 50%
            // chance to be a carrier
            isWhite = this.random.nextBoolean();
            baby.setWhite(true);
        } else if (this.getVariant() == MudiVariant.WHITE || otherParent.getVariant() == MudiVariant.WHITE) {
            // if only one parent is white, baby will be a carrier
            isWhite = false;
            baby.setWhite(true);
        } else if (this.carriesWhite() && otherParent.carriesWhite()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be white
            int determine = this.random.nextInt(4) + 1;
            isWhite = determine == 4;
            baby.setWhite(determine > 1);
        } else if (this.carriesWhite() || otherParent.carriesWhite()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            isWhite = false;
            baby.setWhite(this.random.nextBoolean());
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            isWhite = false;
            baby.setWhite(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (isWhite) {
            baby.setVariant(MudiVariant.WHITE);
        } else if (baby.isBrown()) {
            baby.setVariant(MudiVariant.BROWN);
        } else {
            baby.setVariant(MudiVariant.BLACK);
        }

        // determine baby's merle pattern
        if (this.hasMerle() && otherParent.hasMerle() && baby.hasMerle() &&
                this.getMerlePattern() == otherParent.getMerlePattern()) {
            baby.setMerlePattern(this.getMerlePattern());
        } else {
            baby.setMerlePattern(Util.getRandom(TwoMerleVariant.values(), baby.random));
        }
    }
}