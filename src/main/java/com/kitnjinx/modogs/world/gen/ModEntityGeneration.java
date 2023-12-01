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
                7, 1, 2, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                7, 1, 2, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                15, 1, 2, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                15, 1, 2, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                15, 1, 2, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SHEPHERD.get(),
                3, 1, 2, MEADOW);

        // BORDER COLLIE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                15, 2, 3, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                15, 2, 3, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                7, 2, 3, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                7, 2, 3, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                3, 2, 3, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BORDER_COLLIE.get(),
                15, 2, 3, MEADOW);

        // GOLDEN RETRIEVER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                15, 1, 2, SWAMP);
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                3, 1, 2, SPARSE_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                15, 1, 2, RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                7, 1, 2, FROZEN_RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                15, 1, 2, BEACH);
        addEntityToSpecificBiomes(event, ModEntityTypes.GOLDEN_RETRIEVER.get(),
                7, 1, 2, SNOWY_BEACH);

        // LAB RETRIEVER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                15, 1, 2, STONY_SHORE);
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                3, 1, 2, SWAMP);
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                15, 1, 2, RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                7, 1, 2, FROZEN_RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                15, 1, 2, BEACH);
        addEntityToSpecificBiomes(event, ModEntityTypes.LAB_RETRIEVER.get(),
                7, 1, 2, SNOWY_BEACH);

        // DACHSHUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                3, 2, 2, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                7, 2, 2, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                15, 2, 2, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                15, 2, 2, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                15, 2, 2, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DACHSHUND.get(),
                7, 2, 2, MEADOW);

        // DALMATION SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                15, 1, 2, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                15, 1, 2, SAVANNA_PLATEAU);
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                7, 1, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                7, 1, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                3, 1, 2, WINDSWEPT_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.DALMATIAN.get(),
                15, 1, 2, WINDSWEPT_SAVANNA);

        // CARDIGAN CORGI SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                7, 2, 3, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                7, 2, 3, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                3, 2, 3, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                15, 2, 3, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                15, 2, 3, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.CARDIGAN_CORGI.get(),
                15, 2, 3, TAIGA);

        // PEMBROKE CORGI SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                15, 2, 3, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                15, 2, 3, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                3, 2, 3, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                7, 2, 3, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                7, 2, 3, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.PEMBROKE_CORGI.get(),
                15, 2, 3, TAIGA);

        // RUSSELL TERRIER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.RUSSELL_TERRIER.get(),
                15, 1, 3, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.RUSSELL_TERRIER.get(),
                7, 1, 3, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.RUSSELL_TERRIER.get(),
                15, 1, 3, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.RUSSELL_TERRIER.get(),
                15, 1, 3, SNOWY_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.RUSSELL_TERRIER.get(),
                7, 1, 3, SNOWY_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.RUSSELL_TERRIER.get(),
                3, 1, 3, GROVE);

        // ALASKAN MALAMUTE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                15, 1, 1, SNOWY_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                3, 1, 1, ICE_SPIKES);
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                15, 1, 1, SNOWY_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                15, 1, 1, SNOWY_SLOPES);
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                7, 1, 1, FROZEN_RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.ALASKAN_MALAMUTE.get(),
                7, 1, 1, SNOWY_BEACH);

        // BERNESE MOUNTAIN DOG SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                15, 1, 2, MEADOW);
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                3, 1, 2, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                15, 1, 2, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                15, 1, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                7, 1, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                7, 1, 2, WINDSWEPT_FOREST);

        // SAINT BERNARD SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                3, 1, 1, ICE_SPIKES);
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                7, 1, 1, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                7, 1, 1, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                15, 1, 1, FROZEN_PEAKS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                15, 1, 1, JAGGED_PEAKS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SAINT_BERNARD.get(),
                15, 1, 1, STONY_PEAKS);

        // BLOODHOUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                3, 1, 1, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                7, 1, 1, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                7, 1, 1, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                15, 1, 1, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                15, 1, 1, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BLOODHOUND.get(),
                15, 1, 1, JUNGLE);

        // BOXER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                15, 2, 2, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                15, 2, 2, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                7, 2, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                7, 2, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                3, 2, 2, MEADOW);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOXER.get(),
                15, 2, 2, SPARSE_JUNGLE);

        // GREYHOUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                15, 1, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                15, 1, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                3, 1, 2, SPARSE_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                7, 1, 2, BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                7, 1, 2, WOODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREYHOUND.get(),
                15, 1, 2, SAVANNA);

        // PIT BULL SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                15, 1, 1, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                7, 1, 1, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                7, 1, 1, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                3, 1, 1, SAVANNA_PLATEAU);
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                15, 1, 1, WINDSWEPT_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PIT_BULL.get(),
                15, 1, 1, JUNGLE);

        // GREAT DANE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                15, 1, 1, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                15, 1, 1, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                15, 1, 1, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                7, 1, 1, WOODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                7, 1, 1, ERODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GREAT_DANE.get(),
                3, 1, 1, MEADOW);

        // GREAT DANE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                15, 1, 1, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                15, 1, 1, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                3, 1, 1, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                7, 1, 1, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                7, 1, 1, WINDSWEPT_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.MASTIFF.get(),
                15, 1, 1, WINDSWEPT_SAVANNA);

        // SHIBA INU SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                3, 1, 2, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                7, 1, 2, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                7, 1, 2, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                15, 1, 2, JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                15, 1, 2, BAMBOO_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHIBA_INU.get(),
                15, 1, 2, SPARSE_JUNGLE);

        // SHETLAND SHEEPDOG SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                7, 1, 2, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                7, 1, 2, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                15, 1, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                15, 1, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                3, 1, 2, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                7, 1, 2, MEADOW);

        // BOSTON TERRIER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                15, 2, 2, DESERT);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                7, 2, 2, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                7, 2, 2, SAVANNA_PLATEAU);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                15, 2, 2, BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                15, 2, 2, ERODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.BOSTON_TERRIER.get(),
                3, 2, 2, WOODED_BADLANDS);

        // SCOTTISH TERRIER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                7, 1, 2, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                7, 1, 2, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                15, 1, 2, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                15, 1, 2, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                15, 1, 2, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCOTTISH_TERRIER.get(),
                3, 1, 2, BIRCH_FOREST);

        // CAV. KING CHARLES SPANIEL SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.CK_CHARLES_SPANIEL.get(),
                15, 1, 2, SNOWY_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.CK_CHARLES_SPANIEL.get(),
                7, 1, 2, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.CK_CHARLES_SPANIEL.get(),
                15, 1, 2, SNOWY_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.CK_CHARLES_SPANIEL.get(),
                15, 1, 2, SNOWY_SLOPES);
        addEntityToSpecificBiomes(event, ModEntityTypes.CK_CHARLES_SPANIEL.get(),
                3, 1, 2, MEADOW);
        addEntityToSpecificBiomes(event, ModEntityTypes.CK_CHARLES_SPANIEL.get(),
                7, 1, 2, ICE_SPIKES);

        // ITALIAN GREYHOUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.ITALIAN_GREYHOUND.get(),
                15, 1, 2, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.ITALIAN_GREYHOUND.get(),
                15, 1, 2, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.ITALIAN_GREYHOUND.get(),
                3, 1, 2, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.ITALIAN_GREYHOUND.get(),
                15, 1, 2, BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.ITALIAN_GREYHOUND.get(),
                7, 1, 2, WOODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.ITALIAN_GREYHOUND.get(),
                7, 1, 2, ERODED_BADLANDS);

        // AUSTRALIAN SHEPHERD SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.AUSTRALIAN_SHEPHERD.get(),
                15, 1, 2, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.AUSTRALIAN_SHEPHERD.get(),
                15, 1, 2, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.AUSTRALIAN_SHEPHERD.get(),
                7, 1, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.AUSTRALIAN_SHEPHERD.get(),
                7, 1, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.AUSTRALIAN_SHEPHERD.get(),
                3, 1, 2, WINDSWEPT_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.AUSTRALIAN_SHEPHERD.get(),
                15, 1, 2, MEADOW);

        // BASENJI SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BASENJI.get(),
                15, 1, 1, DESERT);
        addEntityToSpecificBiomes(event, ModEntityTypes.BASENJI.get(),
                15, 1, 1, SWAMP);
        addEntityToSpecificBiomes(event, ModEntityTypes.BASENJI.get(),
                15, 1, 1, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BASENJI.get(),
                7, 1, 1, SAVANNA_PLATEAU);
        addEntityToSpecificBiomes(event, ModEntityTypes.BASENJI.get(),
                7, 1, 1, JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.BASENJI.get(),
                3, 1, 1, BADLANDS);

        // PUG SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.PUG.get(),
                3, 1, 1, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PUG.get(),
                15, 2, 2, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PUG.get(),
                15, 2, 2, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.PUG.get(),
                7, 2, 2, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.PUG.get(),
                7, 2, 2, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.PUG.get(),
                7, 2, 2, TAIGA);

        // COCKER SPANIEL SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.COCKER_SPANIEL.get(),
                7, 1, 1, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.COCKER_SPANIEL.get(),
                7, 1, 1, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.COCKER_SPANIEL.get(),
                15, 1, 1, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.COCKER_SPANIEL.get(),
                15, 1, 1, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.COCKER_SPANIEL.get(),
                15, 1, 1, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.COCKER_SPANIEL.get(),
                3, 1, 1, MEADOW);

        // BULL TERRIER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BULL_TERRIER.get(),
                15, 1, 1, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BULL_TERRIER.get(),
                15, 1, 1, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BULL_TERRIER.get(),
                15, 1, 1, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BULL_TERRIER.get(),
                3, 1, 1, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BULL_TERRIER.get(),
                7, 1, 1, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BULL_TERRIER.get(),
                7, 1, 1, SAVANNA_PLATEAU);

        // MINI BULL TERRIER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_BULL_TERRIER.get(),
                3, 1, 1, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_BULL_TERRIER.get(),
                7, 1, 1, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_BULL_TERRIER.get(),
                7, 1, 1, SAVANNA_PLATEAU);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_BULL_TERRIER.get(),
                15, 1, 1, WINDSWEPT_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_BULL_TERRIER.get(),
                15, 1, 1, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_BULL_TERRIER.get(),
                15, 1, 1, WINDSWEPT_SAVANNA);

        // SCHNAUZER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.SCHNAUZER.get(),
                15, 1, 1, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCHNAUZER.get(),
                15, 1, 1, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCHNAUZER.get(),
                3, 1, 1, DESERT);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCHNAUZER.get(),
                7, 1, 1, SPARSE_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCHNAUZER.get(),
                7, 1, 1, JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCHNAUZER.get(),
                15, 1, 1, MEADOW);

        // MINI SCHNAUZER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_SCHNAUZER.get(),
                15, 1, 1, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_SCHNAUZER.get(),
                15, 1, 1, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCHNAUZER.get(),
                3, 1, 1, DESERT);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_SCHNAUZER.get(),
                7, 1, 1, SPARSE_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.SCHNAUZER.get(),
                7, 1, 1, JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_SCHNAUZER.get(),
                15, 1, 1, FLOWER_FOREST);

        // POODLE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.POODLE.get(),
                15, 1, 2, SWAMP);
        addEntityToSpecificBiomes(event, ModEntityTypes.POODLE.get(),
                15, 1, 2, RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.POODLE.get(),
                3, 1, 2, FROZEN_RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.POODLE.get(),
                15, 1, 2, BEACH);
        addEntityToSpecificBiomes(event, ModEntityTypes.POODLE.get(),
                7, 1, 2, SNOWY_BEACH);
        addEntityToSpecificBiomes(event, ModEntityTypes.POODLE.get(),
                7, 1, 2, STONY_SHORE);

        // TOY POODLE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.TOY_POODLE.get(),
                15, 2, 2, SWAMP);
        addEntityToSpecificBiomes(event, ModEntityTypes.TOY_POODLE.get(),
                15, 2, 2, RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.TOY_POODLE.get(),
                3, 2, 2, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.TOY_POODLE.get(),
                15, 2, 2, BEACH);
        addEntityToSpecificBiomes(event, ModEntityTypes.TOY_POODLE.get(),
                7, 2, 2, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.TOY_POODLE.get(),
                7, 2, 2, STONY_SHORE);

        // DOBERMAN SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.DOBERMAN.get(),
                15, 1, 1, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.DOBERMAN.get(),
                7, 1, 1, DESERT);
        addEntityToSpecificBiomes(event, ModEntityTypes.DOBERMAN.get(),
                7, 1, 1, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DOBERMAN.get(),
                15, 1, 1, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.DOBERMAN.get(),
                3, 1, 1, SNOWY_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.DOBERMAN.get(),
                15, 1, 1, MEADOW);

        // MINI PINSCHER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_PINSCHER.get(),
                15, 1, 1, SPARSE_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_PINSCHER.get(),
                7, 1, 1, DESERT);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_PINSCHER.get(),
                7, 1, 1, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_PINSCHER.get(),
                15, 1, 1, JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_PINSCHER.get(),
                3, 1, 1, SNOWY_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MINI_PINSCHER.get(),
                15, 1, 1, BAMBOO_JUNGLE);

        // HUSKY SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.HUSKY.get(),
                15, 2, 2, SNOWY_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.HUSKY.get(),
                7, 2, 2, ICE_SPIKES);
        addEntityToSpecificBiomes(event, ModEntityTypes.HUSKY.get(),
                7, 2, 2, SNOWY_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.HUSKY.get(),
                3, 2, 2, GROVE);
        addEntityToSpecificBiomes(event, ModEntityTypes.HUSKY.get(),
                15, 2, 2, SNOWY_SLOPES);
        addEntityToSpecificBiomes(event, ModEntityTypes.HUSKY.get(),
                15, 2, 2, SNOWY_BEACH);

        // REDBONE COONHOUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                3, 2, 2, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                7, 2, 2, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                15, 2, 2, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                15, 2, 2, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                15, 2, 2, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                7, 2, 2, SNOWY_TAIGA);

        // TREE WALK HOUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                15, 2, 2, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                15, 2, 2, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                7, 2, 2, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                7, 2, 2, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                3, 2, 2, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.REDBONE_COONHOUND.get(),
                15, 2, 2, FOREST);

        // AIREDALE TERRIER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.AIREDALE_TERRIER.get(),
                15, 1, 1, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.AIREDALE_TERRIER.get(),
                15, 1, 1, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.AIREDALE_TERRIER.get(),
                7, 1, 1, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.AIREDALE_TERRIER.get(),
                3, 1, 1, SAVANNA_PLATEAU);
        addEntityToSpecificBiomes(event, ModEntityTypes.AIREDALE_TERRIER.get(),
                15, 1, 1, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.AIREDALE_TERRIER.get(),
                7, 1, 1, WINDSWEPT_GRAVELLY_HILLS);

        // AMERICAN FOXHOUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.AMERICAN_FOXHOUND.get(),
                3, 1, 2, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.AMERICAN_FOXHOUND.get(),
                7, 1, 2, SNOWY_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.AMERICAN_FOXHOUND.get(),
                7, 1, 2, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.AMERICAN_FOXHOUND.get(),
                15, 1, 2, OLD_GROWTH_SPRUCE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.AMERICAN_FOXHOUND.get(),
                15, 1, 2, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.AMERICAN_FOXHOUND.get(),
                15, 1, 2, TAIGA);

        // BULLDOG SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BULLDOG.get(),
                15, 1, 2, FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BULLDOG.get(),
                15, 1, 2, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BULLDOG.get(),
                3, 1, 2, BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BULLDOG.get(),
                7, 1, 2, OLD_GROWTH_BIRCH_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BULLDOG.get(),
                7, 1, 2, DARK_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BULLDOG.get(),
                15, 1, 2, SPARSE_JUNGLE);

        // COLLIE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.COLLIE.get(),
                7, 1, 1, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.COLLIE.get(),
                7, 1, 1, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.COLLIE.get(),
                15, 1, 1, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.COLLIE.get(),
                15, 1, 1, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.COLLIE.get(),
                15, 1, 1, WINDSWEPT_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.COLLIE.get(),
                3, 1, 1, MEADOW);

        // MUDI SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.MUDI.get(),
                15, 1, 1, PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MUDI.get(),
                15, 1, 1, SUNFLOWER_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MUDI.get(),
                3, 1, 1, SNOWY_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MUDI.get(),
                7, 1, 1, BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MUDI.get(),
                7, 1, 1, WOODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.MUDI.get(),
                15, 1, 1, MEADOW);

        // NORWEGIAN ELKHOUND SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.NORWEGIAN_ELKHOUND.get(),
                15, 1, 1, SNOWY_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.NORWEGIAN_ELKHOUND.get(),
                15, 1, 1, ICE_SPIKES);
        addEntityToSpecificBiomes(event, ModEntityTypes.NORWEGIAN_ELKHOUND.get(),
                7, 1, 1, SNOWY_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.NORWEGIAN_ELKHOUND.get(),
                15, 1, 1, FROZEN_RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.NORWEGIAN_ELKHOUND.get(),
                7, 1, 1, BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.NORWEGIAN_ELKHOUND.get(),
                3, 1, 1, ERODED_BADLANDS);

        // BEAGLE SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.BEAGLE.get(),
                3, 2, 3, DESERT);
        addEntityToSpecificBiomes(event, ModEntityTypes.BEAGLE.get(),
                7, 2, 3, FLOWER_FOREST);
        addEntityToSpecificBiomes(event, ModEntityTypes.BEAGLE.get(),
                15, 2, 3, TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BEAGLE.get(),
                7, 2, 3, MEADOW);
        addEntityToSpecificBiomes(event, ModEntityTypes.BEAGLE.get(),
                15, 2, 3, OLD_GROWTH_PINE_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.BEAGLE.get(),
                15, 2, 3, OLD_GROWTH_SPRUCE_TAIGA);

        // ROTTWEILER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.ROTTWEILER.get(),
                7, 1, 1, DESERT);
        addEntityToSpecificBiomes(event, ModEntityTypes.ROTTWEILER.get(),
                7, 1, 1, SAVANNA);
        addEntityToSpecificBiomes(event, ModEntityTypes.ROTTWEILER.get(),
                3, 1, 1, SAVANNA_PLATEAU);
        addEntityToSpecificBiomes(event, ModEntityTypes.ROTTWEILER.get(),
                15, 1, 1, BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.ROTTWEILER.get(),
                15, 1, 1, ERODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.ROTTWEILER.get(),
                15, 1, 1, WOODED_BADLANDS);

        // IRISH SETTER SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.IRISH_SETTER.get(),
                15, 1, 1, SWAMP);
        addEntityToSpecificBiomes(event, ModEntityTypes.IRISH_SETTER.get(),
                7, 1, 1, JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.IRISH_SETTER.get(),
                7, 1, 1, SPARSE_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.IRISH_SETTER.get(),
                3, 1, 1, RIVER);
        addEntityToSpecificBiomes(event, ModEntityTypes.IRISH_SETTER.get(),
                15, 1, 1, BEACH);
        addEntityToSpecificBiomes(event, ModEntityTypes.IRISH_SETTER.get(),
                15, 1, 1, STONY_SHORE);

        // GERMAN SPITZ SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SPITZ.get(),
                15, 1, 1, SNOWY_PLAINS);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SPITZ.get(),
                15, 1, 1, ICE_SPIKES);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SPITZ.get(),
                15, 1, 1, SNOWY_TAIGA);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SPITZ.get(),
                7, 1, 1, MEADOW);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SPITZ.get(),
                7, 1, 1, SNOWY_BEACH);
        addEntityToSpecificBiomes(event, ModEntityTypes.GERMAN_SPITZ.get(),
                3, 1, 1, JUNGLE);

        // WHIPPET SPAWNING AREAS
        addEntityToSpecificBiomes(event, ModEntityTypes.WHIPPET.get(),
                7, 1, 2, WINDSWEPT_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.WHIPPET.get(),
                7, 1, 2, WINDSWEPT_GRAVELLY_HILLS);
        addEntityToSpecificBiomes(event, ModEntityTypes.WHIPPET.get(),
                3, 1, 2, SPARSE_JUNGLE);
        addEntityToSpecificBiomes(event, ModEntityTypes.WHIPPET.get(),
                15, 1, 2, BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.WHIPPET.get(),
                15, 1, 2, WOODED_BADLANDS);
        addEntityToSpecificBiomes(event, ModEntityTypes.WHIPPET.get(),
                15, 1, 2, SAVANNA);
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