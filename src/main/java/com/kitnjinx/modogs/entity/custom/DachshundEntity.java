package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.DachshundVariant;
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

public class DachshundEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(DachshundEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_CHOCOLATE =
            SynchedEntityData.defineId(DachshundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_CHOCOLATE =
            SynchedEntityData.defineId(DachshundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_FAWN =
            SynchedEntityData.defineId(DachshundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_FAWN =
            SynchedEntityData.defineId(DachshundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_CREAM =
            SynchedEntityData.defineId(DachshundEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_CREAM =
            SynchedEntityData.defineId(DachshundEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT;
    };

    public DachshundEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 18.0f)
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
        DachshundEntity baby = ModEntityTypes.DACHSHUND.get().create(serverLevel);

        determineBabyVariant(baby, (DachshundEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.dachshund.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.dachshund.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.dachshund.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.dachshund.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.dachshund.idle", Animation.LoopType.LOOP));
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

        Item itemForTaming = ModItems.RABBIT_TREAT.get();

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
                if (this.isFawn() && this.isCream()) {
                    if (this.isChocolate()) {
                        message = Component.literal("This Dachshund demonstrates two recessive traits, and has the alleles for chocolate fur.");
                    } else if (this.getChocolateCarrier()) {
                        message = Component.literal("This Dachshund demonstrates two recessive traits, and carries the allele for chocolate fur.");
                    } else {
                        message = Component.literal("This Dachshund demonstrates two recessive traits. They have otherwise standard traits.");
                    }
                } else if (this.isFawn()) {
                    if (this.isChocolate() && this.getCreamCarrier()) {
                        message = Component.literal("This Dachshund demonstrates the recessive dilution trait. They also have the alleles for chocolate fur and carry a recessive trait.");
                    } else if (this.isChocolate()) {
                        message = Component.literal("This Dachshund demonstrates the recessive dilution trait. They also have the alleles for chocolate fur.");
                    } else if (this.getChocolateCarrier() && this.getCreamCarrier()) {
                        message = Component.literal("This Dachshund demonstrates the recessive dilution trait. They also carry two recessive traits.");
                    } else if (this.getChocolateCarrier()) {
                        message = Component.literal("This Dachshund demonstrates the recessive dilution trait. They also carry the recessive chocolate fur trait.");
                    } else if (this.getCreamCarrier()) {
                        message = Component.literal("This Dachshund demonstrates the recessive dilution trait. They also carry the recessive cream trait.");
                    } else {
                        message = Component.literal("This Dachshund demonstrates the recessive dilution trait. They have otherwise standard traits.");
                    }
                } else if (this.isChocolate() && this.isCream()) {
                    if (this.getFawnCarrier()) {
                        message = Component.literal("This Dachshund demonstrates two recessive traits. They also carries the dilution trait.");
                    } else {
                        message = Component.literal("This Dachshund demonstrates two recessive traits.");
                    }
                } else if (this.isChocolate()) {
                    if (this.getFawnCarrier() && this.getCreamCarrier()) {
                        message = Component.literal("This Dachshund demonstrates the recessive chocolate fur trait. They also carry two recessive traits.");
                    } else if (this.getFawnCarrier()) {
                        message = Component.literal("This Dachshund demonstrates the recessive chocolate fur trait, and carries the recessive dilution trait.");
                    } else if (this.getCreamCarrier()) {
                        message = Component.literal("This Dachshund demonstrates the recessive chocolate fur trait, and carries the recessive cream trait.");
                    } else {
                        message = Component.literal("This Dachshund demonstrates the recessive chocolate fur trait.");
                    }
                } else if (this.isCream()) {
                    if (this.getFawnCarrier() && this.getChocolateCarrier()) {
                        message = Component.literal("This Dachshund demonstrates the recessive cream trait. They also carry two recessive traits.");
                    } else if (this.getFawnCarrier()) {
                        message = Component.literal("This Dachshund demonstrates the recessive cream trait, and carries the recessive dilution trait.");
                    } else if (this.getChocolateCarrier()) {
                        message = Component.literal("This Dachshund demonstrates the recessive cream trait, and carries the recessive chocolate fur trait.");
                    } else {
                        message = Component.literal("This Dachshund demonstrates the recessive cream trait.");
                    }
                } else {
                    if (this.getCreamCarrier()) {
                        if (this.getFawnCarrier() && this.getChocolateCarrier()) {
                            message = Component.literal("This Dachshund carries three recessive traits.");
                        } else if (this.getFawnCarrier()) {
                            message = Component.literal("This Dachshund carries the recessive cream and dilution traits.");
                        } else if (this.getChocolateCarrier()) {
                            message = Component.literal("This Dachshund carries the recessive cream and chocolate fur traits.");
                        } else {
                            message = Component.literal("This Dachshund carries the recessive cream trait.");
                        }
                    } else {
                        if (this.getFawnCarrier() && this.getChocolateCarrier()) {
                            message = Component.literal("This Dachshund carries the recessive dilution and chocolate fur traits.");
                        } else if (this.getFawnCarrier()) {
                            message = Component.literal("This Dachshund carries the recessive dilution trait.");
                        } else if (this.getChocolateCarrier()) {
                            message = Component.literal("This Dachshund carries the recessive chocolate fur trait.");
                        } else {
                            message = Component.literal("This Dachshund doesn't carry any recessive traits.");
                        }
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
        this.entityData.set(CARRIES_CHOCOLATE, tag.getBoolean("ChocolateCarrier"));
        this.entityData.set(IS_CHOCOLATE, tag.getBoolean("IsChocolate"));
        this.entityData.set(CARRIES_FAWN, tag.getBoolean("FawnCarrier"));
        this.entityData.set(IS_FAWN, tag.getBoolean("IsFawn"));
        this.entityData.set(CARRIES_CREAM, tag.getBoolean("CreamCarrier"));
        this.entityData.set(IS_CREAM, tag.getBoolean("IsCream"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("ChocolateCarrier", this.getChocolateCarrier());
        tag.putBoolean("IsChocolate", this.isChocolate());
        tag.putBoolean("FawnCarrier", this.getFawnCarrier());
        tag.putBoolean("IsFawn", this.isFawn());
        tag.putBoolean("CreamCarrier", this.getCreamCarrier());
        tag.putBoolean("IsCream", this.isCream());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(CARRIES_CHOCOLATE, false);
        this.entityData.define(IS_CHOCOLATE, false);
        this.entityData.define(CARRIES_FAWN, false);
        this.entityData.define(IS_FAWN, false);
        this.entityData.define(CARRIES_CREAM, false);
        this.entityData.define(IS_CREAM, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(18.0);
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
        int determine = r.nextInt(9) + 1;
        int cream = r.nextInt(3) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        if (cream == 3) {
            setCreamStatus(true, true);
            if (determine < 6) {
                var = 3; // BLACK_CREAM
                setChocolateStatus(carrier == 1, false);
                setFawnStatus(carrier == 2, false);
            } else if (determine < 9) {
                var = 4; // CHOCOLATE_CREAM
                setChocolateStatus(true, true);
                setFawnStatus(carrier == 2, false);
            } else {
                var = 5; // FAWN_CREAM
                setFawnStatus(true, true);
                if (r.nextInt(8) + 1 < 6) {
                    setChocolateStatus(carrier == 1, false);
                } else {
                    setChocolateStatus(true, true);
                }
            }
        } else {
            setCreamStatus(r.nextInt(4) == 0, false);
            if (determine < 6) {
                var = 0; // BLACK_TAN
                setChocolateStatus(carrier == 1, false);
                setFawnStatus(carrier == 2, false);
            } else if (determine < 9) {
                var = 1; // CHOCOLATE_TAN
                setChocolateStatus(true, true);
                setFawnStatus(carrier == 2, false);
            } else {
                var = 2; // FAWN_TAN
                setFawnStatus(true, true);
                if (r.nextInt(8) + 1 < 6) {
                    setChocolateStatus(carrier == 1, false);
                } else {
                    setChocolateStatus(true, true);
                }
            }
        }

        // assign chosen variant and finish the method
        DachshundVariant variant = DachshundVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public DachshundVariant getVariant() {
        return DachshundVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(DachshundVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }


    public boolean getChocolateCarrier() {
        return this.entityData.get(CARRIES_CHOCOLATE);
    }

    public boolean isChocolate() {
        return this.entityData.get(IS_CHOCOLATE);
    }

    private void setChocolateStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_CHOCOLATE, carrier);
        this.entityData.set(IS_CHOCOLATE, is);
    }

    public boolean getFawnCarrier() {
        return this.entityData.get(CARRIES_FAWN);
    }

    public boolean isFawn() {
        return this.entityData.get(IS_FAWN);
    }

    private void setFawnStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_FAWN, carrier);
        this.entityData.set(IS_FAWN, is);
    }
    public boolean getCreamCarrier() {
        return this.entityData.get(CARRIES_CREAM);
    }

    public boolean isCream() {
        return this.entityData.get(IS_CREAM);
    }

    private void setCreamStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_CREAM, carrier);
        this.entityData.set(IS_CREAM, is);
    }

    private void determineBabyVariant(DachshundEntity baby, DachshundEntity otherParent) {
        // determine if baby is black or chocolate
        if (this.isChocolate() && otherParent.isChocolate()) {
            // if both parents are chocolate, baby will be chocolate
            baby.setChocolateStatus(true, true);
        } else if ((this.isChocolate() && otherParent.getChocolateCarrier()) ||
                (this.getChocolateCarrier() && otherParent.isChocolate())) {
            // if one parent is chocolate and one parent is a carrier, baby has 50% chance to be chocolate and
            // 50% chance to be a carrier
            baby.setChocolateStatus(true, this.random.nextBoolean());
        } else if (this.isChocolate() || otherParent.isChocolate()) {
            // if one parent is chocolate and the other is not a carrier, baby will be black and carry chocolate
            baby.setChocolateStatus(true, false);
        } else if (this.getChocolateCarrier() && otherParent.getChocolateCarrier()) {
            // if both parents are chocolate carriers, baby has 25% chance not to carry chocolate, 50% chance to
            // be black and carry chocolate, and 25% chance to be chocolate
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setChocolateStatus(false, false);
            } else {
                baby.setChocolateStatus(true, determine == 4);
            }
        } else if (this.getChocolateCarrier() || otherParent.getChocolateCarrier()) {
            // if only one parent is a chocolate carrier, baby will be black and has 50% chance to carry chocolate
            baby.setChocolateStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a chocolate carrier, baby will be black and won't carry chocolate
            baby.setChocolateStatus(false, false);
        }

        // determine if baby is diluted (fawn) or not
        if (this.isFawn() && otherParent.isFawn()) {
            // if both parents are fawn, baby will be fawn
            baby.setFawnStatus(true, true);
        } else if ((this.isFawn() && otherParent.getFawnCarrier()) ||
                (this.getFawnCarrier() && otherParent.isFawn())) {
            // if one parent is fawn and one parent is a carrier, baby has 50% chance to be fawn and 50% chance to
            // be a carrier
            baby.setFawnStatus(true, this.random.nextBoolean());
        } else if (this.isFawn() || otherParent.isFawn()) {
            // if one parent is fawn and the other is not a carrier, baby will carry fawn
            baby.setFawnStatus(true, false);
        } else if (this.getFawnCarrier() && otherParent.getFawnCarrier()) {
            // if both parents are fawn carriers, baby has 25% chance not to carry fawn, 50% chance to carry
            // fawn, and 25% chance to be fawn
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setFawnStatus(false, false);
            } else {
                baby.setFawnStatus(true, determine == 4);
            }
        } else if (this.getFawnCarrier() || otherParent.getFawnCarrier()) {
            // if only one parent is a fawn carrier, baby will have 50% chance to carry fawn
            baby.setFawnStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a fawn carrier, baby won't carry fawn
            baby.setFawnStatus(false, false);
        }

        // determine if baby is tan or cream
        if (this.isCream() && otherParent.isCream()) {
            // if both parents are cream, baby will be cream
            baby.setCreamStatus(true, true);
        } else if ((this.isCream() && otherParent.getCreamCarrier()) ||
                (this.getCreamCarrier() && otherParent.isCream())) {
            // if one parent is cream and one parent is a carrier, baby has 50% chance to be cream and 50%
            // chance to be a carrier
            baby.setCreamStatus(true, this.random.nextBoolean());
        } else if (this.isCream() || otherParent.isCream()) {
            // if one parent is cream and the other is not a carrier, baby will carry cream
            baby.setCreamStatus(true, false);
        } else if (this.getCreamCarrier() && otherParent.getCreamCarrier()) {
            // if both parents are cream carriers, baby has 25% chance not to carry cream, 50% chance to carry
            // cream, and 25% chance to be cream
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setCreamStatus(false, false);
            } else {
                baby.setCreamStatus(true, determine == 4);
            }
        } else if (this.getCreamCarrier() || otherParent.getCreamCarrier()) {
            // if only one parent is a cream carrier, baby will have 50% chance to carry cream
            baby.setCreamStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a cream carrier, baby won't carry fawn
            baby.setCreamStatus(false, false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isFawn()) {
            if (baby.isCream()) {
                baby.setVariant(DachshundVariant.FAWN_CREAM);
            } else {
                baby.setVariant(DachshundVariant.FAWN_TAN);
            }
        } else if (baby.isChocolate()) {
            if (baby.isCream()) {
                baby.setVariant(DachshundVariant.CHOCOLATE_CREAM);
            } else {
                baby.setVariant(DachshundVariant.CHOCOLATE_TAN);
            }
        } else {
            if (baby.isCream()) {
                baby.setVariant(DachshundVariant.BLACK_CREAM);
            } else {
                baby.setVariant(DachshundVariant.BLACK_TAN);
            }
        }
    }
}
