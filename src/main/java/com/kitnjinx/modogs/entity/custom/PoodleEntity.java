package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.PoodleVariant;
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

public class PoodleEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(PoodleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_BROWN  =
            SynchedEntityData.defineId(PoodleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_BROWN =
            SynchedEntityData.defineId(PoodleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DILUTE  =
            SynchedEntityData.defineId(PoodleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_DILUTE =
            SynchedEntityData.defineId(PoodleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CARRIES_WHITE  =
            SynchedEntityData.defineId(PoodleEntity.class, EntityDataSerializers.BOOLEAN);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.CHICKEN;
    };

    public PoodleEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        PoodleEntity baby = ModEntityTypes.POODLE.get().create(serverLevel);

        determineBabyVariant(baby, (PoodleEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);

        return baby;
    }

    private <E extends PoodleEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poodle.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poodle.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poodle.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poodle.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.poodle.idle"));
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

        Item itemForTaming = ModItems.CHICKEN_TREAT.get();

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
                if (this.getVariant() == PoodleVariant.WHITE) {
                    if (this.isBrown() && this.isDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive white fur trait. They also have the alleles for diluted brown fur.");
                    } else if (this.isBrown() && this.carriesDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive white fur trait. They also have the alleles for brown fur and carry the dilution trait.");
                    } else if (this.isBrown()) {
                        message = new TextComponent("This Poodle demonstrates the recessive white fur trait. They also have the alleles for brown fur.");
                    } else if (this.carriesBrown() && this.isDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive white fur trait. They also have the alleles for diluted fur and carry the brown fur trait.");
                    } else if (this.carriesBrown() && this.carriesDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive white fur trait. They also carry the dilution and brown fur traits.");
                    } else if (this.carriesBrown()) {
                        message = new TextComponent("This Poodle demonstrates the recessive white fur trait. They also carry the brown fur trait.");
                    } else if (this.isDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive white fur trait. They also have the alleles for diluted fur.");
                    } else if (this.carriesDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive white fur trait. They also carry the dilution trait.");
                    } else {
                        message = new TextComponent("This Poodle demonstrates the recessive white fur trait.");
                    }
                } else if (this.carriesWhite()) {
                    if (this.isBrown() && this.isDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive brown fur and dilutions traits. They also carry the white fur trait.");
                    } else if (this.isBrown() && this.carriesDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive brown fur trait. They also carry the dilution and white fur traits.");
                    } else if (this.isBrown()) {
                        message = new TextComponent("This Poodle demonstrates the recessive brown fur trait. They also carry the white fur trait.");
                    } else if (this.carriesBrown() && this.isDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive dilution trait. They also carry the white and brown fur traits.");
                    } else if (this.carriesBrown() && this.carriesDilute()) {
                        message = new TextComponent("This Poodle carries three recessive traits.");
                    } else if (this.carriesBrown()) {
                        message = new TextComponent("This Poodle carries the white and brown fur traits.");
                    } else if (this.isDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive dilution trait and carries the trait for white fur.");
                    } else if (this.carriesDilute()) {
                        message = new TextComponent("This Poodle carries the white fur and dilution traits.");
                    } else {
                        message = new TextComponent("This Poodle carries the white fur trait.");
                    }
                } else {
                    if (this.isBrown() && this.isDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive brown fur and dilution traits.");
                    } else if (this.isBrown() && this.carriesDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive brown fur trait, and carries the dilution trait.");
                    } else if (this.isBrown()) {
                        message = new TextComponent("This Poodle demonstrates the recessive brown fur trait.");
                    } else if (this.carriesBrown() && this.isDilute()) {
                        message = new TextComponent("This Poodle demonstrates the recessive dilution trait, and carries the brown fur trait.");
                    } else if (this.carriesBrown() && this.carriesDilute()) {
                        message = new TextComponent("This Poodle carries the brown fur and dilution traits.");
                    } else if (this.carriesBrown()) {
                        message = new TextComponent("This Poodle carries the brown fur trait.");
                    } else if (this.isDilute()) {
                        message = new TextComponent("This Poodle demonstrates the dilution trait.");
                    } else if (this.carriesDilute()) {
                        message = new TextComponent("This Poodle carries the dilution trait.");
                    } else {
                        message = new TextComponent("This Poodle doesn't have any recessive traits.");
                    }

                    player.sendMessage(message, player.getUUID());

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
        this.entityData.set(IS_BROWN, tag.getBoolean("IsBrown"));
        this.entityData.set(CARRIES_BROWN, tag.getBoolean("CarriesBrown"));
        this.entityData.set(IS_DILUTE, tag.getBoolean("IsDilute"));
        this.entityData.set(CARRIES_DILUTE, tag.getBoolean("CarriesDilute"));
        this.entityData.set(CARRIES_WHITE, tag.getBoolean("CarriesWhite"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsBrown", this.isBrown());
        tag.putBoolean("CarriesBrown", this.carriesBrown());
        tag.putBoolean("IsDilute", this.isDilute());
        tag.putBoolean("CarriesDilute", this.carriesDilute());
        tag.putBoolean("CarriesWhite", this.carriesWhite());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(IS_BROWN, false);
        this.entityData.define(CARRIES_BROWN, false);
        this.entityData.define(IS_DILUTE, false);
        this.entityData.define(CARRIES_DILUTE, false);
        this.entityData.define(CARRIES_WHITE, false);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(26.0);
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
        int determine = r.nextInt(29) + 1;
        int carrier = r.nextInt(12) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (determine < 21) {
            setDiluteStatus(carrier == 1, false);
            setWhite(carrier == 3);
            if (determine < 13) {
                var = 0; // BLACK
                setBrownStatus(carrier == 2, false);
            } else {
                var = 1; // BROWN
                setBrownStatus(true, true);
            }
        } else if (determine < 27) {
            var = 2; // WHITE
            setWhite(true);
            int hidden = r.nextInt(23) + 1;
            if (hidden < 21) {
                setDiluteStatus(carrier == 1, false);
                if (hidden < 13) {
                    setBrownStatus(carrier == 2, false);
                } else {
                    setBrownStatus(true, true);
                }
            } else {
                setDiluteStatus(true, true);
                if (hidden < 23) {
                    setBrownStatus(carrier < 3, false);
                } else {
                    setBrownStatus(true, true);
                }
            }
        } else {
            setDiluteStatus(true, true);
            setWhite(carrier == 1);
            if (determine < 29) {
                var = 3; // SILVER
                setBrownStatus(carrier == 2, false);
            } else {
                var = 4; // CREAM
                setBrownStatus(true, true);
            }
        }

        // assign chosen variant and finish the method
        PoodleVariant variant = PoodleVariant.byId(var);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public PoodleVariant getVariant() {
        return PoodleVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(PoodleVariant variant) {
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

    public boolean isDilute() {
        return this.entityData.get(IS_DILUTE);
    }

    public boolean carriesDilute() {
        return this.entityData.get(CARRIES_DILUTE);
    }

    private void setDiluteStatus(boolean carrier, boolean is) {
        this.entityData.set(CARRIES_DILUTE, carrier);
        this.entityData.set(IS_DILUTE, is);
    }

    public boolean carriesWhite() {
        return this.entityData.get(CARRIES_WHITE);
    }

    private void setWhite(boolean carrier) {
        this.entityData.set(CARRIES_WHITE, carrier);
    }

    private void determineBabyVariant(PoodleEntity baby, PoodleEntity otherParent) {
        // determine if baby is brown, a carrier, or straight black
        if (this.isBrown() && otherParent.isBrown()) {
            // if both parents are brown, baby will be brown
            baby.setBrownStatus(true, true);
        } else if ((this.isBrown() && otherParent.carriesBrown()) || (this.carriesBrown() && otherParent.isBrown())) {
            // if one parent is brown and the other is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be brown
            baby.setBrownStatus(true, this.random.nextBoolean());
        } else if (this.isBrown() || otherParent.isBrown()) {
            // if only one parent is brown, baby will be a carrier
            baby.setBrownStatus(true, false);
        } else if (this.carriesBrown() && otherParent.carriesBrown()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier,
            // and 25% chance to be brown
            int determine = this.random.nextInt(4) + 1;
            baby.setBrownStatus(determine > 1, determine == 4);
        } else if (this.carriesBrown() || otherParent.carriesBrown()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setBrownStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not carry brown
            baby.setBrownStatus(false, false);
        }

        // determine if baby is dilute, a carrier, or straight black
        if (this.isDilute() && otherParent.isDilute()) {
            // if both parents are diluted, baby will be diluted
            baby.setDiluteStatus(true, true);
        } else if ((this.isDilute() && otherParent.carriesDilute()) ||
                (this.carriesDilute() && otherParent.isDilute())) {
            // if one parent is diluted and the other is a carrier, baby has 50% chance to be diluted and 50%
            // chance to be a carrier
            baby.setDiluteStatus(true, this.random.nextBoolean());
        } else if (this.isDilute() || otherParent.isDilute()) {
            // if only one parent is diluted, baby will be a carrier
            baby.setDiluteStatus(true, false);
        } else if (this.carriesDilute() && otherParent.carriesDilute()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be diluted
            int determine = this.random.nextInt(4) + 1;
            baby.setDiluteStatus(determine > 1, determine == 4);
        } else if (this.carriesDilute() || otherParent.carriesDilute()) {
            // if only one parent carries dilute, baby has 50/50 chance to be a carrier
            baby.setDiluteStatus(this.random.nextBoolean(), false);
        } else {
            // if neither parent is a carrier, baby will not be a carrier
            baby.setDiluteStatus(false, false);
        }

        // determine if baby is white, a carrier, or straight white
        boolean isWhite;
        if (this.getVariant() == PoodleVariant.WHITE && otherParent.getVariant() == PoodleVariant.WHITE) {
            // if both parents are white, baby will be white
            baby.setWhite(true);
            isWhite = true;
        } else if ((this.getVariant() == PoodleVariant.WHITE && otherParent.carriesWhite())
                || (this.carriesWhite() && otherParent.getVariant() == PoodleVariant.WHITE)) {
            // if one parent is white and the other is a carrier, baby has 50% chance to be a carrier and 50%
            // chance to be white
            baby.setWhite(true);
            isWhite = this.random.nextBoolean();
        } else if (this.getVariant() == PoodleVariant.WHITE || otherParent.getVariant() == PoodleVariant.WHITE) {
            // if only one parent is white, baby will be a carrier
            baby.setWhite(true);
            isWhite = false;
        } else if (this.carriesWhite() && otherParent.carriesWhite()) {
            // if both parents are carriers, baby has 25% chance not to carry, 50% chance to be a carrier, and
            // 25% chance to be white
            int determine = this.random.nextInt(4) + 1;
            baby.setWhite(determine > 1);
            isWhite = determine == 4;
        } else if (this.carriesWhite() || otherParent.carriesWhite()) {
            // if only one parent is a carrier, baby has 50/50 chance to be a carrier
            baby.setWhite(this.random.nextBoolean());
            isWhite = false;
        } else {
            // if neither parent is a carrier, baby will not have white
            baby.setWhite(false);
            isWhite = false;
        }

        // determine baby's phenotype (TYPE_VARIANT)
        if (isWhite) {
            baby.setVariant(PoodleVariant.WHITE);
        } else if (baby.isDilute() && baby.isBrown()) {
            baby.setVariant(PoodleVariant.CREAM);
        } else if (baby.isDilute()) {
            baby.setVariant(PoodleVariant.SILVER);
        } else if (baby.isBrown()) {
            baby.setVariant(PoodleVariant.BROWN);
        } else {
            baby.setVariant(PoodleVariant.BLACK);
        }
    }
}