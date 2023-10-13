package com.kitnjinx.modogs.entity.custom;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.variant.AlaskanMalamuteVariant;
import com.kitnjinx.modogs.entity.variant.ArmorVariant;
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
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.Random;
import java.util.function.Predicate;

public class AlaskanMalamuteEntity extends AbstractDog {

    // handles coat variant
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(AlaskanMalamuteEntity.class, EntityDataSerializers.INT);

    // this method controls what animals a dog will hunt
    public static final Predicate<LivingEntity> PREY_SELECTOR = prey -> {
        EntityType<?> entitytype = prey.getType();
        return entitytype == EntityType.COW;
    };

    public AlaskanMalamuteEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0f)
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
        AlaskanMalamuteEntity baby = ModEntityTypes.ALASKAN_MALAMUTE.get().create(serverLevel);

        determineBabyVariant(baby, (AlaskanMalamuteEntity) otherParent);

        // Determines if the baby is tamed based on parent
        if (this.isTame()) {
            baby.setOwnerUUID(this.getOwnerUUID());
            baby.setTame(true);
        }

        baby.setCollar(CollarVariant.NONE);
        baby.setArmor(ArmorVariant.NONE);
        
        return baby;
    }

    private <E extends AlaskanMalamuteEntity> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.alaskan_malamute.sitting"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive() & event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.alaskan_malamute.angrywalk"));
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.alaskan_malamute.walk"));
            return PlayState.CONTINUE;
        }

        if (this.isAngry() || this.isAggressive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.alaskan_malamute.angryidle"));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.alaskan_malamute.idle"));
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

        Item itemForTaming = ModItems.BEEF_TREAT.get();
        Item itemForTaming2 = ModItems.SALMON_TREAT.get();

        if (item == itemForTaming || item == itemForTaming2 && !isTame()) {
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
                player.sendMessage(new TextComponent("Alaskan Malamutes don't have any genes to look for."), player.getUUID());

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
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(24.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(7D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        }
    }

    /* VARIANTS */
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawn, @Nullable SpawnGroupData group,
                                        @Nullable CompoundTag tag) {
        // Variables for determining the variant
        Random r = new Random();
        int determine = r.nextInt(16);
        int var;

        // if statement gives weighted chances to different variants
        if (getRandom().nextBoolean()) {
            var = 0;
        } else {
            if (determine < 5) {
                var = 1;
            } else if (determine < 10) {
                var = 2;
            } else if (determine < 13) {
                var = 3;
            } else if (determine < 15) {
                var = 4;
            } else {
                var = 5;
            }
        }

        // assign chosen variant and finish the method
        AlaskanMalamuteVariant variant = AlaskanMalamuteVariant.byId(var);
        // Basic variant setter, equal chance
        // AlaskanMalamuteVariant variant = Util.getRandom(AlaskanMalamuteVariant.values(), this.random);
        setVariant(variant);
        setCollar(CollarVariant.NONE);
        setArmor(ArmorVariant.NONE);
        return super.finalizeSpawn(level, difficulty, spawn, group, tag);
    }

    public AlaskanMalamuteVariant getVariant() {
        return AlaskanMalamuteVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(AlaskanMalamuteVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    private void determineBabyVariant(AlaskanMalamuteEntity baby, AlaskanMalamuteEntity otherParent) {
        boolean blackParent;
        boolean silverCheck;
        boolean redCheck;
        boolean sableCheck; // simplified name; triggers whenever there's a seal/sable parent and a black/silver parent

        if (this.getVariant() == AlaskanMalamuteVariant.BLACK || otherParent.getVariant() == AlaskanMalamuteVariant.BLACK) {
            blackParent = true;
        } else {
            blackParent = false;
        }

        if (blackParent && (this.getVariant() == AlaskanMalamuteVariant.SILVER || otherParent.getVariant() == AlaskanMalamuteVariant.SILVER)) {
            silverCheck = true;
            redCheck = false;
        } else if (blackParent && (this.getVariant() == AlaskanMalamuteVariant.RED || otherParent.getVariant() == AlaskanMalamuteVariant.RED)) {
            silverCheck = false;
            redCheck = true;
        } else {
            silverCheck = false;
            redCheck = false;
        }

        if (this.getVariant() != AlaskanMalamuteVariant.GRAY && this.getVariant() != AlaskanMalamuteVariant.RED &&
                otherParent.getVariant() != AlaskanMalamuteVariant.GRAY && otherParent.getVariant() != AlaskanMalamuteVariant.RED) {
            boolean blackSilver = this.getVariant() == AlaskanMalamuteVariant.BLACK || this.getVariant() == AlaskanMalamuteVariant.SILVER;
            if (blackSilver) {
                if (otherParent.getVariant() == AlaskanMalamuteVariant.BLACK || otherParent.getVariant() == AlaskanMalamuteVariant.SILVER) {
                    sableCheck = false;
                } else {
                    sableCheck = true;
                }
            } else {
                if (otherParent.getVariant() == AlaskanMalamuteVariant.BLACK || otherParent.getVariant() == AlaskanMalamuteVariant.SILVER) {
                    sableCheck = true;
                } else {
                    sableCheck = false;
                }
            }
        } else {
            sableCheck = false;
        }

        if (silverCheck || sableCheck) {
            Random r = new Random();
            int determine = r.nextInt(3) + 1;

            if (determine == 3) {
                baby.setVariant(AlaskanMalamuteVariant.GRAY);
            } else if (determine == 2) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(otherParent.getVariant());
            }
        } else if (redCheck) {
            Random r = new Random();
            int determine = r.nextInt(6) + 1;

            if (determine == 6) {
                baby.setVariant(AlaskanMalamuteVariant.SEAL);
            } else if (determine == 5) {
                baby.setVariant(AlaskanMalamuteVariant.SABLE);
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
