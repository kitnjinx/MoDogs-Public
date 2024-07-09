package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.PembrokeCorgiVariant;
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

public class PembrokeCorgiEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(PembrokeCorgiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BASE_COLOR =
            SynchedEntityData.defineId(PembrokeCorgiEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_FAWN =
            SynchedEntityData.defineId(PembrokeCorgiEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CARRIED_COLOR =
            SynchedEntityData.defineId(PembrokeCorgiEntity.class, EntityDataSerializers.INT);
    // CARRIED_COLOR + BASE_COLOR Value meanings: 0 = Red (or Fawn), 1 = Sable, 2 = Black_Tan

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COW || entitytype == EntityType.FOX;
    };

    public PembrokeCorgiEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        PembrokeCorgiEntity baby = ModEntityTypes.PEMBROKE_CORGI.get().create(serverLevel);

        determineBabyVariant(baby, (PembrokeCorgiEntity) otherParent);

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
            state.getController().setAnimation(RawAnimation.begin().then("animation.pembroke_corgi.sitting", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() && state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.pembroke_corgi.angrywalk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.pembroke_corgi.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.pembroke_corgi.angryidle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(RawAnimation.begin().then("animation.pembroke_corgi.idle", Animation.LoopType.LOOP));
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

        Item itemForTaming = ModItems.BEEF_TREAT.get();

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
                if (this.getBaseColor() == 2) {
                    message = Component.literal("This Pembroke Corgi demonstrates a recessive trait.");
                } else if (this.getBaseColor() == 1) {
                    if (this.getCarriedColor() == 2) {
                        message = Component.literal("This Pembroke Corgi demonstrates a trait that can be recessive, but also carries the more recessive black-headed tricolor trait.");
                    } else {
                        message = Component.literal("This Pembroke Corgi demonstrates a trait that can be recessive.");
                    }
                } else if (this.isFawn()) {
                    if (this.getCarriedColor() == 2) {
                        message = Component.literal("This Pembroke Corgi demonstrates a rare variant and carries the recessive black-headed tricolor trait.");
                    } else if (this.getCarriedColor() == 1) {
                        message = Component.literal("This Pembroke Corgi demonstrates a rare variant and carries the red-headed tricolor, or sable, trait.");
                    } else {
                        message = Component.literal("This Pembroke Corgi demonstrates a rare variant, but doesn't have any recessive traits.");
                    }
                } else {
                    if (this.getCarriedColor() == 2) {
                        message = Component.literal("This Pembroke Corgi carries the recessive black-headed tricolor trait.");
                    } else if (this.getCarriedColor() == 1) {
                        message = Component.literal("This Pembroke Corgi carries the red-headed tricolor, or sable, trait.");
                    } else {
                        message = Component.literal("This Pembroke Corgi doesn't have any recessive traits.");
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
        this.entityData.set(BASE_COLOR, tag.getInt("BaseColor"));
        this.entityData.set(IS_FAWN, tag.getBoolean("IsFawn"));
        this.entityData.set(CARRIED_COLOR, tag.getInt("CarriedColor"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("BaseColor", this.getBaseColor());
        tag.putBoolean("IsFawn", this.isFawn());
        tag.putInt("CarriedColor", this.getCarriedColor());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(BASE_COLOR, 0);
        this.entityData.define(IS_FAWN, false);
        this.entityData.define(CARRIED_COLOR, 0);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(26.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(2.5D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
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
        int determine = r.nextInt(16) + 1;
        int carrier = r.nextInt(8) + 1;
        int var;

        if (determine < 8) {
            var = 0; // RED
            setBaseColor(0);
            setFawn(false);
            if (carrier == 1 || carrier == 2) {
                setCarriedColor(carrier);
            } else {
                setCarriedColor(0);
            }
        } else if (determine < 13) {
            var = 1; // BLACK_TAN
            setBaseColor(2);
            setCarriedColor(2);
            setFawn(carrier == 1);
        } else if (determine < 16) {
            var = 2; // FAWN
            setBaseColor(0);
            setFawn(true);
            if (carrier == 1 || carrier == 2) {
                setCarriedColor(carrier);
            } else {
                setCarriedColor(0);
            }
        } else {
            var = 3; // SABLE
            setBaseColor(1);
            setFawn(carrier == 1);
            if (carrier == 2) {
                setCarriedColor(2);
            } else {
                setCarriedColor(1);
            }
        }

        // assign chosen variant and finish the method
        PembrokeCorgiVariant variant = PembrokeCorgiVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public PembrokeCorgiVariant getVariant() {
        return PembrokeCorgiVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(PembrokeCorgiVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public int getBaseColor() {
        return this.entityData.get(BASE_COLOR);
    }

    private void setBaseColor(int color) {
        this.entityData.set(BASE_COLOR, color);
    }

    public boolean isFawn() {
        return this.entityData.get(IS_FAWN);
    }

    private void setFawn(boolean is) {
        this.entityData.set(IS_FAWN, is);
    }

    public int getCarriedColor() {
        return this.entityData.get(CARRIED_COLOR);
    }

    private void setCarriedColor(int color) {
        this.entityData.set(CARRIED_COLOR, color);
    }

    private void determineBabyVariant(PembrokeCorgiEntity baby, PembrokeCorgiEntity otherParent) {
        // determine baby's base and carried color
        if (this.getBaseColor() == 0 && otherParent.getBaseColor() == 0) {
            // if both parents are Red, call the following method
            this.determinePureRedBaby(baby, otherParent);
        } else if (this.getBaseColor() == 0) {
            // if this parent is Red, check whether otherParent is Sable or Black_Tan
            if (otherParent.getBaseColor() == 1) {
                // if otherParent is Sable, call the following method
                this.determineRedSableBaby(baby, this, otherParent);
            } else {
                // if otherParent is Black_Tan, call the following method
                this.determineRedBlackTanBaby(baby, this, otherParent);
            }
        } else if (otherParent.getBaseColor() == 0) {
            // if otherParent is Red, check whether this parent is Sable or Black_Tan
            if (this.getBaseColor() == 1) {
                // if this is Sable, call the following method
                this.determineRedSableBaby(baby, otherParent, this);
            } else {
                // if this is Black_Tan, call the following method
                this.determineRedBlackTanBaby(baby, otherParent, this);
            }
        } else if (this.getBaseColor() == 1 && otherParent.getBaseColor() == 1) {
            // if both parents are Sable, call the following method
            this.determinePureSableBaby(baby, otherParent);
        } else if (this.getBaseColor() == 1) {
            // if this parent is Sable and otherParent is Black_Tan, call the following method
            this.determineSableBlackTanBaby(baby, this);
        } else if (otherParent.getBaseColor() == 1) {
            // if otherParent is Sable and this is Black_Tan, call the following method
            this.determineSableBlackTanBaby(baby, otherParent);
        } else {
            // if both parents are Black_Tan, set baby to be Black_Tan
            baby.setBaseColor(2);
            baby.setCarriedColor(2);
        }

        // determine baby's fawn status (true or false)
        if (this.isFawn() && otherParent.isFawn()) {
            // if both parents are fawn, baby will be fawn
            baby.setFawn(true);
        } else if (this.isFawn() || otherParent.isFawn()) {
            // if one parent is fawn, baby has 50% chance to be fawn
            baby.setFawn(this.random.nextBoolean());
        } else {
            // if neither parent is fawn, baby will not be fawn
            baby.setFawn(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isFawn() && baby.getBaseColor() == 0) {
            baby.setVariant(PembrokeCorgiVariant.FAWN);
        } else if (baby.getBaseColor() == 0) {
            baby.setVariant(PembrokeCorgiVariant.RED);
        } else if (baby.getBaseColor() == 1) {
            baby.setVariant(PembrokeCorgiVariant.SABLE);
        } else {
            baby.setVariant(PembrokeCorgiVariant.BLACK_TAN);
        }
    }

    private void determinePureRedBaby(PembrokeCorgiEntity baby, PembrokeCorgiEntity otherParent) {
        int determine = random.nextInt(4) + 1;

        if (this.getCarriedColor() == 0 && otherParent.getCarriedColor() == 0) {
            // if both parents are pure red (don't carry other colors), baby will be pure red
            baby.setBaseColor(0);
            baby.setCarriedColor(0);
        } else if (this.getCarriedColor() == 0) {
            // if one parent is pure red and the other carries another color, baby will appear red and have
            // 50% chance to carry red and 50% chance to carry the other trait
            baby.setBaseColor(0);
            if (this.random.nextBoolean()) {
                baby.setCarriedColor(0);
            } else {
                baby.setCarriedColor(otherParent.getCarriedColor());
            }
        } else if (otherParent.getCarriedColor() == 0) {
            // if one parent is pure red and the other carries another color, baby will appear red and have
            // 50% chance to carry red and 50% chance to carry the other trait
            baby.setBaseColor(0);
            if (this.random.nextBoolean()) {
                baby.setCarriedColor(0);
            } else {
                baby.setCarriedColor(this.getCarriedColor());
            }
        } else if (this.getCarriedColor() == 1 && otherParent.getCarriedColor() == 1) {
            // if both parents carry Sable, baby has 25% chance to be pure red, 50% to be red and carry sable,
            // and 25% chance to be pure sable
            if (determine == 1) {
                baby.setBaseColor(0);
                baby.setCarriedColor(0);
            } else if (determine < 4) {
                baby.setBaseColor(0);
                baby.setCarriedColor(1);
            } else {
                baby.setBaseColor(1);
                baby.setCarriedColor(1);
            }
        } else if (this.getCarriedColor() == 1 || otherParent.getCarriedColor() == 1) {
            // if one parent carries Sable and the other carries Black_Tan, baby has 25% chance to be pure red,
            // 25% chance to be red and carry sable, 25% chance to be red and carry black_tan, and 25% chance to
            // be sable and carry black_tan
            if (determine == 1) {
                baby.setBaseColor(0);
                baby.setCarriedColor(0);
            } else if (determine == 2) {
                baby.setBaseColor(0);
                baby.setCarriedColor(1);
            } else if (determine == 3) {
                baby.setBaseColor(0);
                baby.setCarriedColor(2);
            } else {
                baby.setBaseColor(1);
                baby.setCarriedColor(2);
            }
        } else {
            // if both parents carry Black_Tan, baby has 25% chance to be pure red, 50% chance to be red and
            // carry black_tan, and 25% chance to be black_tan
            if (determine == 1) {
                baby.setBaseColor(0);
                baby.setCarriedColor(0);
            } else if (determine < 4) {
                baby.setBaseColor(0);
                baby.setCarriedColor(2);
            } else {
                baby.setBaseColor(2);
                baby.setCarriedColor(2);
            }
        }
    }

    // method used when red parent is bred to a sable, where parA is red and parB is Sable
    private void determineRedSableBaby(PembrokeCorgiEntity baby, PembrokeCorgiEntity parA, PembrokeCorgiEntity parB) {
        int determine = this.random.nextInt(4) + 1;

        if (parA.getCarriedColor() == 0 && parB.getCarriedColor() == 1) {
            // if parA is pure red and parB is pure Sable, baby will be red and carry sable
            baby.setBaseColor(0);
            baby.setCarriedColor(1);
        } else if (parA.getCarriedColor() == 0) {
            // if parA is pure red and parB carries Black_Tan, baby will be red and carry either Sable or BT
            baby.setBaseColor(0);
            baby.setCarriedColor(this.random.nextInt(2) + 1);
        } else if (parA.getCarriedColor() == 1 && parB.getCarriedColor() == 1) {
            // if parA carries Sable and parB is pure sable, baby has 50% chance to be red and carry sable and
            // 50% chance to be pure sable
            baby.setBaseColor(this.random.nextInt(2));
            baby.setCarriedColor(1);
        } else if (parA.getCarriedColor() == 1) {
            // if parA carries sable and parB carries Black_Tan, baby has 25% chance to be red and carry sable,
            // 25% chance to be pure sable, 25% chance to be red and carry black_tan, and 25% to be sable and
            // carry BT
            if (determine == 1) {
                baby.setBaseColor(0);
                baby.setCarriedColor(1);
            } else if (determine == 2) {
                baby.setBaseColor(1);
                baby.setCarriedColor(1);
            } else if (determine == 3) {
                baby.setBaseColor(0);
                baby.setBaseColor(2);
            } else {
                baby.setBaseColor(1);
                baby.setCarriedColor(2);
            }
        } else if (parA.getCarriedColor() == 2 && parB.getCarriedColor() == 1) {
            // if parA carries Black_Tan and parB is pure Sable, baby has 50% chance to be red and carry sable
            // and 50% chance to be sable and carry black_tan
            if (this.random.nextBoolean()) {
                baby.setBaseColor(0);
                baby.setCarriedColor(1);
            } else {
                baby.setBaseColor(1);
                baby.setCarriedColor(2);
            }
        } else {
            // if parA and parB carry Black_Tan, baby has 25% chance to be red and carry sable, 25% chance to be
            // red and carry black_tan, 25% chance to be sable and carry BT, and 25% chance to be black_tan
            if (determine == 1) {
                baby.setBaseColor(0);
                baby.setCarriedColor(1);
            } else if (determine == 2) {
                baby.setBaseColor(0);
                baby.setCarriedColor(2);
            } else if (determine == 3) {
                baby.setBaseColor(1);
                baby.setCarriedColor(2);
            } else {
                baby.setBaseColor(2);
                baby.setBaseColor(2);
            }
        }
    }

    // method used when red parent is bred to a black_tan, where parA is red and parB is black_tan
    private void determineRedBlackTanBaby(PembrokeCorgiEntity baby, PembrokeCorgiEntity parA, PembrokeCorgiEntity parB) {
        // parB will always be pure Black_Tan
        if (parA.getCarriedColor() == 0) {
            // if parA is pure red, baby will be red and carry black_tan
            baby.setBaseColor(0);
            baby.setCarriedColor(2);
        } else if (parA.getCarriedColor() == 1) {
            // if parA carries sable, baby has 50% chance to be red and carry black_tan and 50% chance to be
            // sable and carry black_tan
            baby.setBaseColor(this.random.nextInt(2));
            baby.setCarriedColor(2);
        } else {
            // if parA carries black_tan, baby has 50% chance to be red and carry black_tan and 50% chance to be
            // pure black_tan
            baby.setCarriedColor(2);
            if (this.random.nextBoolean()) {
                baby.setBaseColor(0);
            } else {
                baby.setBaseColor(2);
            }
        }
    }

    private void determinePureSableBaby(PembrokeCorgiEntity baby, PembrokeCorgiEntity otherParent) {
        if (this.getCarriedColor() == 1 && otherParent.getCarriedColor() == 1) {
            // if both parents are pure sable, baby will be pure sable
            baby.setBaseColor(1);
            baby.setCarriedColor(1);
        } else if (this.getCarriedColor() == 1 || otherParent.getCarriedColor() == 1) {
            // if one parent is pure sable and the other carries black_tan, baby has 50% chance to be pure
            // sable and 50% chance to be sable and carry black_tan
            baby.setBaseColor(1);
            baby.setCarriedColor(this.random.nextInt(2) + 1);
        } else {
            // if both parents carry black_tan, baby has 25% to be pure sable, 50% chance to be sable and carry
            // black_tan, and 25% chance to be pure black_tan
            int determine = this.random.nextInt(4) + 1;
            if (determine == 1) {
                baby.setBaseColor(1);
                baby.setCarriedColor(1);
            } else if (determine < 4) {
                baby.setBaseColor(1);
                baby.setCarriedColor(2);
            } else {
                baby.setBaseColor(2);
                baby.setCarriedColor(2);
            }
        }
    }

    private void determineSableBlackTanBaby(PembrokeCorgiEntity baby, PembrokeCorgiEntity sablePar) {
        if (sablePar.getCarriedColor() == 1) {
            // if sablePar is pure sable, baby will be sable and carry black_tan
            baby.setBaseColor(1);
            baby.setCarriedColor(2);
        } else {
            // if parA carries black_tan, baby has 50% chance to be sable and carry black_tan and 50% chance to
            // be black_tan
            baby.setBaseColor(this.random.nextInt(2) + 1);
            baby.setCarriedColor(2);
        }
    }
}
