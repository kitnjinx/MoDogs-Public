package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.BorderCollieVariant;
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
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
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

public class BorderCollieEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CARRIES_RED =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_RED =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_LILAC =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MERLE =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BASE_COLOR =
            SynchedEntityData.defineId(BorderCollieEntity.class, EntityDataSerializers.INT);
    /* Explanation of Genetic Variables
    CARRIES_RED: true for Red-based dogs, black dogs carrying red, or lilac that carry red
    IS_RED: true for Red-based dogs and lilacs that would be red without dilution
    CARRIES_LILAC: true for Red or Black dogs that carry dilution, and true for lilac dogs
    MERLE: true for dogs with a _MERLE variant
    BASE_COLOR: Dogs TYPE_VARIANT without merle. 0 for Black or Black Merles, 1 for Red or Red Merles, and
        2 for Lilacs and Lilac Merles
     */

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.ZOMBIE_HORSE || entitytype == EntityType.ZOMBIE || entitytype == EntityType.ZOMBIE_VILLAGER;
    };

    public BorderCollieEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 18.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.ARMOR, 0.0f)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f).build();
    }

    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Zombie.class, false, PREY_SELECTOR));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, ZombieVillager.class, false, PREY_SELECTOR));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob otherParent) {
        BorderCollieEntity baby = ModEntityTypes.BORDER_COLLIE.get().create(serverLevel);

        determineBabyVariant(baby, (BorderCollieEntity) otherParent);

        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);
        
        return baby;
    }

    private <E extends BorderCollieEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.border_collie.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.border_collie.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.border_collie.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.border_collie.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.border_collie.idle"));
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
                Component part1;
                if (this.getBaseColor() == 2) {
                    if (this.isMerle()) {
                        part1 = Component.literal("This Border Collie demonstrates a recessive trait, as well as the merle trait.");
                    } else {
                        part1 = Component.literal("This Border Collie demonstrates a recessive trait.");
                    }

                    if (this.isRed()) {
                        message = Component.literal(part1.getString() + " They also have the alleles for red fur.");
                    } else if (this.getRedCarrier()) {
                        message = Component.literal(part1.getString() + " They also carry the red fur trait.");
                    } else {
                        message = Component.literal(part1.getString() + " They have otherwise standard traits.");
                    }
                } else if (this.getBaseColor() == 1) {
                    if (this.isMerle()) {
                        part1 = Component.literal("This Border Collie demonstrates a recessive trait, as well as the merle trait.");
                    } else {
                        part1 = Component.literal("This Border Collie demonstrates a recessive trait.");
                    }

                    if (this.getLilacCarrier()) {
                        message = Component.literal(part1.getString() + " They also carry the recessive dilution trait.");
                    } else {
                        message = part1;
                    }
                } else {
                    if (this.getRedCarrier() && this.getLilacCarrier()) {
                        part1 = Component.literal("This Border Collie carries two recessive traits.");
                    } else if (this.getRedCarrier()) {
                        part1 = Component.literal("This Border Collie carries the recessive red fur trait.");
                    } else if (this.getLilacCarrier()) {
                        part1 = Component.literal("This Border Collie carries the recessive dilution trait.");
                    } else {
                        part1 = Component.literal("This Border Collie doesn't have any recessive traits.");
                    }

                    if (this.isMerle()) {
                        message = Component.literal(part1.getString() + " They demonstrate the merle trait.");
                    } else {
                        message = part1;
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
        this.entityData.set(CARRIES_RED, tag.getBoolean("RedCarrier"));
        this.entityData.set(IS_RED, tag.getBoolean("IsRed"));
        this.entityData.set(CARRIES_LILAC, tag.getBoolean("LilacCarrier"));
        this.entityData.set(MERLE, tag.getBoolean("Merle"));
        this.entityData.set(BASE_COLOR, tag.getInt("BaseColor"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("RedCarrier", this.getRedCarrier());
        tag.putBoolean("IsRed", this.isRed());
        tag.putBoolean("LilacCarrier", this.getLilacCarrier());
        tag.putBoolean("Merle", this.isMerle());
        tag.putInt("BaseColor", this.getBaseColor());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(CARRIES_RED, false);
        this.entityData.define(IS_RED, false);
        this.entityData.define(CARRIES_LILAC, false);
        this.entityData.define(MERLE, false);
        this.entityData.define(BASE_COLOR, 0);
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
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
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

        /* Brief explanation of different variables:
        determine: Used to decide if the dog's phenotype (visible coat) is Black, Red, or Lilac
        merle: Used to determine if dog is merle or not
        carrier: Used to determine potential carrier status for recessive traits (Red and Lilac)
         */

        if (merle == 3) {
            setMerle(true);
            if (determine < 6) {
                var = 3; // BLACK_MERLE
                setBaseColor(0);
                setRedStatus(carrier == 1, false);
                setLilacCarrier(carrier == 2);
            } else if (determine < 9) {
                var = 4; // RED_MERLE
                setBaseColor(1);
                setRedStatus(true, true);
                setLilacCarrier(carrier == 1);
            } else {
                var = 5; // LILAC_MERLE
                setBaseColor(2);
                setLilacCarrier(true);
                if ((r.nextInt(8) + 1) < 6) {
                    setRedStatus(true, true);
                } else {
                    setRedStatus(carrier == 1, false);
                }
            }
        } else {
            setMerle(false);
            if (determine < 6) {
                var = 0; // BLACK
                setBaseColor(0);
                setRedStatus(carrier == 1, false);
                setLilacCarrier(carrier == 2);
            } else if (determine < 9) {
                var = 1; // RED
                setBaseColor(1);
                setRedStatus(true, true);
                setLilacCarrier(carrier == 1);
            } else {
                var = 2;
                setBaseColor(2); // LILAC
                setLilacCarrier(true);
                if ((r.nextInt(8) + 1) < 6) {
                    setRedStatus(true, true);
                } else {
                    setRedStatus(carrier == 1, false);
                }
            }
        }

        // assign chosen variant and finish the method
        BorderCollieVariant variant = BorderCollieVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public BorderCollieVariant getVariant() {
        return BorderCollieVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BorderCollieVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean getRedCarrier() {
        return this.entityData.get(CARRIES_RED);
    }

    public boolean isRed() {
        return this.entityData.get(IS_RED);
    }

    private void setRedStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_RED, carrier);
        this.entityData.set(IS_RED, is);
    }

    public boolean getLilacCarrier() {
        return this.entityData.get(CARRIES_LILAC);
    }

    private void setLilacCarrier(boolean status) {
        this.entityData.set(CARRIES_LILAC, status);
    }

    public boolean isMerle() {
        return this.entityData.get(MERLE);
    }

    private void setMerle(boolean status) {
        this.entityData.set(MERLE, status);
    }

    public int getBaseColor() {
        return this.entityData.get(BASE_COLOR);
    }

    private void setBaseColor(int base) {
        this.entityData.set(BASE_COLOR, base);
    }

    private void determineBabyVariant(BorderCollieEntity baby, BorderCollieEntity otherParent) {
        BorderCollieVariant babyVar;

        // determine the baby's base color (Black, Red, or Lilac)
        if (this.getBaseColor() == 0 && otherParent.getBaseColor() == 0) {
            // if both parents are black-based, run the following method
            determinePureBlackBaby(baby, otherParent);
        } else if (this.getBaseColor() == 0) {
            // if this parent is black-based, run a method depending on whether the otherParent is red or lilac
            if (otherParent.getBaseColor() == 1) {
                determineBlackRedBaby(baby, this, otherParent);
            } else {
                determineBlackLilacBaby(baby, this, otherParent);
            }
        } else if (otherParent.getBaseColor() == 0) {
            // if otherParent is black-based, run a method depending on whether this parent is red or lilac
            if (this.getBaseColor() == 1) {
                determineBlackRedBaby(baby, otherParent, this);
            } else {
                determineBlackLilacBaby(baby, otherParent, this);
            }
        } else if (this.getBaseColor() == 1 && otherParent.getBaseColor() == 1) {
            // if both parents are red-based, run the following method
            determinePureRedBaby(baby, otherParent);
        } else if (this.getBaseColor() == 1) {
            // if only this parent is red-based, run the following method
            determineRedLilacBaby(baby, this, otherParent);
        } else if (otherParent.getBaseColor() == 1) {
            // if only the otherParent is red-based, run the following method
            determineRedLilacBaby(baby, otherParent, this);
        } else {
            // if both parents are lilac-based, baby will be lilac-based and have hidden red/black genetics
            // that are determined by the following method
            determinePureLilacBaby(baby, otherParent);
        }

        // determine if the baby is merle or not
        if (this.isMerle() && otherParent.isMerle()) {
            // if both parents are merle, baby is merle
            baby.setMerle(true);
        } else if (this.isMerle() || otherParent.isMerle()) {
            // if one parent is merle, baby has 50/50 chance to be merle
            baby.setMerle(this.random.nextBoolean());
        } else {
            // if neither parent is merle, baby will not be merle
            baby.setMerle(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isMerle()) {
            if (baby.getBaseColor() == 0) {
                // if baby is black-based and merle, baby is Black Merle
                babyVar = BorderCollieVariant.BLACK_MERLE;
            } else if (baby.getBaseColor() == 1) {
                //if baby is red-based and merle, baby is Red Merle
                babyVar = BorderCollieVariant.RED_MERLE;
            } else {
                // if baby is lilac-based and merle, baby is Lilac Merle
                babyVar = BorderCollieVariant.LILAC_MERLE;
            }
        } else {
            if (baby.getBaseColor() == 0) {
                // if baby is black-based and not merle, baby is Black
                babyVar = BorderCollieVariant.BLACK;
            } else if (baby.getBaseColor() == 1) {
                //if baby is red-based and not merle, baby is Red
                babyVar = BorderCollieVariant.RED;
            } else {
                // if baby is lilac-based and not merle, baby is Lilac
                babyVar = BorderCollieVariant.LILAC;
            }
        }

        baby.setVariant(babyVar);
    }

    private void determinePureBlackBaby(BorderCollieEntity baby, BorderCollieEntity otherParent) {
        // determine if baby will be black or red
        if (this.getRedCarrier() && otherParent.getRedCarrier()) {
            // if both parents carry red, baby has 25% chance to be black and not carry red, 50% to be black
            // and carry red, and 25% to be red
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setRedStatus(false, false);
            } else {
                baby.setRedStatus(true, determine == 4);
            }
        } else if (this.getRedCarrier() || otherParent.getRedCarrier()) {
            // if only one parent carries red, baby will be black and has 50% chance to carry red
            baby.setRedStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent carries red, baby will be black and won't carry red
            baby.setRedStatus(false, false);
        }

        // determine if the baby will be lilac or not
        boolean isLilac;
        if (this.getLilacCarrier() && otherParent.getLilacCarrier()) {
            // if both parents carry lilac, baby has 25% chance to not carry lilac, 50% chance to carry
            // lilac, and 25% chance to be lilac
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setLilacCarrier(false);
                isLilac = false;
            } else if (determine < 4) {
                baby.setLilacCarrier(true);
                isLilac = false;
            } else {
                baby.setLilacCarrier(true);
                isLilac = true;
            }
        } else if (this.getLilacCarrier() || otherParent.getLilacCarrier()) {
            // if only one parent carries lilac, baby will have 50% chance to carry lilac
            isLilac = false;
            baby.setLilacCarrier(this.random.nextBoolean());
        } else {
            // if neither parent carries lilac, baby will not carry lilac
            isLilac = false;
            baby.setLilacCarrier(false);
        }

        // determine the baby's actual base color based on it's genetics
        if (isLilac) {
            baby.setBaseColor(2);
        } else if (baby.isRed()) {
            baby.setBaseColor(1);
        } else {
            baby.setBaseColor(0);
        }
    }

    // method used when a black dog is bred to a red dog, where parA is black-based and parB is red-based
    private void determineBlackRedBaby(BorderCollieEntity baby, BorderCollieEntity parA, BorderCollieEntity parB) {
        // determine if the baby is black or red-based
        if (parA.getRedCarrier()) {
            // if parA carries red, baby has 50% chance to be black and carry red and 50% chance to be red
            baby.setRedStatus(true, this.random.nextBoolean());
        } else {
            // if parA does not carry red, baby will be black and carry red
            baby.setRedStatus(true, false);
        }

        // determine if the baby will be lilac or not
        boolean isLilac;
        if (parA.getLilacCarrier() && parB.getLilacCarrier()) {
            // if both parents carry lilac, baby has 25% chance to not carry lilac, 50% chance to carry
            // lilac, and 25% chance to be lilac
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setLilacCarrier(false);
                isLilac = false;
            } else if (determine < 4) {
                baby.setLilacCarrier(true);
                isLilac = false;
            } else {
                baby.setLilacCarrier(true);
                isLilac = true;
            }
        } else if (parA.getLilacCarrier() || parB.getLilacCarrier()) {
            // if only one parent carries lilac, baby will have 50% chance to carry lilac
            isLilac = false;
            baby.setLilacCarrier(this.random.nextBoolean());
        } else {
            // if neither parent carries lilac, baby will not carry lilac
            isLilac = false;
            baby.setLilacCarrier(false);
        }

        // determine the baby's actual base color based on it's genetics
        if (isLilac) {
            baby.setBaseColor(2);
        } else if (baby.isRed()) {
            baby.setBaseColor(1);
        } else {
            baby.setBaseColor(0);
        }
    }

    // method used when a black dog is bred to a lilac dog, where parA is black-based and parB is lilac-based
    private void determineBlackLilacBaby(BorderCollieEntity baby, BorderCollieEntity parA, BorderCollieEntity parB) {
        // determine if the baby is black or red
        if (parB.isRed()) {
            // if the lilac parent would be red, then check the black parent's genetics
            if (parA.getRedCarrier()) {
                // if parA carries red, baby has 50% chance to be black and carry red and 50% chance to be red
                baby.setRedStatus(true, this.random.nextBoolean());
            } else {
                // if parA does not carry red, baby will be black and carry red
                baby.setRedStatus(true, false);
            }
        } else if (parB.getRedCarrier()) {
            // if the lilac parent carries red, then check the black parent's genetics
            if (parA.getRedCarrier()) {
                // if both parents carry red, baby has 25% chance to be black and not carry red, 50% chance to
                // be black and carry red, and 25% chance to be red
                int determine = this.random.nextInt(4) + 1;
                if (determine == 1) {
                    baby.setRedStatus(false, false);
                } else {
                    baby.setRedStatus(true, determine == 4);
                }
            } else {
                // if parA is not a red carrier, baby will be black and has 50% chance to carry red
                baby.setRedStatus(this.random.nextBoolean(), false);
            }
        } else {
            // if the lilac parent doesn't carry red, then check the black parent's genetics
            if (parA.getRedCarrier()) {
                // if parA carries red, baby will be black and have 50% chance to carry red
                baby.setRedStatus(this.random.nextBoolean(), false);
            } else {
                // if neither parent carries red, baby will be black and won't carry red
                baby.setRedStatus(false, false);
            }
        }

        // determine if the baby is lilac or not. As the baby has a lilac parent, they will always carry lilac
        baby.setLilacCarrier(true);
        boolean isLilac;
        if (parA.getLilacCarrier()) {
            // if parA is a carrier, baby will have 50% chance to be lilac
            isLilac = this.random.nextBoolean();
        } else {
            // if parA is not a carrier, baby will not be lilac
            isLilac = false;
        }

        // determine the baby's base color
        if (isLilac) {
            baby.setBaseColor(2);
        } else if (baby.isRed()) {
            baby.setBaseColor(1);
        } else {
            baby.setBaseColor(0);
        }
    }

    private void determinePureRedBaby(BorderCollieEntity baby, BorderCollieEntity otherParent) {
        // with two red parents, baby will always carry and be red
        baby.setRedStatus(true, true);

        // determine if baby is lilac, a lilac carrier, or neither
        boolean isLilac;
        if (this.getLilacCarrier() && otherParent.getLilacCarrier()) {
            // if both parents are lilac carriers, baby has 25% chance not to carry lilac, 50% to carry lilac,
            // and 25% chance to be lilac
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setLilacCarrier(false);
                isLilac = false;
            } else if (determine < 4) {
                baby.setLilacCarrier(true);
                isLilac = false;
            } else {
                baby.setLilacCarrier(true);
                isLilac = true;
            }
        } else if (this.getLilacCarrier() || otherParent.getLilacCarrier()) {
            // if only one parent is a lilac carrier, baby has 50% to be a lilac carrier
            isLilac = false;
            baby.setLilacCarrier(this.random.nextBoolean());
        } else {
            // if neither parent is a lilac carrier, baby will not be a lilac carrier
            isLilac = false;
            baby.setLilacCarrier(false);
        }

        // determine if baby is red or lilac for its base color
        if (isLilac) {
            baby.setBaseColor(2);
        } else {
            baby.setBaseColor(1);
        }
    }

    // method used when a red dog is bred to a lilac dog, where parA is red-based and parB is lilac-based
    private void determineRedLilacBaby(BorderCollieEntity baby, BorderCollieEntity parA, BorderCollieEntity parB) {
        // determine if baby is red or black-based
        if (parB.isRed()) {
            // if lilac parent is red-based, baby will be red
            baby.setRedStatus(true, true);
        } else if (parB.getRedCarrier()) {
            // if lilac parent is a red carrier, baby will have 50% chance to be a red carrier and 50% chance to
            // be red
            baby.setRedStatus(true, this.random.nextBoolean());
        } else {
            // if lilac parent is not a red carrier, baby will be black and carry red
            baby.setRedStatus(true, false);
        }

        // determine if baby is lilac or only a carrier
        boolean isLilac;
        baby.setLilacCarrier(true);
        if (parA.getLilacCarrier()) {
            // if parA is a lilac carrier, baby will have a 50/50 chance of being lilac
            isLilac = this.random.nextBoolean();
        } else {
            // if parA is not a lilac carrier, baby will not be lilac
            isLilac = false;
        }

        // determine baby's base color
        if (isLilac) {
            baby.setBaseColor(2);
        } else if (baby.isRed()) {
            baby.setBaseColor(1);
        } else {
            baby.setBaseColor(0);
        }
    }

    private void determinePureLilacBaby(BorderCollieEntity baby, BorderCollieEntity otherParent) {
        // as both parents are lilac, set baby to be lilac
        baby.setLilacCarrier(true);
        baby.setBaseColor(2);

        // determine baby's hidden red/black genetics
        if (this.isRed() && otherParent.isRed()) {
            // if both parents are red, baby will be red
            baby.setRedStatus(true, true);
        } else if ((this.isRed() && otherParent.getRedCarrier()) || (this.getRedCarrier() && otherParent.isRed())) {
            // if one parent is red and the other is a carrier, baby has 50% to be black and a red carrier and
            // 50% chance to be red
            baby.setRedStatus(true, this.random.nextBoolean());
        } else if (this.isRed() || otherParent.isRed()) {
            // if one parent is red and the other is not a carrier, baby will be a red carrier
            baby.setRedStatus(true, false);
        } else if (this.getRedCarrier() && otherParent.getRedCarrier()) {
            // if both parents are red carriers, baby will have 25% chance not to carry, 50% chance to be a red
            // carrier, and 25% chance to be red
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setRedStatus(false, false);
            } else {
                baby.setRedStatus(true, determine == 4);
            }
        } else if (this.getRedCarrier() || otherParent.getRedCarrier()) {
            // if only one parent is a red carrier, baby will be black and have 50% chance to carry red
            baby.setRedStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent carries red, baby will be black and won't carry red
            baby.setRedStatus(false, false);
        }
    }
}