package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.BeagleVariant;
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

public class BeagleEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BeagleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_TAN =
            SynchedEntityData.defineId(BeagleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_TAN =
            SynchedEntityData.defineId(BeagleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_DARK_EARS =
            SynchedEntityData.defineId(BeagleEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.RABBIT;
    };

    public BeagleEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        BeagleEntity baby = ModEntityTypes.BEAGLE.get().create(serverLevel);

        determineBabyVariant(baby, (BeagleEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends BeagleEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.beagle.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.beagle.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.beagle.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.beagle.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.beagle.idle"));
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
                TextComponent message;
                if (this.getVariant() == BeagleVariant.DARK_EARS) {
                    message = new TextComponent("This Beagle demonstrates a rare variant of a recessive trait.");
                } else if (this.isTan()) {
                    message = new TextComponent("This Beagle demonstrates a recessive trait.");
                } else if (this.carriesTan()) {
                    message = new TextComponent("This Beagle carries a recessive trait.");
                } else {
                    message = new TextComponent("This Beagle doesn't have any recessive traits.");
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
        this.entityData.set(IS_TAN, tag.getBoolean("IsTan"));
        this.entityData.set(CARRIES_TAN, tag.getBoolean("CarriesTan"));
        this.entityData.set(HAS_DARK_EARS, tag.getBoolean("HasDarkEars"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsTan", this.isTan());
        tag.putBoolean("CarriesTan", this.carriesTan());
        tag.putBoolean("HasDarkEars", this.hasDarkEars());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_TAN, false);
        this.entityData.define(CARRIES_TAN, false);
        this.entityData.define(HAS_DARK_EARS, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(28.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
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
        int determine = r.nextInt(10) + 1;
        int carrier = r.nextInt(4) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 7) {
            var = 0; // BLACK_TAN
            setTanStatus(carrier == 4, false);
        } else {
            setTanStatus(true, true);
            if (determine < 10) {
                var = 1; // TAN
            } else {
                var = 2; // DARK_EARS
            }
        }

        setDarkEars(var == 2);

        // assign chosen variant and finish the method
        BeagleVariant variant = BeagleVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public BeagleVariant getVariant() {
        return BeagleVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(BeagleVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isTan() {
        return this.entityData.get(IS_TAN);
    }

    public boolean carriesTan() {
        return this.entityData.get(CARRIES_TAN);
    }

    private void setTanStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_TAN, carrier);
        this.entityData.set(IS_TAN, is);
    }

    public boolean hasDarkEars() {
        return this.entityData.get(HAS_DARK_EARS);
    }

    private void setDarkEars(boolean has) {
        this.entityData.set(HAS_DARK_EARS, has);
    }

    private void determineBabyVariant(BeagleEntity baby, BeagleEntity otherParent) {
        // determine if baby is black & tan or only tan
        if (this.isTan() && otherParent.isTan()) {
            // if both parents are tan, baby will be tan
            baby.setTanStatus(true, true);
        } else if ((this.isTan() && otherParent.carriesTan()) || (this.carriesTan() && otherParent.isTan())) {
            // if one parent is tan and the other is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be tan
            baby.setTanStatus(true, this.random.nextBoolean());
        } else if (this.isTan() || otherParent.isTan()) {
            // if only one parent is tan, baby will be a carrier
            baby.setTanStatus(true, false);
        } else if (this.carriesTan() && otherParent.carriesTan()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier,
            // and 25% chance to be tan
            int determine = this.random.nextInt(4) + 1;
            baby.setTanStatus(determine > 1, determine == 4);
        } else if (this.carriesTan() || otherParent.carriesTan()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setTanStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby won't carry
            baby.setTanStatus(false, false);
        }

        // determine if baby has the dark ears variant
        if (this.hasDarkEars() && otherParent.hasDarkEars()) {
            // if both parents have dark ears, baby will have it
            baby.setDarkEars(true);
        } else if (this.hasDarkEars() || otherParent.hasDarkEars()) {
            // if only one parent has dark ears, baby has 50/50 chance to have it
            baby.setDarkEars(this.random.nextBoolean());
        } else {
            // if neither parent has dark ears, baby won't have it
            baby.setDarkEars(false);
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (baby.isTan() && baby.hasDarkEars()) {
            baby.setVariant(BeagleVariant.DARK_EARS);
        } else if (baby.isTan()) {
            baby.setVariant(BeagleVariant.TAN);
        } else {
            baby.setVariant(BeagleVariant.BLACK_TAN);
        }
    }
}