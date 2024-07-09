package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.BasenjiVariant;
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
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;
import java.util.function.Predicate;

public class BasenjiEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BasenjiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CARRIER =
            SynchedEntityData.defineId(BasenjiEntity.class, EntityDataSerializers.INT);
    // CARRIER values explanation: 0 = carries nothing, 1 = carries Black, 2 = carries Tricolor

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.PIG || entitytype == EntityType.CHICKEN;
    };

    public BasenjiEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        BasenjiEntity baby = ModEntityTypes.BASENJI.get().create(serverLevel);

        determineBabyVariant(baby, (BasenjiEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.basenji.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.basenji.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.basenji.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.basenji.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.basenji.idle", Animation.LoopType.LOOP));
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

    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.75F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.25F;
    }

    /* Tamable */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        Item itemForTaming = ModItems.BACON_TREAT.get();
        Item itemForTaming2 = ModItems.CHICKEN_TREAT.get();

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
                if (this.getVariant() == BasenjiVariant.TRICOLOR) {
                    message = Component.literal("This Basenji demonstrates a recessive trait.");
                } else if (this.getVariant() == BasenjiVariant.BLACK) {
                    if (this.getCarrier() == 0) {
                        message = Component.literal("This Basenji demonstrates a trait that can be recessive.");
                    } else {
                        message = Component.literal("This Basenji demonstrates a trait that can be recessive, however they also carry the more recessive tricolor fur trait.");
                    }
                } else {
                    if (this.getCarrier() == 0) {
                        message = Component.literal("This Basenji doesn't have any recessive traits.");
                    } else if (this.getCarrier() == 1) {
                        message = Component.literal("This Basenji carries a recessive trait for black fur.");
                    } else {
                        message = Component.literal("This Basenji carries a recessive trait for tricolor fur.");
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
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5D);
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
        int determine = r.nextInt(9) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 6) {
            var = 0; // RED
            if (carrier < 3) {
                setCarrier(carrier);
            } else {
                setCarrier(0);
            }
        } else if (determine < 9) {
            var = 1; // BLACK
            if (carrier < 3) {
                setCarrier(2);
            } else {
                setCarrier(0);
            }
        } else {
            var = 2; // TRICOLOR
            setCarrier(0);
        }

        // assign chosen variant and finish the method
        BasenjiVariant variant = BasenjiVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public BasenjiVariant getVariant() {
        return BasenjiVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BasenjiVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }
    
    public int getCarrier() {
        return this.entityData.get(CARRIER);
    }
    
    private void setCarrier(int carrier) {
        this.entityData.set(CARRIER, carrier);
    }

    private void determineBabyVariant(BasenjiEntity baby, BasenjiEntity otherParent) {
        if (this.getVariant() == BasenjiVariant.RED && otherParent.getVariant() == BasenjiVariant.RED) {
            determinePureRedBaby(baby, otherParent);
        } else if (this.getVariant() == BasenjiVariant.RED || otherParent.getVariant() == BasenjiVariant.RED) {
            if (this.getVariant() == BasenjiVariant.RED) {
                determineRedBaby(this, otherParent, baby);
            } else {
                determineRedBaby(otherParent, this, baby);
            }
        } else if (this.getVariant() == BasenjiVariant.BLACK && otherParent.getVariant() == BasenjiVariant.BLACK) {
            determinePureBlackBaby(baby, otherParent);
        } else if (this.getVariant() == BasenjiVariant.BLACK || otherParent.getVariant() == BasenjiVariant.BLACK) {
            if (this.getVariant() == BasenjiVariant.BLACK) {
                determineBlackBaby(this, baby);
            } else {
                determineBlackBaby(otherParent, baby);
            }
        } else {
            // if both parents are Liver_Tan, pup will carry nothing and be liver_tan
            baby.setCarrier(0);
            baby.setVariant(BasenjiVariant.TRICOLOR);
        }
    }

    // method used with two Red variant parents
    private void determinePureRedBaby(BasenjiEntity baby, BasenjiEntity otherParent) {
        int parACarry = this.getCarrier();
        int parBCarry = otherParent.getCarrier();

        int determine = this.random.nextInt(4) + 1;

        if (parACarry == 0 && parBCarry == 0) {
            // if neither parent carries another trait, pup will be red and carry nothing
            baby.setCarrier(0);
            baby.setVariant(BasenjiVariant.RED);
        } else if (parACarry == parBCarry) {
            // if both parents carry the same trait, pup has 25% to be red and carry nothing, 50% to be red and
            // carry the same trait as their parents, and 25% to be the recessive trait with nothing carried
            if (determine == 1) {
                baby.setCarrier(0);
                baby.setVariant(BasenjiVariant.RED);
            } else if (determine < 4) {
                baby.setCarrier(parACarry);
                baby.setVariant(BasenjiVariant.RED);
            } else {
                baby.setCarrier(0);
                baby.setVariant(BasenjiVariant.byId(parACarry));
            }
        } else if (parACarry == 0) {
            // if one parent carries a trait and the other doesn't, pup will be red and has a 50/50 chance of
            // carrying the other trait
            baby.setVariant(BasenjiVariant.RED);
            if (this.random.nextBoolean()) {
                baby.setCarrier(0);
            } else {
                baby.setCarrier(parBCarry);
            }
        } else if (parBCarry == 0) {
            // if one parent carries a trait and the other doesn't, pup will be red and has a 50/50 chance of
            // carrying the other trait
            baby.setVariant(BasenjiVariant.RED);
            if (this.random.nextBoolean()) {
                baby.setCarrier(0);
            } else {
                baby.setCarrier(parACarry);
            }
        } else {
            // if one parent carries Black and the other carries Tricolor, pup has 25% to be red with no
            // carried trait, 25% chance to be red and carry black, 25% chance to be red and carry tricolor,
            // and 25% chance to be black and carry tricolor
            if (determine == 1) {
                baby.setCarrier(0);
                baby.setVariant(BasenjiVariant.RED);
            } else if (determine == 2) {
                baby.setCarrier(1);
                baby.setVariant(BasenjiVariant.RED);
            } else if (determine == 3) {
                baby.setCarrier(2);
                baby.setVariant(BasenjiVariant.RED);
            } else {
                baby.setCarrier(2);
                baby.setVariant(BasenjiVariant.BLACK);
            }
        }
    }

    // method used with one Red variant parent, where parA is the Red parent
    private void determineRedBaby(BasenjiEntity parA, BasenjiEntity parB, BasenjiEntity baby) {
        int parACarry = parA.getCarrier();
        int parBCarry = parB.getCarrier();

        int determine = this.random.nextInt(4) + 1;

        if (parACarry == 0) {
            // if Red parent has no carried trait, baby will be Red and carry based on parB
            baby.setVariant(BasenjiVariant.RED);
            if (parB.getVariant() == BasenjiVariant.BLACK && parBCarry == 0) {
                // if parB is Black and doesn't carry Tricolor, baby will carry Black
                baby.setCarrier(1);
            } else if (parB.getVariant() == BasenjiVariant.BLACK) {
                // if parB is Black and carries Tricolor, baby has 50/50 chance to carry either trait
                if (this.random.nextBoolean()) {
                    baby.setCarrier(1);
                } else {
                    baby.setCarrier(2);
                }
            } else {
                // if parB is Tricolor, baby will carry Tricolor
                baby.setCarrier(2);
            }
        } else if (parACarry == 1 && parB.getVariant() == BasenjiVariant.BLACK) {
            // if parA carries Black and parB is Black, baby has 50/50 chance of being Red or Black
            if (parBCarry == 0) {
                // if parB doesn't carry Tricolor, baby will either be Black with no carried trait or Red
                // and carrying Black
                if (this.random.nextBoolean()) {
                    baby.setCarrier(0);
                    baby.setVariant(BasenjiVariant.BLACK);
                } else {
                    baby.setCarrier(1);
                    baby.setVariant(BasenjiVariant.RED);
                }
            } else {
                // if parB carries Tricolor, baby has 25% chance of being red and carrying Black, 25%
                // chance of Red and carrying Tricolor, 25% chance of being Black with no carried trait, and 25%
                // chance of Black and carrying Tricolor
                if (determine == 1) {
                    baby.setCarrier(1);
                    baby.setVariant(BasenjiVariant.RED);
                } else if (determine == 2) {
                    baby.setCarrier(2);
                    baby.setVariant(BasenjiVariant.RED);
                } else if (determine == 3) {
                    baby.setCarrier(0);
                    baby.setVariant(BasenjiVariant.BLACK);
                } else {
                    baby.setCarrier(2);
                    baby.setVariant(BasenjiVariant.BLACK);
                }
            }
        } else if (parACarry == 1 && parB.getVariant() == BasenjiVariant.TRICOLOR) {
            // if parA carries Black and parB is Tricolor, baby will carry Tricolor and have a 50/50
            // chance of being Red or Black
            baby.setCarrier(2);
            if (this.random.nextBoolean()) {
                baby.setVariant(BasenjiVariant.RED);
            } else {
                baby.setVariant(BasenjiVariant.BLACK);
            }
        } else if (parACarry == 2 && parB.getVariant() == BasenjiVariant.BLACK) {
            // if parA carries Tricolor and parB is Black, the baby depends on parB's carrier status
            if (parBCarry == 0) {
                // if parB does not carry Tricolor, baby has 50% chance to be Red and carry Black and a
                // 50% chance to be Black and carry Tricolor
                if (this.random.nextBoolean()) {
                    baby.setCarrier(1);
                    baby.setVariant(BasenjiVariant.RED);
                } else {
                    baby.setCarrier(2);
                    baby.setVariant(BasenjiVariant.BLACK);
                }
            } else {
                // if parB carries Tricolor, baby has 25% chance to be red and carry Black, 25% chance to
                // be red and carry Tricolor, 25% chance to be Black and carry Tricolor, and 25% chance
                // to be Tricolor with no carried trait
                if (determine == 1) {
                    baby.setCarrier(1);
                    baby.setVariant(BasenjiVariant.RED);
                } else if (determine == 2) {
                    baby.setCarrier(2);
                    baby.setVariant(BasenjiVariant.RED);
                } else if (determine == 3) {
                    baby.setCarrier(2);
                    baby.setVariant(BasenjiVariant.BLACK);
                } else {
                    baby.setCarrier(0);
                    baby.setVariant(BasenjiVariant.TRICOLOR);
                }
            }
        } else {
            // if parA carries Tricolor and parB is Tricolor, baby has 50% chance to be red and carry Tri and
            // 50% chance to be Tricolor with no carried trait
            if (this.random.nextBoolean()) {
                baby.setCarrier(2);
                baby.setVariant(BasenjiVariant.RED);
            } else {
                baby.setCarrier(0);
                baby.setVariant(BasenjiVariant.TRICOLOR);
            }
        }
    }

    // method used with two Black parents
    private void determinePureBlackBaby(BasenjiEntity baby, BasenjiEntity otherParent) {
        if (this.getCarrier() == 0 && otherParent.getCarrier() == 0) {
            // if neither parent carries Tricolor, baby will be Black and carry nothing
            baby.setCarrier(0);
            baby.setVariant(BasenjiVariant.BLACK);
        } else if (this.getCarrier() == 0 || otherParent.getCarrier() == 0) {
            // if one parent carries Tricolor, baby will be Black and have 50% chance to carry Tricolor
            baby.setVariant(BasenjiVariant.BLACK);
            if (this.random.nextBoolean()) {
                baby.setCarrier(2);
            } else {
                baby.setCarrier(0);
            }
        } else {
            // if both parents carry Tricolor, baby has 25% chance to be Black and carry nothing, 50%
            // chance to be Black and carry Tricolor, and 25% chance to be Tricolor and carry nothing
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setCarrier(0);
                baby.setVariant(BasenjiVariant.BLACK);
            } else if (determine < 4) {
                baby.setCarrier(2);
                baby.setVariant(BasenjiVariant.BLACK);
            } else {
                baby.setCarrier(0);
                baby.setVariant(BasenjiVariant.TRICOLOR);
            }
        }
    }

    // method used with one Black variant parent, where parA is the Black parent
    private void determineBlackBaby(BasenjiEntity parA, BasenjiEntity baby) {
        if (parA.getCarrier() == 0) {
            // if parA does not carry Tricolor, then the baby will be Black and carry Tricolor
            baby.setCarrier(2);
            baby.setVariant(BasenjiVariant.BLACK);
        } else {
            // if parA carries Tricolor, baby has 50% chance to be Black and carry Tricolor and 50% chance
            // to be Tricolor with no carried trait
            if (this.random.nextBoolean()) {
                baby.setCarrier(2);
                baby.setVariant(BasenjiVariant.BLACK);
            } else {
                baby.setCarrier(0);
                baby.setVariant(BasenjiVariant.TRICOLOR);
            }
        }
    }
}