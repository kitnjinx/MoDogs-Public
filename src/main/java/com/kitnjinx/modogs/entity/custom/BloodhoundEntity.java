package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.BloodhoundVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
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

public class BloodhoundEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BloodhoundEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CARRIER =
            SynchedEntityData.defineId(BloodhoundEntity.class, EntityDataSerializers.INT);
    // CARRIER values explanation: 0 = carries nothing, 1 = carries Black_Tan, 2 = carries Liver_Tan

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.SHEEP;
    };

    public BloodhoundEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        BloodhoundEntity baby = ModEntityTypes.BLOODHOUND.get().create(serverLevel);

        determineBabyVariant(baby, (BloodhoundEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends BloodhoundEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bloodhound.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bloodhound.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bloodhound.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bloodhound.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bloodhound.idle"));
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
                TextComponent message;
                if (this.getVariant() == BloodhoundVariant.LIVER_TAN) {
                    message = new TextComponent("This Bloodhound demonstrates a recessive trait.");
                } else if (this.getVariant() == BloodhoundVariant.BLACK_TAN) {
                    if (this.getCarrier() == 0) {
                        message = new TextComponent("This Bloodhound demonstrates a trait that can be recessive.");
                    } else {
                        message = new TextComponent("This Bloodhound demonstrates a trait that can be recessive, however they also carry the more recessive liver and tan fur trait.");
                    }
                } else {
                    if (this.getCarrier() == 0) {
                        message = new TextComponent("This Bloodhound doesn't have any recessive traits.");
                    } else if (this.getCarrier() == 1) {
                        message = new TextComponent("This Bloodhound carries a recessive trait for black and tan fur.");
                    } else {
                        message = new TextComponent("This Bloodhound carries a recessive trait for liver and tan fur.");
                    }
                }

                player.sendMessage(message, player.getUUID());

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
        this.entityData.set(CARRIER, tag.getInt("Carrier"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("Carrier", this.getCarrier());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(CARRIER, 0);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(22.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.45f);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3.0);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(2.0);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group,
                                        @Nullable CompoundTag tag) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(9) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 6) {
            var = 0; // BLACK_TAN
            if (carrier < 3) {
                setCarrier(2);
            } else {
                setCarrier(0);
            }
        } else if (determine < 9) {
            var = 1; // LIVER_TAN
            setCarrier(0);
        } else {
            var = 2; // RED
            if (carrier == 1) {
                setCarrier(1);
            } else if (carrier == 2) {
                setCarrier(2);
            } else {
                setCarrier(0);
            }
        }

        // assign chosen variant and finish the method
        BloodhoundVariant variant = BloodhoundVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public BloodhoundVariant getVariant() {
        return BloodhoundVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BloodhoundVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public int getCarrier() {
        return this.entityData.get(CARRIER);
    }

    private void setCarrier(int status) {
        this.entityData.set(CARRIER, status);
    }

    private void determineBabyVariant(BloodhoundEntity baby, BloodhoundEntity otherParent) {
        if (this.getVariant() == BloodhoundVariant.RED && otherParent.getVariant() == BloodhoundVariant.RED) {
            determinePureRedBaby(baby, otherParent);
        } else if (this.getVariant() == BloodhoundVariant.RED || otherParent.getVariant() == BloodhoundVariant.RED) {
            if (this.getVariant() == BloodhoundVariant.RED) {
                determineRedBaby(this, otherParent, baby);
            } else {
                determineRedBaby(otherParent, this, baby);
            }
        } else if (this.getVariant() == BloodhoundVariant.BLACK_TAN && otherParent.getVariant() == BloodhoundVariant.BLACK_TAN) {
            determinePureBTBaby(baby, otherParent);
        } else if (this.getVariant() == BloodhoundVariant.BLACK_TAN || otherParent.getVariant() == BloodhoundVariant.BLACK_TAN) {
            if (this.getVariant() == BloodhoundVariant.BLACK_TAN) {
                determineBTBaby(this, baby);
            } else {
                determineBTBaby(otherParent,  baby);
            }
        } else {
            // if both parents are Liver_Tan, pup will carry nothing and be liver_tan
            baby.setCarrier(0);
            baby.setVariant(BloodhoundVariant.LIVER_TAN);
        }
    }

    // method used with two Red variant parents
    private void determinePureRedBaby(BloodhoundEntity baby, BloodhoundEntity otherParent) {
        int parACarry = this.getCarrier();
        int parBCarry = otherParent.getCarrier();

        int determine = this.random.nextInt(4) + 1;

        if (parACarry == 0 && parBCarry == 0) {
            // if neither parent carries another trait, pup will be red and carry nothing
            baby.setCarrier(0);
            baby.setVariant(BloodhoundVariant.RED);
        } else if (parACarry == parBCarry) {
            // if both parents carry the same trait, pup has 25% to be red and carry nothing, 50% to be red and
            // carry the same trait as their parents, and 25% to be the recessive trait with nothing carried
            if (determine == 1) {
                baby.setCarrier(0);
                baby.setVariant(BloodhoundVariant.RED);
            } else if (determine < 4) {
                baby.setCarrier(parACarry);
                baby.setVariant(BloodhoundVariant.RED);
            } else {
                baby.setCarrier(0);
                baby.setVariant(BloodhoundVariant.byId(parACarry - 1));
            }
        } else if (parACarry == 0) {
            // if one parent carries a trait and the other doesn't, pup will be red and has a 50/50 chance of
            // carrying the other trait
            baby.setVariant(BloodhoundVariant.RED);
            if (this.random.nextBoolean()) {
                baby.setCarrier(0);
            } else {
                baby.setCarrier(parBCarry);
            }
        } else if (parBCarry == 0) {
            // if one parent carries a trait and the other doesn't, pup will be red and has a 50/50 chance of
            // carrying the other trait
            baby.setVariant(BloodhoundVariant.RED);
            if (this.random.nextBoolean()) {
                baby.setCarrier(0);
            } else {
                baby.setCarrier(parACarry);
            }
        } else {
            // if one parent carries Black_Tan and the other carries Liver_Tan, pup has 25% to be red with no
            // carried trait, 25% chance to be red and carry Black_Tan, 25% chance to be red and carry liver tan,
            // and 25% chance to be black tan and carry liver tan
            if (determine == 1) {
                baby.setCarrier(0);
                baby.setVariant(BloodhoundVariant.RED);
            } else if (determine == 2) {
                baby.setCarrier(1);
                baby.setVariant(BloodhoundVariant.RED);
            } else if (determine == 3) {
                baby.setCarrier(2);
                baby.setVariant(BloodhoundVariant.RED);
            } else {
                baby.setCarrier(2);
                baby.setVariant(BloodhoundVariant.BLACK_TAN);
            }
        }
    }

    // method used with one Red variant parent, where parA is the Red parent
    private void determineRedBaby(BloodhoundEntity parA, BloodhoundEntity parB, BloodhoundEntity baby) {
        int parACarry = parA.getCarrier();
        int parBCarry = parB.getCarrier();
        int determine = this.random.nextInt(4) + 1;

        if (parACarry == 0) {
            // if Red parent has no carried trait, baby will be Red and carry based on parB
            baby.setVariant(BloodhoundVariant.RED);
            if (parB.getVariant() == BloodhoundVariant.BLACK_TAN && parBCarry == 0) {
                // if parB is Black_Tan and doesn't carry Liver_Tan, baby will carry Black_Tan
                baby.setCarrier(1);
            } else if (parB.getVariant() == BloodhoundVariant.BLACK_TAN) {
                // if parB is Black_Tan and carries Liver_Tan, baby has 50/50 chance to carry either trait
                if (this.random.nextBoolean()) {
                    baby.setCarrier(1);
                } else {
                    baby.setCarrier(2);
                }
            } else {
                // if parB is Liver_Tan, baby will carry Liver_Tan
                baby.setCarrier(2);
            }
        } else if (parACarry == 1 && parB.getVariant() == BloodhoundVariant.BLACK_TAN) {
            // if parA carries Black_Tan and parB is Black_Tan, baby has 50/50 chance of being Red or Black_Tan
            if (parBCarry == 0) {
                // if parB doesn't carry Liver_Tan, baby will either be Black_Tan with no carried trait or Red
                // and carrying Black_Tan
                if (this.random.nextBoolean()) {
                    baby.setCarrier(0);
                    baby.setVariant(BloodhoundVariant.BLACK_TAN);
                } else {
                    baby.setCarrier(1);
                    baby.setVariant(BloodhoundVariant.RED);
                }
            } else {
                // if parB carries Liver_Tan, baby has 25% chance of being red and carrying Black_Tan, 25%
                // chance of Red and carrying Liver_Tan, 25% chance of being BT with no carried trait, and 25%
                // chance of BT and carrying LT
                if (determine == 1) {
                    baby.setCarrier(1);
                    baby.setVariant(BloodhoundVariant.RED);
                } else if (determine == 2) {
                    baby.setCarrier(2);
                    baby.setVariant(BloodhoundVariant.RED);
                } else if (determine == 3) {
                    baby.setCarrier(0);
                    baby.setVariant(BloodhoundVariant.BLACK_TAN);
                } else {
                    baby.setCarrier(2);
                    baby.setVariant(BloodhoundVariant.BLACK_TAN);
                }
            }
        } else if (parACarry == 1 && parB.getVariant() == BloodhoundVariant.LIVER_TAN) {
            // if parA carries Black_Tan and parB is Liver_Tan, baby will carry Liver_Tan and have a 50/50
            // chance of being Red or Black_Tan
            baby.setCarrier(2);
            if (this.random.nextBoolean()) {
                baby.setVariant(BloodhoundVariant.RED);
            } else {
                baby.setVariant(BloodhoundVariant.BLACK_TAN);
            }
        } else if (parACarry == 2 && parB.getVariant() == BloodhoundVariant.BLACK_TAN) {
            // if parA carries Liver_Tan and parB is Black_Tan, the baby depends on parB's carrier status
            if (parBCarry == 0) {
                // if parB does not carry Liver_Tan, baby has 50% chance to be Red and carry Black_Tan and a
                // 50% chance to be Black_Tan and carry Liver_Tan
                if (this.random.nextBoolean()) {
                    baby.setCarrier(1);
                    baby.setVariant(BloodhoundVariant.RED);
                } else {
                    baby.setCarrier(2);
                    baby.setVariant(BloodhoundVariant.BLACK_TAN);
                }
            } else {
                // if parB carries Liver_Tan, baby has 25% chance to be red and carry Black_Tan, 25% chance to
                // be red and carry Liver_Tan, 25% chance to be Black_Tan and carry Liver_Tan, and 25% chance
                // to be Liver_Tan with no carried trait
                if (determine == 1) {
                    baby.setCarrier(1);
                    baby.setVariant(BloodhoundVariant.RED);
                } else if (determine == 2) {
                    baby.setCarrier(2);
                    baby.setVariant(BloodhoundVariant.RED);
                } else if (determine == 3) {
                    baby.setCarrier(2);
                    baby.setVariant(BloodhoundVariant.BLACK_TAN);
                } else {
                    baby.setCarrier(0);
                    baby.setVariant(BloodhoundVariant.LIVER_TAN);
                }
            }
        } else {
            // if parA carries Liver_Tan and parB is Liver_Tan, baby has 50% chance to be red and carry LT and
            // 50% chance to be Liver_Tan with no carried trait
            if (this.random.nextBoolean()) {
                baby.setCarrier(2);
                baby.setVariant(BloodhoundVariant.RED);
            } else {
                baby.setCarrier(0);
                baby.setVariant(BloodhoundVariant.LIVER_TAN);
            }
        }
    }
    
    // method used with two Black_Tan parents
    private void determinePureBTBaby(BloodhoundEntity baby, BloodhoundEntity otherParent) {
        if (this.getCarrier() == 0 && otherParent.getCarrier() == 0) {
            // if neither parent carries Liver_Tan, baby will be Black_Tan and carry nothing
            baby.setCarrier(0);
            baby.setVariant(BloodhoundVariant.BLACK_TAN);
        } else if (this.getCarrier() == 0 || otherParent.getCarrier() == 0) {
            // if one parent carries Liver_Tan, baby will be Black_Tan and have 50% chance to carry Liver_Tan
            baby.setVariant(BloodhoundVariant.BLACK_TAN);
            if (this.random.nextBoolean()) {
                baby.setCarrier(2);
            } else {
                baby.setCarrier(0);
            }
        } else {
            // if both parents carry Liver_Tan, baby has 25% chance to be Black_Tan and carry nothing, 50%
            // chance to be Black_Tan and carry Liver_Tan, and 25% chance to be Liver_Tan and carry nothing
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setCarrier(0);
                baby.setVariant(BloodhoundVariant.BLACK_TAN);
            } else if (determine < 4) {
                baby.setCarrier(2);
                baby.setVariant(BloodhoundVariant.BLACK_TAN);
            } else {
                baby.setCarrier(0);
                baby.setVariant(BloodhoundVariant.LIVER_TAN);
            }
        }
    }

    // method used with one Black_Tan variant parent, where parA is the Black_Tan parent
    private void determineBTBaby(BloodhoundEntity parA, BloodhoundEntity baby) {
        if (parA.getCarrier() == 0) {
            // if parA does not carry Liver_Tan, then the baby will be Black_Tan and carry Liver_Tan
            baby.setCarrier(2);
            baby.setVariant(BloodhoundVariant.BLACK_TAN);
        } else {
            // if parA carries Liver_Tan, baby has 50% chance to be Black_Tan and carry Liver_Tan and 50% chance
            // to be Liver_Tan with no carried trait
            if (this.random.nextBoolean()) {
                baby.setCarrier(2);
                baby.setVariant(BloodhoundVariant.BLACK_TAN);
            } else {
                baby.setCarrier(0);
                baby.setVariant(BloodhoundVariant.LIVER_TAN);
            }
        }
    }
}