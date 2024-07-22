package com.kitnjinx.modogs.entity;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, MoDogs.MOD_ID);

    public static final Supplier<EntityType<GermanShepherdEntity>> GERMAN_SHEPHERD =
            ENTITY_TYPES.register("german_shepherd",
            () -> EntityType.Builder.of(GermanShepherdEntity::new, MobCategory.CREATURE)
                    .sized(0.65f, 0.9f)
                    .build(new ResourceLocation(MoDogs.MOD_ID, "german_shepherd").toString()));

    public static final Supplier<EntityType<BorderCollieEntity>> BORDER_COLLIE =
            ENTITY_TYPES.register("border_collie",
                    () -> EntityType.Builder.of(BorderCollieEntity::new, MobCategory.CREATURE)
                            .sized(0.55f, 0.8f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "border_collie").toString()));

    public static final Supplier<EntityType<GoldenRetrieverEntity>> GOLDEN_RETRIEVER =
            ENTITY_TYPES.register("golden_retriever",
                    () -> EntityType.Builder.of(GoldenRetrieverEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "golden_retriever").toString()));

    public static final Supplier<EntityType<LabRetrieverEntity>> LAB_RETRIEVER =
            ENTITY_TYPES.register("lab_retriever",
                    () -> EntityType.Builder.of(LabRetrieverEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "lab_retriever").toString()));

    public static final Supplier<EntityType<DachshundEntity>> DACHSHUND =
            ENTITY_TYPES.register("dachshund",
                    () -> EntityType.Builder.of(DachshundEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.5f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "dachshund").toString()));

    public static final Supplier<EntityType<DalmatianEntity>> DALMATIAN =
            ENTITY_TYPES.register("dalmatian",
                    () -> EntityType.Builder.of(DalmatianEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "dalmatian").toString()));

    public static final Supplier<EntityType<CardiganCorgiEntity>> CARDIGAN_CORGI =
            ENTITY_TYPES.register("cardigan_corgi",
                    () -> EntityType.Builder.of(CardiganCorgiEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "cardigan_corgi").toString()));

    public static final Supplier<EntityType<PembrokeCorgiEntity>> PEMBROKE_CORGI =
            ENTITY_TYPES.register("pembroke_corgi",
                    () -> EntityType.Builder.of(PembrokeCorgiEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "pembroke_corgi").toString()));

    public static final Supplier<EntityType<RussellTerrierEntity>> RUSSELL_TERRIER =
            ENTITY_TYPES.register("russell_terrier",
                    () -> EntityType.Builder.of(RussellTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.5f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "russell_terrier").toString()));

    public static final Supplier<EntityType<AlaskanMalamuteEntity>> ALASKAN_MALAMUTE =
            ENTITY_TYPES.register("alaskan_malamute",
                    () -> EntityType.Builder.of(AlaskanMalamuteEntity::new, MobCategory.CREATURE)
                            .sized(0.65f, 0.9f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "alaskan_malamute").toString()));

    public static final Supplier<EntityType<BerneseMountainDogEntity>> BERNESE_MOUNTAIN_DOG =
            ENTITY_TYPES.register("bernese_mountain_dog",
                    () -> EntityType.Builder.of(BerneseMountainDogEntity::new, MobCategory.CREATURE)
                            .sized(0.7f, 0.95f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "bernese_mountain_dog").toString()));

    public static final Supplier<EntityType<SaintBernardEntity>> SAINT_BERNARD =
            ENTITY_TYPES.register("saint_bernard",
                    () -> EntityType.Builder.of(SaintBernardEntity::new, MobCategory.CREATURE)
                            .sized(0.75f, 1.05f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "saint_bernard").toString()));

    public static final Supplier<EntityType<BloodhoundEntity>> BLOODHOUND =
            ENTITY_TYPES.register("bloodhound",
                    () -> EntityType.Builder.of(BloodhoundEntity::new, MobCategory.CREATURE)
                            .sized(0.675f, 0.925f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "bloodhound").toString()));

    public static final Supplier<EntityType<BoxerEntity>> BOXER =
            ENTITY_TYPES.register("boxer",
                    () -> EntityType.Builder.of(BoxerEntity::new, MobCategory.CREATURE)
                            .sized(0.625f, 0.875f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "boxer").toString()));

    public static final Supplier<EntityType<GreyhoundEntity>> GREYHOUND =
            ENTITY_TYPES.register("greyhound",
                    () -> EntityType.Builder.of(GreyhoundEntity::new, MobCategory.CREATURE)
                            .sized(0.75f, 1.1f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "greyhound").toString()));

    public static final Supplier<EntityType<PitBullEntity>> PIT_BULL =
            ENTITY_TYPES.register("pit_bull",
                    () -> EntityType.Builder.of(PitBullEntity::new, MobCategory.CREATURE)
                            .sized(0.55f, 0.8f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "pit_bull").toString()));

    public static final Supplier<EntityType<GreatDaneEntity>> GREAT_DANE =
            ENTITY_TYPES.register("great_dane",
                    () -> EntityType.Builder.of(GreatDaneEntity::new, MobCategory.CREATURE)
                            .sized(0.8f, 1.15f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "great_dane").toString()));

    public static final Supplier<EntityType<MastiffEntity>> MASTIFF =
            ENTITY_TYPES.register("mastiff",
                    () -> EntityType.Builder.of(MastiffEntity::new, MobCategory.CREATURE)
                            .sized(0.8f, 1.15f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "mastiff").toString()));

    public static final Supplier<EntityType<ShibaInuEntity>> SHIBA_INU =
            ENTITY_TYPES.register("shiba_inu",
                    () -> EntityType.Builder.of(ShibaInuEntity::new, MobCategory.CREATURE)
                            .sized(0.45f, 0.65f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "shiba_inu").toString()));

    public static final Supplier<EntityType<ShetlandSheepdogEntity>> SHETLAND_SHEEPDOG =
            ENTITY_TYPES.register("shetland_sheepdog",
                    () -> EntityType.Builder.of(ShetlandSheepdogEntity::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.6f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "shetland_sheepdog").toString()));

    public static final Supplier<EntityType<BostonTerrierEntity>> BOSTON_TERRIER =
            ENTITY_TYPES.register("boston_terrier",
                    () -> EntityType.Builder.of(BostonTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "boston_terrier").toString()));

    public static final Supplier<EntityType<ScottishTerrierEntity>> SCOTTISH_TERRIER =
            ENTITY_TYPES.register("scottish_terrier",
                    () -> EntityType.Builder.of(ScottishTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.3f, 0.4f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "scottish_terrier").toString()));

    public static final Supplier<EntityType<CKCharlesSpanielEntity>> CK_CHARLES_SPANIEL =
            ENTITY_TYPES.register("ck_charles_spaniel",
                    () -> EntityType.Builder.of(CKCharlesSpanielEntity::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "ck_charles_spaniel").toString()));

    public static final Supplier<EntityType<ItalianGreyhoundEntity>> ITALIAN_GREYHOUND =
            ENTITY_TYPES.register("italian_greyhound",
                    () -> EntityType.Builder.of(ItalianGreyhoundEntity::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.6f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "italian_greyhound").toString()));

    public static final Supplier<EntityType<AustralianShepherdEntity>> AUSTRALIAN_SHEPHERD =
            ENTITY_TYPES.register("australian_shepherd",
                    () -> EntityType.Builder.of(AustralianShepherdEntity::new, MobCategory.CREATURE)
                            .sized(0.55f, 0.75f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "australian_shepherd").toString()));

    public static final Supplier<EntityType<BasenjiEntity>> BASENJI =
            ENTITY_TYPES.register("basenji",
                    () -> EntityType.Builder.of(BasenjiEntity::new, MobCategory.CREATURE)
                            .sized(0.45f, 0.65f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "basenji").toString()));

    public static final Supplier<EntityType<PugEntity>> PUG =
            ENTITY_TYPES.register("pug",
                    () -> EntityType.Builder.of(PugEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.5f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "pug").toString()));

    public static final Supplier<EntityType<CockerSpanielEntity>> COCKER_SPANIEL =
            ENTITY_TYPES.register("cocker_spaniel",
                    () -> EntityType.Builder.of(CockerSpanielEntity::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.6f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "cocker_spaniel").toString()));

    public static final Supplier<EntityType<BullTerrierEntity>> BULL_TERRIER =
            ENTITY_TYPES.register("bull_terrier",
                    () -> EntityType.Builder.of(BullTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.75f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "bull_terrier").toString()));

    public static final Supplier<EntityType<MiniBullTerrierEntity>> MINI_BULL_TERRIER =
            ENTITY_TYPES.register("mini_bull_terrier",
                    () -> EntityType.Builder.of(MiniBullTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.45f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "mini_bull_terrier").toString()));

    public static final Supplier<EntityType<SchnauzerEntity>> SCHNAUZER =
            ENTITY_TYPES.register("schnauzer",
                    () -> EntityType.Builder.of(SchnauzerEntity::new, MobCategory.CREATURE)
                            .sized(0.5f, 0.7f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "schnauzer").toString()));

    public static final Supplier<EntityType<MiniSchnauzerEntity>> MINI_SCHNAUZER =
            ENTITY_TYPES.register("mini_schnauzer",
                    () -> EntityType.Builder.of(MiniSchnauzerEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "mini_schnauzer").toString()));

    public static final Supplier<EntityType<PoodleEntity>> POODLE =
            ENTITY_TYPES.register("poodle",
                    () -> EntityType.Builder.of(PoodleEntity::new, MobCategory.CREATURE)
                            .sized(0.5f, 0.65f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "poodle").toString()));

    public static final Supplier<EntityType<ToyPoodleEntity>> TOY_POODLE =
            ENTITY_TYPES.register("toy_poodle",
                    () -> EntityType.Builder.of(ToyPoodleEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.4f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "toy_poodle").toString()));

    public static final Supplier<EntityType<DobermanEntity>> DOBERMAN =
            ENTITY_TYPES.register("doberman",
                    () -> EntityType.Builder.of(DobermanEntity::new, MobCategory.CREATURE)
                            .sized(0.7f, 1.0f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "doberman").toString()));

    public static final Supplier<EntityType<MiniPinscherEntity>> MINI_PINSCHER =
            ENTITY_TYPES.register("mini_pinscher",
                    () -> EntityType.Builder.of(MiniPinscherEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "mini_pinscher").toString()));

    public static final Supplier<EntityType<HuskyEntity>> HUSKY =
            ENTITY_TYPES.register("husky",
                    () -> EntityType.Builder.of(HuskyEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "husky").toString()));

    public static final Supplier<EntityType<RedboneCoonhoundEntity>> REDBONE_COONHOUND =
            ENTITY_TYPES.register("redbone_coonhound",
                    () -> EntityType.Builder.of(RedboneCoonhoundEntity::new, MobCategory.CREATURE)
                            .sized(0.65f, 0.9f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "redbone_coonhound").toString()));

    public static final Supplier<EntityType<TreeWalkHoundEntity>> TREE_WALK_HOUND =
            ENTITY_TYPES.register("tree_walk_hound",
                    () -> EntityType.Builder.of(TreeWalkHoundEntity::new, MobCategory.CREATURE)
                            .sized(0.65f, 0.9f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "tree_walk_hound").toString()));

    public static final Supplier<EntityType<AiredaleTerrierEntity>> AIREDALE_TERRIER =
            ENTITY_TYPES.register("airedale_terrier",
                    () -> EntityType.Builder.of(AiredaleTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "airedale_terrier").toString()));

    public static final Supplier<EntityType<AmericanFoxhoundEntity>> AMERICAN_FOXHOUND =
            ENTITY_TYPES.register("american_foxhound",
                    () -> EntityType.Builder.of(AmericanFoxhoundEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "american_foxhound").toString()));

    public static final Supplier<EntityType<BulldogEntity>> BULLDOG =
            ENTITY_TYPES.register("bulldog",
                    () -> EntityType.Builder.of(BulldogEntity::new, MobCategory.CREATURE)
                            .sized(0.45f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "bulldog").toString()));

    public static final Supplier<EntityType<CollieEntity>> COLLIE =
            ENTITY_TYPES.register("collie",
                    () -> EntityType.Builder.of(CollieEntity::new, MobCategory.CREATURE)
                            .sized(0.65f, 0.9f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "collie").toString()));

    public static final Supplier<EntityType<MudiEntity>> MUDI =
            ENTITY_TYPES.register("mudi",
                    () -> EntityType.Builder.of(MudiEntity::new, MobCategory.CREATURE)
                            .sized(0.5f, 0.7f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "mudi").toString()));

    public static final Supplier<EntityType<NorwegianElkhoundEntity>> NORWEGIAN_ELKHOUND =
            ENTITY_TYPES.register("norwegian_elkhound",
                    () -> EntityType.Builder.of(NorwegianElkhoundEntity::new, MobCategory.CREATURE)
                            .sized(0.55f, 0.78f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "norwegian_elkhound").toString()));

    public static final Supplier<EntityType<BeagleEntity>> BEAGLE =
            ENTITY_TYPES.register("beagle",
                    () -> EntityType.Builder.of(BeagleEntity::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.6f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "beagle").toString()));

    public static final Supplier<EntityType<RottweilerEntity>> ROTTWEILER =
            ENTITY_TYPES.register("rottweiler",
                    () -> EntityType.Builder.of(RottweilerEntity::new, MobCategory.CREATURE)
                            .sized(0.65f, 0.9f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "rottweiler").toString()));

    public static final Supplier<EntityType<IrishSetterEntity>> IRISH_SETTER =
            ENTITY_TYPES.register("irish_setter",
                    () -> EntityType.Builder.of(IrishSetterEntity::new, MobCategory.CREATURE)
                            .sized(0.7f, 1.0f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "irish_setter").toString()));

    public static final Supplier<EntityType<GermanSpitzEntity>> GERMAN_SPITZ =
            ENTITY_TYPES.register("german_spitz",
                    () -> EntityType.Builder.of(GermanSpitzEntity::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.6f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "german_spitz").toString()));

    public static final Supplier<EntityType<WhippetEntity>> WHIPPET =
            ENTITY_TYPES.register("whippet",
                    () -> EntityType.Builder.of(WhippetEntity::new, MobCategory.CREATURE)
                            .sized(0.55f, 0.8f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "whippet").toString()));

    @SubscribeEvent
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
