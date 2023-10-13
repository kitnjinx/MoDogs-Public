package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.RussellTerrierVariant;
import com.kitnjinx.modogs.item.ModItems;
import net.minecraft.nbt.CompoundTag;
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

public class RussellTerrierEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(RussellTerrierEntity.class, EntityDataSerializers.INT);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.FOX;
    };

    public RussellTerrierEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
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
        RussellTerrierEntity baby = ModEntityTypes.RUSSELL_TERRIER.get().create(serverLevel);

        determineBabyVariant(baby, (RussellTerrierEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);
        
        return baby;
    }

    private <E extends RussellTerrierEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.russell_terrier.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.russell_terrier.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.russell_terrier.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.russell_terrier.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.russell_terrier.idle"));
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
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

        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
    }

    @Override
    public void setTame (boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(3D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3D);
            getAttribute(Attributes.ATTACK_SPEED).setBaseValue(2.0D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group,
                                        @Nullable CompoundTag tag) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(5) + 1;
        int rarity = r.nextInt(10) + 1;
        int var;

        // if statement gives weighted chances to different variants
        if (rarity < 7) {
            if (determine < 4) {
                var = 0;
            } else {
                var = 1;
            }
        } else if (rarity < 10) {
            if (determine < 4) {
                var = 2;
            } else {
                var = 3;
            }
        } else {
            if (determine < 4) {
                var = 4;
            } else {
                var = 5;
            }
        }

        // assign chosen variant and finish the method
        RussellTerrierVariant variant = RussellTerrierVariant.byId(var);
        // Basic variant setter, equal chance
        // RussellTerrierVariant variant = Util.getRandom(RussellTerrierVariant.values(), this.random);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public RussellTerrierVariant getVariant() {
        return RussellTerrierVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(RussellTerrierVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    private void determineBabyVariant(RussellTerrierEntity baby, RussellTerrierEntity otherParent) {
        RussellTerrierVariant parentA = this.getVariant();
        RussellTerrierVariant parentB = otherParent.getVariant();
        boolean combineBrown; // parents can create brown
        boolean combineTriBrown; // parents can create tri brown
        boolean combineTriTan; // parents can create tri tan
        boolean combineTan; // parents can create tan

        if (parentA == RussellTerrierVariant.BLACK) {
            if (parentB != RussellTerrierVariant.TRI_BROWN && parentB != RussellTerrierVariant.TRI_TAN) {
                if (parentB == RussellTerrierVariant.BROWN) {
                    combineBrown = false;
                    combineTriBrown = true;
                    combineTriTan = false;
                } else if (parentB == RussellTerrierVariant.TAN) {
                    combineBrown = false;
                    combineTriBrown = false;
                    combineTriTan = true;
                } else if (parentB == RussellTerrierVariant.CREAM) {
                    combineBrown = true;
                    combineTriBrown = false;
                    combineTriTan = false;
                } else {
                    combineBrown = false;
                    combineTriBrown = false;
                    combineTriTan = false;
                }
            } else {
                combineBrown = false;
                combineTriBrown = false;
                combineTriTan = false;
            }
        } else if (parentB == RussellTerrierVariant.BLACK) {
            if (parentA != RussellTerrierVariant.TRI_BROWN && parentA != RussellTerrierVariant.TRI_TAN) {
                if (parentA == RussellTerrierVariant.BROWN) {
                    combineBrown = false;
                    combineTriBrown = true;
                    combineTriTan = false;
                } else if (parentA == RussellTerrierVariant.TAN) {
                    combineBrown = false;
                    combineTriBrown = false;
                    combineTriTan = true;
                } else if (parentA == RussellTerrierVariant.CREAM) {
                    combineBrown = true;
                    combineTriBrown = false;
                    combineTriTan = false;
                } else {
                    combineBrown = false;
                    combineTriBrown = false;
                    combineTriTan = false;
                }
            } else {
                combineBrown = false;
                combineTriBrown = false;
                combineTriTan = false;
            }
        } else {
            combineBrown = false;
            combineTriBrown = false;
            combineTriTan = false;
        }

        if (parentA == RussellTerrierVariant.BROWN || parentA == RussellTerrierVariant.CREAM &&
                parentB == RussellTerrierVariant.BROWN || parentB == RussellTerrierVariant.CREAM) {
            if (parentA != parentB) {
                combineTan = true;
            } else {
                combineTan = false;
            }
        } else {
            combineTan = false;
        }

        if (combineBrown) {
            Random r = new Random();
            int determine = r.nextInt(5) + 1;

            if (determine == 5) {
                baby.setVariant(RussellTerrierVariant.BROWN);
            } else if (determine > 2) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(otherParent.getVariant());
            }
        } else if (combineTriBrown) {
            Random r = new Random();
            int determine = r.nextInt(5) + 1;

            if (determine == 5) {
                baby.setVariant(RussellTerrierVariant.TRI_BROWN);
            } else if (determine > 2) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(otherParent.getVariant());
            }
        } else if (combineTriTan) {
            Random r = new Random();
            int determine = r.nextInt(5) + 1;

            if (determine == 5) {
                baby.setVariant(RussellTerrierVariant.TRI_TAN);
            } else if (determine > 2) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(otherParent.getVariant());
            }
        } else if (combineTan) {
            Random r = new Random();
            int determine = r.nextInt(5) + 1;

            if (determine == 5) {
                baby.setVariant(RussellTerrierVariant.TAN);
            } else if (determine > 2) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(otherParent.getVariant());
            }
        } else {
            // Determines variant based on parents
            if (this.random.nextBoolean()) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(otherParent.getVariant());
            }
        }
    }
}
