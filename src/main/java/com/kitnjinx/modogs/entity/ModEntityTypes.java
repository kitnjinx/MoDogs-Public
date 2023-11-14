package com.kitnjinx.modogs.entity;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, MoDogs.MOD_ID);

    public static final RegistryObject<EntityType<GermanShepherdEntity>> GERMAN_SHEPHERD =
            ENTITY_TYPES.register("german_shepherd",
            () -> EntityType.Builder.of(GermanShepherdEntity::new, MobCategory.CREATURE)
                    .sized(0.65f, 0.9f)
                    .build(new ResourceLocation(MoDogs.MOD_ID, "german_shepherd").toString()));

    public static final RegistryObject<EntityType<BorderCollieEntity>> BORDER_COLLIE =
            ENTITY_TYPES.register("border_collie",
                    () -> EntityType.Builder.of(BorderCollieEntity::new, MobCategory.CREATURE)
                            .sized(0.55f, 0.8f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "border_collie").toString()));

    public static final RegistryObject<EntityType<GoldenRetrieverEntity>> GOLDEN_RETRIEVER =
            ENTITY_TYPES.register("golden_retriever",
                    () -> EntityType.Builder.of(GoldenRetrieverEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "golden_retriever").toString()));

    public static final RegistryObject<EntityType<LabRetrieverEntity>> LAB_RETRIEVER =
            ENTITY_TYPES.register("lab_retriever",
                    () -> EntityType.Builder.of(LabRetrieverEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "lab_retriever").toString()));

    public static final RegistryObject<EntityType<DachshundEntity>> DACHSHUND =
            ENTITY_TYPES.register("dachshund",
                    () -> EntityType.Builder.of(DachshundEntity::new, MobCategory.CREATURE)
                            .sized(0.3f, 0.5f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "dachshund").toString()));

    public static final RegistryObject<EntityType<DalmatianEntity>> DALMATIAN =
            ENTITY_TYPES.register("dalmatian",
                    () -> EntityType.Builder.of(DalmatianEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "dalmatian").toString()));

    public static final RegistryObject<EntityType<CardiganCorgiEntity>> CARDIGAN_CORGI =
            ENTITY_TYPES.register("cardigan_corgi",
                    () -> EntityType.Builder.of(CardiganCorgiEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "cardigan_corgi").toString()));

    public static final RegistryObject<EntityType<PembrokeCorgiEntity>> PEMBROKE_CORGI =
            ENTITY_TYPES.register("pembroke_corgi",
                    () -> EntityType.Builder.of(PembrokeCorgiEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "pembroke_corgi").toString()));

    public static final RegistryObject<EntityType<RussellTerrierEntity>> RUSSELL_TERRIER =
            ENTITY_TYPES.register("russell_terrier",
                    () -> EntityType.Builder.of(RussellTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "russell_terrier").toString()));

    public static final RegistryObject<EntityType<AlaskanMalamuteEntity>> ALASKAN_MALAMUTE =
            ENTITY_TYPES.register("alaskan_malamute",
                    () -> EntityType.Builder.of(AlaskanMalamuteEntity::new, MobCategory.CREATURE)
                            .sized(0.65f, 0.9f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "alaskan_malamute").toString()));

    public static final RegistryObject<EntityType<BerneseMountainDogEntity>> BERNESE_MOUNTAIN_DOG =
            ENTITY_TYPES.register("bernese_mountain_dog",
                    () -> EntityType.Builder.of(BerneseMountainDogEntity::new, MobCategory.CREATURE)
                            .sized(0.7f, 0.95f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "bernese_mountain_dog").toString()));

    public static final RegistryObject<EntityType<SaintBernardEntity>> SAINT_BERNARD =
            ENTITY_TYPES.register("saint_bernard",
                    () -> EntityType.Builder.of(SaintBernardEntity::new, MobCategory.CREATURE)
                            .sized(0.75f, 1.0f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "saint_bernard").toString()));

    public static final RegistryObject<EntityType<BloodhoundEntity>> BLOODHOUND =
            ENTITY_TYPES.register("bloodhound",
                    () -> EntityType.Builder.of(BloodhoundEntity::new, MobCategory.CREATURE)
                            .sized(0.675f, 0.925f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "bloodhound").toString()));

    public static final RegistryObject<EntityType<BoxerEntity>> BOXER =
            ENTITY_TYPES.register("boxer",
                    () -> EntityType.Builder.of(BoxerEntity::new, MobCategory.CREATURE)
                            .sized(0.625f, 0.875f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "boxer").toString()));

    public static final RegistryObject<EntityType<GreyhoundEntity>> GREYHOUND =
            ENTITY_TYPES.register("greyhound",
                    () -> EntityType.Builder.of(GreyhoundEntity::new, MobCategory.CREATURE)
                            .sized(0.75f, 1.0f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "greyhound").toString()));

    public static final RegistryObject<EntityType<PitBullEntity>> PIT_BULL =
            ENTITY_TYPES.register("pit_bull",
                    () -> EntityType.Builder.of(PitBullEntity::new, MobCategory.CREATURE)
                            .sized(0.55f, 0.8f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "pit_bull").toString()));

    public static final RegistryObject<EntityType<GreatDaneEntity>> GREAT_DANE =
            ENTITY_TYPES.register("great_dane",
                    () -> EntityType.Builder.of(GreatDaneEntity::new, MobCategory.CREATURE)
                            .sized(0.8f, 1.05f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "great_dane").toString()));

    public static final RegistryObject<EntityType<MastiffEntity>> MASTIFF =
            ENTITY_TYPES.register("mastiff",
                    () -> EntityType.Builder.of(MastiffEntity::new, MobCategory.CREATURE)
                            .sized(0.8f, 1.05f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "mastiff").toString()));

    public static final RegistryObject<EntityType<ShibaInuEntity>> SHIBA_INU =
            ENTITY_TYPES.register("shiba_inu",
                    () -> EntityType.Builder.of(ShibaInuEntity::new, MobCategory.CREATURE)
                            .sized(0.45f, 0.45f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "shiba_inu").toString()));

    public static final RegistryObject<EntityType<ShetlandSheepdogEntity>> SHETLAND_SHEEPDOG =
            ENTITY_TYPES.register("shetland_sheepdog",
                    () -> EntityType.Builder.of(ShetlandSheepdogEntity::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.6f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "shetland_sheepdog").toString()));

    public static final RegistryObject<EntityType<BostonTerrierEntity>> BOSTON_TERRIER =
            ENTITY_TYPES.register("boston_terrier",
                    () -> EntityType.Builder.of(BostonTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "boston_terrier").toString()));

    public static final RegistryObject<EntityType<ScottishTerrierEntity>> SCOTTISH_TERRIER =
            ENTITY_TYPES.register("scottish_terrier",
                    () -> EntityType.Builder.of(ScottishTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.3f, 0.5f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "scottish_terrier").toString()));

    public static final RegistryObject<EntityType<CKCharlesSpanielEntity>> CK_CHARLES_SPANIEL =
            ENTITY_TYPES.register("ck_charles_spaniel",
                    () -> EntityType.Builder.of(CKCharlesSpanielEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "ck_charles_spaniel").toString()));

    public static final RegistryObject<EntityType<ItalianGreyhoundEntity>> ITALIAN_GREYHOUND =
            ENTITY_TYPES.register("italian_greyhound",
                    () -> EntityType.Builder.of(ItalianGreyhoundEntity::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.6f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "italian_greyhound").toString()));

    public static final RegistryObject<EntityType<AustralianShepherdEntity>> AUSTRALIAN_SHEPHERD =
            ENTITY_TYPES.register("australian_shepherd",
                    () -> EntityType.Builder.of(AustralianShepherdEntity::new, MobCategory.CREATURE)
                            .sized(0.55f, 0.8f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "australian_shepherd").toString()));

    public static final RegistryObject<EntityType<BasenjiEntity>> BASENJI =
            ENTITY_TYPES.register("basenji",
                    () -> EntityType.Builder.of(BasenjiEntity::new, MobCategory.CREATURE)
                            .sized(0.45f, 0.65f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "basenji").toString()));

    public static final RegistryObject<EntityType<PugEntity>> PUG =
            ENTITY_TYPES.register("pug",
                    () -> EntityType.Builder.of(PugEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "pug").toString()));

    public static final RegistryObject<EntityType<CockerSpanielEntity>> COCKER_SPANIEL =
            ENTITY_TYPES.register("cocker_spaniel",
                    () -> EntityType.Builder.of(CockerSpanielEntity::new, MobCategory.CREATURE)
                            .sized(0.4f, 0.6f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "cocker_spaniel").toString()));

    public static final RegistryObject<EntityType<BullTerrierEntity>> BULL_TERRIER =
            ENTITY_TYPES.register("bull_terrier",
                    () -> EntityType.Builder.of(BullTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.85f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "bull_terrier").toString()));

    public static final RegistryObject<EntityType<MiniBullTerrierEntity>> MINI_BULL_TERRIER =
            ENTITY_TYPES.register("mini_bull_terrier",
                    () -> EntityType.Builder.of(MiniBullTerrierEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "mini_bull_terrier").toString()));

    public static final RegistryObject<EntityType<SchnauzerEntity>> SCHNAUZER =
            ENTITY_TYPES.register("schnauzer",
                    () -> EntityType.Builder.of(SchnauzerEntity::new, MobCategory.CREATURE)
                            .sized(0.5f, 0.75f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "schnauzer").toString()));

    public static final RegistryObject<EntityType<MiniSchnauzerEntity>> MINI_SCHNAUZER =
            ENTITY_TYPES.register("mini_schnauzer",
                    () -> EntityType.Builder.of(MiniSchnauzerEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "mini_schnauzer").toString()));

    public static final RegistryObject<EntityType<PoodleEntity>> POODLE =
            ENTITY_TYPES.register("poodle",
                    () -> EntityType.Builder.of(PoodleEntity::new, MobCategory.CREATURE)
                            .sized(0.5f, 0.75f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "poodle").toString()));

    public static final RegistryObject<EntityType<ToyPoodleEntity>> TOY_POODLE =
            ENTITY_TYPES.register("toy_poodle",
                    () -> EntityType.Builder.of(ToyPoodleEntity::new, MobCategory.CREATURE)
                            .sized(0.35f, 0.55f)
                            .build(new ResourceLocation(MoDogs.MOD_ID, "toy_poodle").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
