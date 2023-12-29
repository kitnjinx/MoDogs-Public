package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.GermanShepherdVariant;
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
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.Random;
import java.util.function.Predicate;

public class GermanShepherdEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(GermanShepherdEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BLACK_DEGREE =
            SynchedEntityData.defineId(GermanShepherdEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_WHITE =
            SynchedEntityData.defineId(GermanShepherdEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.SHEEP;
    };

    public GermanShepherdEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        GermanShepherdEntity baby = ModEntityTypes.GERMAN_SHEPHERD.get().create(serverLevel);

        determineBabyVariant(baby, (GermanShepherdEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);
        
        return baby;
    }

    private <E extends GermanShepherdEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.germanshepherd.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.germanshepherd.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.germanshepherd.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.germanshepherd.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.germanshepherd.idle"));
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
                Component message;
                if (this.getVariant() == GermanShepherdVariant.WHITE) {
                    if (this.getBlackDegree() == 0) {
                        message = Component.literal("This German Shepherd demonstrates a recessive trait. They have otherwise standard traits.");
                    } else if (this.getBlackDegree() == 1) {
                        message = Component.literal("This German Shepherd demonstrates a recessive trait. They have alleles for high amounts of black fur.");
                    } else {
                        message = Component.literal("This German Shepherd demonstrates a recessive trait. They have alleles for a purely black coat.");
                    }
                } else if (this.getCarrier()) {
                    message = Component.literal("This German Shepherd carries a recessive trait.");
                } else {
                    message = Component.literal("This German Shepherd doesn't have any recessive traits.");
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
        this.entityData.set(BLACK_DEGREE, tag.getInt("BlackDegree"));
        this.entityData.set(CARRIES_WHITE, tag.getBoolean("CarrierStatus"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("BlackDegree", this.getBlackDegree());
        tag.putBoolean("CarrierStatus", this.getCarrier());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(BLACK_DEGREE, 0);
        this.entityData.define(CARRIES_WHITE, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
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
        int determine = r.nextInt(16) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 9) {
            var = 0; // STANDARD
            setBlackDegree(0);
        } else if (determine < 13) {
            var = 1; // BROWN_POINTS
            setBlackDegree(1);
        } else if (determine < 16) {
            var = 2; // BLACK
            setBlackDegree(2);
        } else {
            var = 3; // WHITE
            int rand = r.nextInt(15) + 1;
            if (rand < 9) {
                setBlackDegree(0);
            } else if (rand < 13) {
                setBlackDegree(1);
            } else {
                setBlackDegree(2);
            }
        }

        setCarrier(var == 3 || carrier == 4); // if dog is white or rolled to be a carrier, boolean is true

        // assign chosen variant and finish the method
        GermanShepherdVariant variant = GermanShepherdVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public GermanShepherdVariant getVariant() {
        return GermanShepherdVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(GermanShepherdVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public int getBlackDegree() {
        return this.entityData.get(BLACK_DEGREE);
    }

    public void setBlackDegree(int degree) {
        this.entityData.set(BLACK_DEGREE, degree);
    }

    public boolean getCarrier() {
        return this.entityData.get(CARRIES_WHITE);
    }

    public void setCarrier(boolean status) {
        this.entityData.set(CARRIES_WHITE, status);
    }

    private void determineBabyVariant(GermanShepherdEntity baby, GermanShepherdEntity otherParent) {
        // determine baby's BlackDegree (how much black they have)
        if (this.getBlackDegree() == 1 && otherParent.getBlackDegree() == 1) {
            // if both parents have a medium level of black (BROWN_POINTS variant), baby has a 25% chance to inherit
            // low levels of black (STANDARD), 50% chance of medium levels, and 25% chance of high levels (BLACK)
            int determine = this.random.nextInt(4) + 1;

            if (determine == 1) {
                baby.setBlackDegree(0);
            } else if (determine < 4) {
                baby.setBlackDegree(1);
            } else {
                baby.setBlackDegree(2);
            }
        } else if (this.getBlackDegree() == otherParent.getBlackDegree()) {
            // if both parents have either low or high levels of black, baby will have the same BlackDegree as them
            baby.setBlackDegree(this.getBlackDegree());
        } else if ((this.getBlackDegree() == 0 && otherParent.getBlackDegree() == 2) ||
                (this.getBlackDegree() == 2 && otherParent.getBlackDegree() == 0)) {
            // if one parent has low levels of black and one parent has high levels, baby will have medium levels
            baby.setBlackDegree(1);
        } else {
            // if no above conditions are triggered, baby will inherit its BlackDegree from a random parent
            if (this.random.nextBoolean()) {
                baby.setBlackDegree(this.getBlackDegree());
            } else {
                baby.setBlackDegree(otherParent.getBlackDegree());
            }
        }

        // determine if baby is white, a carrier, or neither
        if (this.getVariant() == GermanShepherdVariant.WHITE &&
                otherParent.getVariant() == GermanShepherdVariant.WHITE) {
            // if both parents are white, baby will be marked as a carrier and have the White variant
            baby.setCarrier(true);
            baby.setVariant(GermanShepherdVariant.WHITE);
        } else if ((this.getVariant() == GermanShepherdVariant.WHITE && otherParent.getCarrier()) ||
                (this.getCarrier() && otherParent.getVariant() == GermanShepherdVariant.WHITE)) {
            // if one parent is white and the other a carrier, the baby will be marked as a carrier and have
            // a 50/50 chance of being White or a normal coat based on the baby's BlackDegree
            baby.setCarrier(true);
            if (this.random.nextBoolean()) {
                baby.setVariant(GermanShepherdVariant.WHITE);
            } else {
                baby.setVariant(GermanShepherdVariant.byId(baby.getBlackDegree()));
            }
        } else if ((this.getVariant() == GermanShepherdVariant.WHITE && !otherParent.getCarrier()) ||
                (!this.getCarrier() && otherParent.getVariant() == GermanShepherdVariant.WHITE)) {
            // if one parent is white and the other is not a carrier, the baby will be marked as a carrier and
            // have a variant based on the baby's BlackDegree
            baby.setCarrier(true);
            baby.setVariant(GermanShepherdVariant.byId(baby.getBlackDegree()));
        } else if (this.getCarrier() && otherParent.getCarrier()) {
            // if both parents are a carrier, baby has 25% chance not to be a carrier, 50% to be a carrier, and
            // 25% to be white. If baby is not white, baby will have a variant based on its BlackDegree
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setCarrier(false);
                baby.setVariant(GermanShepherdVariant.byId(baby.getBlackDegree()));
            } else if (determine < 4) {
                baby.setCarrier(true);
                baby.setVariant(GermanShepherdVariant.byId(baby.getBlackDegree()));
            } else {
                baby.setCarrier(true);
                baby.setVariant(GermanShepherdVariant.WHITE);
            }
        } else if (this.getCarrier() || otherParent.getCarrier()) {
            // if one parent is a carrier, baby has a 50% chance to be a carrier. Baby's visible variant will
            // be based on its BlackDegree
            baby.setVariant(GermanShepherdVariant.byId(baby.getBlackDegree()));
            baby.setCarrier(this.random.nextBoolean());
        } else {
            // if neither parent is a carrier, baby won't be a carrier and will have a variant based on its BlackDegree
            baby.setVariant(GermanShepherdVariant.byId(baby.getBlackDegree()));
        }
    }
}