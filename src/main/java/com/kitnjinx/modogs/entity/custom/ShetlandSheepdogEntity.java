package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.ShetlandSheepdogVariant;
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
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.Random;
import java.util.function.Predicate;

public class ShetlandSheepdogEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(ShetlandSheepdogEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_SABLE =
            SynchedEntityData.defineId(ShetlandSheepdogEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_SABLE =
            SynchedEntityData.defineId(ShetlandSheepdogEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_TAN =
            SynchedEntityData.defineId(ShetlandSheepdogEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_TAN =
            SynchedEntityData.defineId(ShetlandSheepdogEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MERLE =
            SynchedEntityData.defineId(ShetlandSheepdogEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.SHEEP;
    };

    public ShetlandSheepdogEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        ShetlandSheepdogEntity baby = ModEntityTypes.SHETLAND_SHEEPDOG.get().create(serverLevel);

        determineBabyVariant(baby, (ShetlandSheepdogEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends ShetlandSheepdogEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shetland_sheepdog.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shetland_sheepdog.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shetland_sheepdog.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shetland_sheepdog.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shetland_sheepdog.idle"));
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
                Component message;
                if (this.hasMerle()) {
                    if (this.isSable() && this.isTan()) {
                        message = Component.literal("This Shetland Sheepdog demonstrates the merle and recessive sable fur traits. They also have the alleles for the recessive points trait.");
                    } else if (this.isSable()) {
                        if (this.carriesTan()) {
                            message = Component.literal("This Shetland Sheepdog demonstrates the merle and recessive sable fur traits. They also carry the points trait.");
                        } else {
                            message = Component.literal("This Shetland Sheepdog demonstrates the merle and recessive sable fur traits.");
                        }
                    } else if (this.isTan()) {
                        if (this.carriesSable()) {
                            message = Component.literal("This Shetland Sheepdog demonstrates the merle and recessive points trait. They also carry the sable fur trait.");
                        } else {
                            message = Component.literal("This Shetland Sheepdog demonstrates the merle and recessive points trait.");
                        }
                    } else {
                        if (this.carriesSable() && this.carriesTan()) {
                            message = Component.literal("This Shetland Sheepdog demonstrates the merle trait, and carries two recessive traits.");
                        } else if (this.carriesTan()) {
                            message = Component.literal("This Shetland Sheepdog demonstrates the merle trait, and carries the recessive points trait.");
                        } else if (this.carriesSable()) {
                            message = Component.literal("This Shetland Sheepdog demonstrates the merle trait, and carries the recessive sable fur trait.");
                        } else {
                            message = Component.literal("This Shetland Sheepdog demonstrates the merle trait.");
                        }
                    }
                } else {
                    if (this.isSable() && this.isTan()) {
                        message = Component.literal("This Shetland Sheepdog demonstrates the recessive sable fur trait. They also have the alleles for the recessive points trait.");
                    } else if (this.isSable()) {
                        if (this.carriesTan()) {
                            message = Component.literal("This Shetland Sheepdog demonstrates the recessive sable fur trait, and carries the points trait.");
                        } else {
                            message = Component.literal("This Shetland Sheepdog demonstrates the recessive sable fur trait.");
                        }
                    } else if (this.isTan()) {
                        if (this.carriesSable()) {
                            message = Component.literal("This Shetland Sheepdog demonstrates the recessive points trait, and carries the recessive sable fur trait.");
                        } else {
                            message = Component.literal("This Shetland Sheepdog demonstrates the recessive points trait.");
                        }
                    } else {
                        if (this.carriesSable() && this.carriesTan()) {
                            message = Component.literal("This Shetland Sheepdog carries two recessive traits.");
                        } else if (this.carriesTan()) {
                            message = Component.literal("This Shetland Sheepdog carries the recessive points trait.");
                        } else if (this.carriesSable()) {
                            message = Component.literal("This Shetland Sheepdog carries the recessive sable fur trait.");
                        } else {
                            message = Component.literal("This Shetland Sheepdog doesn't have any recessive traits.");
                        }
                    }

                    player.sendSystemMessage(message);

                    return InteractionResult.SUCCESS;
                }
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
        this.entityData.set(IS_SABLE, tag.getBoolean("IsSable"));
        this.entityData.set(CARRIES_SABLE, tag.getBoolean("CarriesSable"));
        this.entityData.set(IS_TAN, tag.getBoolean("IsTan"));
        this.entityData.set(CARRIES_TAN, tag.getBoolean("CarriesTan"));
        this.entityData.set(MERLE, tag.getBoolean("Merle"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsSable", this.isSable());
        tag.putBoolean("CarriesSable", this.carriesSable());
        tag.putBoolean("IsTan", this.isTan());
        tag.putBoolean("CarriesTan", this.carriesTan());
        tag.putBoolean("Merle", this.hasMerle());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_SABLE, true);
        this.entityData.define(CARRIES_SABLE, true);
        this.entityData.define(IS_TAN, false);
        this.entityData.define(CARRIES_TAN, false);
        this.entityData.define(MERLE, false);
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
        int determine = r.nextInt(9) + 1;
        int merle = r.nextInt(3) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (merle != 3) {
            setMerle(false);
            if (determine < 6) {
                var = 0; // SABLE
                setSableStatus(true, true);
                setTanStatus(carrier < 3, false);
            } else if (determine < 9) {
                var = 1; // BLACK
                setSableStatus(carrier < 3, false);
                setTanStatus(carrier == 3, false);
            } else {
                var = 2; //BLACK_TAN
                setSableStatus(carrier < 3, false);
                setTanStatus(true, true);
            }
        } else {
            setMerle(true);
            if (determine < 6) {
                var = 3; // SABLE_MERLE
                setSableStatus(true, true);
                setTanStatus(carrier < 3, false);
            } else if (determine < 9) {
                var = 4; // BLUE_MERLE (Black Merle)
                setSableStatus(carrier < 3, false);
                setTanStatus(carrier == 3, false);
            } else {
                var = 5; // BLACK_TAN_MERLE
                setSableStatus(carrier < 3, false);
                setTanStatus(true, true);
            }
        }

        // assign chosen variant and finish the method
        ShetlandSheepdogVariant variant = ShetlandSheepdogVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public ShetlandSheepdogVariant getVariant() {
        return ShetlandSheepdogVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(ShetlandSheepdogVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isSable() {
        return this.entityData.get(IS_SABLE);
    }

    public boolean carriesSable() {
        return this.entityData.get(CARRIES_SABLE);
    }

    private void setSableStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_SABLE, carrier);
        this.entityData.set(IS_SABLE, is);
    }

    public boolean isTan() {
        return this.entityData.get(IS_TAN);
    }

    public boolean carriesTan() {
        return this.entityData.get(IS_TAN);
    }

    private void setTanStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_TAN, carrier);
        this.entityData.set(IS_TAN, is);
    }

    public boolean hasMerle() {
        return this.entityData.get(MERLE);
    }

    private void setMerle(boolean has) {
        this.entityData.set(MERLE, has);
    }

    private void determineBabyVariant(ShetlandSheepdogEntity baby, ShetlandSheepdogEntity otherParent) {
        // determine if baby is sable or black
        if (this.isSable() && otherParent.isSable()) {
            // if both parents are sable, baby will be sable
            baby.setSableStatus(true, true);
        } else if ((this.isSable() && otherParent.carriesSable()) || (this.carriesSable() && otherParent.isSable())) {
            // if one parent is sable and one parent is a carrier, baby has 50% chance to be sable and 50%
            // chance to be a carrier
            baby.setSableStatus(true, this.random.nextBoolean());
        } else if (this.isSable() || otherParent.isSable()) {
            // if one parent is sable and the other isn't a carrier, baby will be a carrier
            baby.setSableStatus(true, false);
        } else if (this.carriesSable() && otherParent.carriesSable()) {
            // if both parents are carriers, baby has 25% not to carry, 50% chance to be a carrier, and 25%
            // chance to be sable
            int determine = this.random.nextInt(4) + 1;
            baby.setSableStatus(determine > 1, determine == 4);
        } else if (this.carriesSable() || otherParent.carriesSable()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setSableStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby won't be a carrier
            baby.setSableStatus(false, false);
        }

        // determine if baby is tan or not (tan creates Black_Tan variants and is hidden on Sables)
        if (this.isTan() && otherParent.isTan()) {
            // if both parents are tan, baby will be tan
            baby.setTanStatus(true, true);
        } else if ((this.isTan() && otherParent.carriesTan()) || (this.carriesTan() && otherParent.isTan())) {
            // if one parent is tan and the other is a carrier, baby has 50% chance to be tan and 50% chance to
            // be a carrier
            baby.setTanStatus(true, this.random.nextBoolean());
        } else if (this.isTan() || otherParent.isTan()) {
            // if one parent is tan and the other is not a carrier, baby will carry tan
            baby.setTanStatus(true, false);
        } else if (this.carriesTan() && otherParent.carriesTan()) {
            // if both parents carry tan, baby has 25% chance not to carry, 50% chance to be a carrier, and 25%
            // chance to be tan
            int determine = this.random.nextInt(4) + 1;
            baby.setTanStatus(determine > 1, determine == 4);
        } else if (this.carriesTan() || otherParent.carriesTan()) {
            // if only one parent carries tan, baby has 50/50 chance to be a carrier
            baby.setTanStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setTanStatus(false, false);
        }

        // determine if baby is merle or not
        if (this.hasMerle() && otherParent.hasMerle()) {
            // if both parents are merle, baby will be merle
            baby.setMerle(true);
        } else if (this.hasMerle() || otherParent.hasMerle()) {
            // if only one parent has merle, baby has 50/50 chance to be merle
            baby.setMerle(this.random.nextBoolean());
        } else {
            // if neither parent is merle, baby won't be merle
            baby.setMerle(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.hasMerle()) {
            if (baby.isSable()) {
                baby.setVariant(ShetlandSheepdogVariant.SABLE_MERLE);
            } else if (baby.isTan()) {
                baby.setVariant(ShetlandSheepdogVariant.BLACK_TAN_MERLE);
            } else {
                baby.setVariant(ShetlandSheepdogVariant.BLUE_MERLE);
            }
        } else {
            if (baby.isSable()) {
                baby.setVariant(ShetlandSheepdogVariant.SABLE);
            } else if (baby.isTan()) {
                baby.setVariant(ShetlandSheepdogVariant.BLACK_TAN);
            } else {
                baby.setVariant(ShetlandSheepdogVariant.BLACK);
            }
        }
    }
}