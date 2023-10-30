package com.kitnjinx.modogs.world.gen;

import com.kitnjinx.modogs.entity.ModEntityTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.level.biome.Biomes.*;

public class ModEntityGeneration {
    public static void onEntitySpawn(final BiomeLoadingEvent event) {
        /*
        // GENERIC, HIGH WEIGHT ENTITY SPAWNER, GOOD FOR TESTING
        addEntityToAllOverworldBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                40, 2, 3);
         */


        // GERMAN SHEPHERD SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                20, 1, 2, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                20, 1, 2, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                30, 1, 2, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                30, 1, 2, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                30, 1, 2, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                10, 1, 2, MEADOW);

        // BORDER COLLIE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                30, 2, 3, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                30, 2, 3, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                20, 2, 3, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                20, 2, 3, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                10, 2, 3, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                30, 2, 3, MEADOW);

        // GOLDEN RETRIEVER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                30, 1, 2, SWAMP);
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                10, 1, 2, SPARSE_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                30, 1, 2, RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                20, 1, 2, FROZEN_RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                30, 1, 2, BEACH);
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                20, 1, 2, SNOWY_BEACH);

        // LAB RETRIEVER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                30, 1, 2, STONY_SHORE);
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                10, 1, 2, SWAMP);
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                30, 1, 2, RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                20, 1, 2, FROZEN_RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                30, 1, 2, BEACH);
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                20, 1, 2, SNOWY_BEACH);

        // DACHSHUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                10, 2, 2, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                20, 2, 2, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                30, 2, 2, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                30, 2, 2, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                30, 2, 2, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                20, 2, 2, MEADOW);

        // DALMATION SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                30, 1, 2, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                30, 1, 2, SAVANNA_PLATEAU);
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                20, 1, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                20, 1, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                10, 1, 2, WINDSWEPT_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                30, 1, 2, WINDSWEPT_SAVANNA);

        // CARDIGAN CORGI SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                20, 2, 3, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                20, 2, 3, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                10, 2, 3, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                30, 2, 3, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                30, 2, 3, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                30, 2, 3, TAIGA);

        // PEMBROKE CORGI SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                30, 2, 3, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                30, 2, 3, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                10, 2, 3, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                20, 2, 3, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                20, 2, 3, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                30, 2, 3, TAIGA);

        // RUSSELL TERRIER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.RUSSELL_TERRIER.get(),
                20, 1, 3, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.RUSSELL_TERRIER.get(),
                20, 1, 3, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.RUSSELL_TERRIER.get(),
                30, 1, 3, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.RUSSELL_TERRIER.get(),
                30, 1, 3, SNOWY_TAIGA);

        // ALASKAN MALAMUTE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                30, 1, 1, SNOWY_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                10, 1, 1, ICE_SPIKES);
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                30, 1, 1, SNOWY_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                30, 1, 1, SNOWY_SLOPES);
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                20, 1, 1, FROZEN_RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                20, 1, 1, SNOWY_BEACH);

        // BERNESE MOUNTAIN DOG SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                30, 1, 2, MEADOW);
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                10, 1, 2, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                30, 1, 2, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                30, 1, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                20, 1, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                20, 1, 2, WINDSWEPT_FOREST);

        // SAINT BERNARD SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                10, 1, 1, ICE_SPIKES);
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                20, 1, 1, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                20, 1, 1, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                30, 1, 1, FROZEN_PEAKS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                30, 1, 1, JAGGED_PEAKS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                30, 1, 1, STONY_PEAKS);

        // BLOODHOUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                10, 1, 1, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                20, 1, 1, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                20, 1, 1, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                30, 1, 1, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                30, 1, 1, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                30, 1, 1, JUNGLE);

        // BOXER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                30, 2, 2, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                30, 2, 2, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                20, 2, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                20, 2, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                10, 2, 2, MEADOW);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                30, 2, 2, SPARSE_JUNGLE);

        // GREYHOUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                30, 1, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                30, 1, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                10, 1, 2, SPARSE_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                20, 1, 2, BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                20, 1, 2, WOODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                10, 1, 2, SAVANNA);

        // PIT BULL SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                30, 1, 1, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                20, 1, 1, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                20, 1, 1, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                10, 1, 1, SAVANNA_PLATEAU);
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                30, 1, 1, WINDSWEPT_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                30, 1, 1, JUNGLE);

        // GREAT DANE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                30, 1, 1, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                30, 1, 1, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                30, 1, 1, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                20, 1, 1, WOODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                20, 1, 1, ERODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                10, 1, 1, MEADOW);

        // GREAT DANE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                30, 1, 1, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                30, 1, 1, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                10, 1, 1, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                20, 1, 1, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                20, 1, 1, WINDSWEPT_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                30, 1, 1, WINDSWEPT_SAVANNA);

        // SHIBA INU SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                10, 1, 2, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                20, 1, 2, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                20, 1, 2, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                30, 1, 2, JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                30, 1, 2, BAMBOO_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                30, 1, 2, SPARSE_JUNGLE);

        // SHETLAND SHEEPDOG SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                20, 1, 2, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                20, 1, 2, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                30, 1, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                30, 1, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                10, 1, 2, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                20, 1, 2, MEADOW);

        // BOSTON TERRIER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                30, 2, 2, DESERT);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                20, 2, 2, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                20, 2, 2, SAVANNA_PLATEAU);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                30, 2, 2, BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                30, 2, 2, ERODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                10, 2, 2, WOODED_BADLANDS);

        // SCOTTISH TERRIER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                20, 1, 2, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                20, 1, 2, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                30, 1, 2, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                30, 1, 2, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                30, 1, 2, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                10, 1, 2, BIRCH_FOREST);
    }

    private static void addEntityToAllBiomesExceptThese(BiomeLoadingEvent event, EntityType<?> type,
                                                        int weight, int minCount, int maxCount, ResourceKey<Biome>... biomes) {
        // Goes through each entry in the biomes and sees if it matches the current biome we are loading
        boolean isBiomeSelected = Arrays.stream(biomes).map(ResourceKey::location)
                .map(Object::toString).anyMatch(s -> s.equals(event.getName().toString()));

        if(!isBiomeSelected) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    @SafeVarargs
    private static void addEntityToSpecificBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                                  int weight, int minCount, int maxCount, ResourceKey<Biome>... biomes) {
        // Goes through each entry in the biomes and sees if it matches the current biome we are loading
        boolean isBiomeSelected = Arrays.stream(biomes).map(ResourceKey::location)
                .map(Object::toString).anyMatch(s -> s.equals(event.getName().toString()));

        if(isBiomeSelected) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    private static void addEntityToAllOverworldBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                                      int weight, int minCount, int maxCount) {
        if(!event.getCategory().equals(Biome.BiomeCategory.THEEND) && !event.getCategory().equals(Biome.BiomeCategory.NETHER)) {
            addEntityToAllBiomes(event, type, weight, minCount, maxCount);
        }
    }

    private static void addEntityToAllBiomesNoNether(BiomeLoadingEvent event, EntityType<?> type,
                                                     int weight, int minCount, int maxCount) {
        if(!event.getCategory().equals(Biome.BiomeCategory.NETHER)) {
            List<MobSpawnSettings.SpawnerData> base = event.getSpawns().getSpawner(type.getCategory());
            base.add(new MobSpawnSettings.SpawnerData(type,weight, minCount, maxCount));
        }
    }

    private static void addEntityToAllBiomesNoEnd(BiomeLoadingEvent event, EntityType<?> type,
                                                  int weight, int minCount, int maxCount) {
        if(!event.getCategory().equals(Biome.BiomeCategory.THEEND)) {
            List<MobSpawnSettings.SpawnerData> base = event.getSpawns().getSpawner(type.getCategory());
            base.add(new MobSpawnSettings.SpawnerData(type,weight, minCount, maxCount));
        }
    }

    private static void addEntityToAllBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                             int weight, int minCount, int maxCount) {
        List<MobSpawnSettings.SpawnerData> base = event.getSpawns().getSpawner(type.getCategory());
        base.add(new MobSpawnSettings.SpawnerData(type,weight, minCount, maxCount));
    }
}