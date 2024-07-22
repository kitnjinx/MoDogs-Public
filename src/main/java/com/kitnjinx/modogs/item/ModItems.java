package com.kitnjinx.modogs.item;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.block.ModBlocks;
import com.kitnjinx.modogs.entity.ModEntityTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    // simplified explanation: list of items we're creating with the mod
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoDogs.MOD_ID);

    /* BLOCK ITEMS */
    public static final Supplier<BlockItem> CARE_STATION_BLOCK =
            ITEMS.registerSimpleBlockItem(ModBlocks.CARE_STATION);
    public static final Supplier<BlockItem> GENO_PRINTER_BLOCK =
            ITEMS.registerSimpleBlockItem(ModBlocks.GENO_PRINTER);

    /* DOG TREATS */
    public static final Supplier<Item> BACON_TREAT = ITEMS.register("bacon_treat",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BEEF_TREAT = ITEMS.register("beef_treat",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CHICKEN_TREAT = ITEMS.register("chicken_treat",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> MUTTON_TREAT = ITEMS.register("mutton_treat",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> RABBIT_TREAT = ITEMS.register("rabbit_treat",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> SALMON_TREAT = ITEMS.register("salmon_treat",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> HEALING_TREAT = ITEMS.register("healing_treat",
            () -> new Item(new Item.Properties()));

    /* GENE TESTING AND RELATED */
    public static final Supplier<Item> GENE_TESTER = ITEMS.register("gene_tester",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> GENO_READER = ITEMS.register("geno_reader",
            () -> new GenoReaderItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> PAPER_STRIP = ITEMS.register("paper_strip",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> USED_PAPER_STRIP = ITEMS.register("used_paper_strip",
            () -> new Item(new Item.Properties()));

    /* DOG COLLARS */
    public static final Supplier<Item> WHITE_COLLAR = ITEMS.register("white_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_GRAY_COLLAR = ITEMS.register("light_gray_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GRAY_COLLAR = ITEMS.register("gray_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLACK_COLLAR = ITEMS.register("black_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BROWN_COLLAR = ITEMS.register("brown_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> RED_COLLAR = ITEMS.register("red_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> ORANGE_COLLAR = ITEMS.register("orange_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> YELLOW_COLLAR = ITEMS.register("yellow_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIME_COLLAR = ITEMS.register("lime_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GREEN_COLLAR = ITEMS.register("green_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CYAN_COLLAR = ITEMS.register("cyan_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_BLUE_COLLAR = ITEMS.register("light_blue_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLUE_COLLAR = ITEMS.register("blue_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PURPLE_COLLAR = ITEMS.register("purple_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> MAGENTA_COLLAR = ITEMS.register("magenta_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PINK_COLLAR = ITEMS.register("pink_collar",
            () -> new Item(new Item.Properties()));

    /* REINFORCED DOG COLLARS */
    public static final Supplier<Item> WHITE_REINFORCED_COLLAR = ITEMS.register("white_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_GRAY_REINFORCED_COLLAR = ITEMS.register(
            "light_gray_reinforced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GRAY_REINFORCED_COLLAR = ITEMS.register("gray_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLACK_REINFORCED_COLLAR = ITEMS.register("black_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BROWN_REINFORCED_COLLAR = ITEMS.register("brown_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> RED_REINFORCED_COLLAR = ITEMS.register("red_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> ORANGE_REINFORCED_COLLAR = ITEMS.register("orange_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> YELLOW_REINFORCED_COLLAR = ITEMS.register("yellow_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIME_REINFORCED_COLLAR = ITEMS.register("lime_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GREEN_REINFORCED_COLLAR = ITEMS.register("green_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CYAN_REINFORCED_COLLAR = ITEMS.register("cyan_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_BLUE_REINFORCED_COLLAR = ITEMS.register(
            "light_blue_reinforced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLUE_REINFORCED_COLLAR = ITEMS.register("blue_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PURPLE_REINFORCED_COLLAR = ITEMS.register("purple_reinforced_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> MAGENTA_REINFORCED_COLLAR = ITEMS.register(
            "magenta_reinforced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PINK_REINFORCED_COLLAR = ITEMS.register("pink_reinforced_collar",
            () -> new Item(new Item.Properties()));

    /* GOLD-PLATED DOG COLLARS */
    public static final Supplier<Item> WHITE_GOLD_PLATED_COLLAR = ITEMS.register("white_gold_plated_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_GRAY_GOLD_PLATED_COLLAR = ITEMS.register(
            "light_gray_gold_plated_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GRAY_GOLD_PLATED_COLLAR = ITEMS.register("gray_gold_plated_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLACK_GOLD_PLATED_COLLAR = ITEMS.register("black_gold_plated_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BROWN_GOLD_PLATED_COLLAR = ITEMS.register("brown_gold_plated_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> RED_GOLD_PLATED_COLLAR = ITEMS.register("red_gold_plated_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> ORANGE_GOLD_PLATED_COLLAR = ITEMS.register(
            "orange_gold_plated_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> YELLOW_GOLD_PLATED_COLLAR = ITEMS.register(
            "yellow_gold_plated_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIME_GOLD_PLATED_COLLAR = ITEMS.register("lime_gold_plated_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GREEN_GOLD_PLATED_COLLAR = ITEMS.register("green_gold_plated_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CYAN_GOLD_PLATED_COLLAR = ITEMS.register("cyan_gold_plated_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_BLUE_GOLD_PLATED_COLLAR = ITEMS.register(
            "light_blue_gold_plated_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLUE_GOLD_PLATED_COLLAR = ITEMS.register("blue_gold_plated_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PURPLE_GOLD_PLATED_COLLAR = ITEMS.register(
            "purple_gold_plated_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> MAGENTA_GOLD_PLATED_COLLAR = ITEMS.register(
            "magenta_gold_plated_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PINK_GOLD_PLATED_COLLAR = ITEMS.register("pink_gold_plated_collar",
            () -> new Item(new Item.Properties()));

    /* IRON-INFUSED DOG COLLARS */
    public static final Supplier<Item> WHITE_IRON_INFUSED_COLLAR = ITEMS.register(
            "white_iron_infused_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_GRAY_IRON_INFUSED_COLLAR = ITEMS.register(
            "light_gray_iron_infused_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GRAY_IRON_INFUSED_COLLAR = ITEMS.register("gray_iron_infused_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLACK_IRON_INFUSED_COLLAR = ITEMS.register(
            "black_iron_infused_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BROWN_IRON_INFUSED_COLLAR = ITEMS.register(
            "brown_iron_infused_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> RED_IRON_INFUSED_COLLAR = ITEMS.register("red_iron_infused_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> ORANGE_IRON_INFUSED_COLLAR = ITEMS.register(
            "orange_iron_infused_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> YELLOW_IRON_INFUSED_COLLAR = ITEMS.register(
            "yellow_iron_infused_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIME_IRON_INFUSED_COLLAR = ITEMS.register("lime_iron_infused_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GREEN_IRON_INFUSED_COLLAR = ITEMS.register(
            "green_iron_infused_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CYAN_IRON_INFUSED_COLLAR = ITEMS.register("cyan_iron_infused_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_BLUE_IRON_INFUSED_COLLAR = ITEMS.register(
            "light_blue_iron_infused_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLUE_IRON_INFUSED_COLLAR = ITEMS.register("blue_iron_infused_collar",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PURPLE_IRON_INFUSED_COLLAR = ITEMS.register(
            "purple_iron_infused_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> MAGENTA_IRON_INFUSED_COLLAR = ITEMS.register(
            "magenta_iron_infused_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PINK_IRON_INFUSED_COLLAR = ITEMS.register("pink_iron_infused_collar",
            () -> new Item(new Item.Properties()));

    /* DIAMOND-CRUSTED DOG COLLARS */
    public static final Supplier<Item> WHITE_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "white_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_GRAY_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "light_gray_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GRAY_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "gray_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLACK_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "black_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BROWN_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "brown_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> RED_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "red_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> ORANGE_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "orange_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> YELLOW_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "yellow_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIME_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "lime_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GREEN_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "green_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CYAN_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "cyan_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_BLUE_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "light_blue_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLUE_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "blue_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PURPLE_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "purple_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> MAGENTA_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "magenta_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PINK_DIAMOND_CRUSTED_COLLAR = ITEMS.register(
            "pink_diamond_crusted_collar", () -> new Item(new Item.Properties()));

    /* NETHERITE-LACED DOG COLLARS */
    public static final Supplier<Item> WHITE_NETHERITE_LACED_COLLAR = ITEMS.register(
            "white_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_GRAY_NETHERITE_LACED_COLLAR = ITEMS.register(
            "light_gray_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GRAY_NETHERITE_LACED_COLLAR = ITEMS.register(
            "gray_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLACK_NETHERITE_LACED_COLLAR = ITEMS.register(
            "black_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BROWN_NETHERITE_LACED_COLLAR = ITEMS.register(
            "brown_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> RED_NETHERITE_LACED_COLLAR = ITEMS.register(
            "red_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> ORANGE_NETHERITE_LACED_COLLAR = ITEMS.register(
            "orange_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> YELLOW_NETHERITE_LACED_COLLAR = ITEMS.register(
            "yellow_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIME_NETHERITE_LACED_COLLAR = ITEMS.register(
            "lime_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> GREEN_NETHERITE_LACED_COLLAR = ITEMS.register(
            "green_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CYAN_NETHERITE_LACED_COLLAR = ITEMS.register(
            "cyan_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> LIGHT_BLUE_NETHERITE_LACED_COLLAR = ITEMS.register(
            "light_blue_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> BLUE_NETHERITE_LACED_COLLAR = ITEMS.register(
            "blue_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PURPLE_NETHERITE_LACED_COLLAR = ITEMS.register(
            "purple_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> MAGENTA_NETHERITE_LACED_COLLAR = ITEMS.register(
            "magenta_netherite_laced_collar", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> PINK_NETHERITE_LACED_COLLAR = ITEMS.register(
            "pink_netherite_laced_collar", () -> new Item(new Item.Properties()));

    /* DOG SPAWN EGGS */
    public static final Supplier<Item> GERMAN_SHEPHERD_SPAWN_EGG = ITEMS.register(
            "german_shepherd_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.GERMAN_SHEPHERD,
                    0x9C6337, 0x191919, new Item.Properties()));

    public static final Supplier<Item> BORDER_COLLIE_SPAWN_EGG = ITEMS.register("border_collie_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.BORDER_COLLIE,0x202020,
                    0xf5f5f5, new Item.Properties()));

    public static final Supplier<Item> GOLDEN_RETRIEVER_SPAWN_EGG = ITEMS.register(
            "golden_retriever_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.GOLDEN_RETRIEVER,
                    0xf3c394, 0xc57f4d, new Item.Properties()));

    public static final Supplier<Item> LAB_RETRIEVER_SPAWN_EGG = ITEMS.register("lab_retriever_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.LAB_RETRIEVER,0x2a2a2a,
                    0x4d2b16, new Item.Properties()));

    public static final Supplier<Item> DACHSHUND_SPAWN_EGG = ITEMS.register("dachshund_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.DACHSHUND,0x202020, 0xa87347,
                    new Item.Properties()));

    public static final Supplier<Item> DALMATIAN_SPAWN_EGG = ITEMS.register("dalmatian_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.DALMATIAN,0xeeeeee, 0x111111,
                    new Item.Properties()));

    public static final Supplier<Item> CARDIGAN_CORGI_SPAWN_EGG = ITEMS.register("cardigan_corgi_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.CARDIGAN_CORGI,0xc46d32,
                    0xebebeb, new Item.Properties()));

    public static final Supplier<Item> PEMBROKE_CORGI_SPAWN_EGG = ITEMS.register("pembroke_corgi_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.PEMBROKE_CORGI,0xc68a41,
                    0xebebeb, new Item.Properties()));

    public static final Supplier<Item> RUSSELL_TERRIER_SPAWN_EGG = ITEMS.register(
            "russell_terrier_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.RUSSELL_TERRIER,
                    0xebebeb, 0x9b6c3c, new Item.Properties()));

    public static final Supplier<Item> ALASKAN_MALAMUTE_SPAWN_EGG = ITEMS.register(
            "alaskan_malamute_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.ALASKAN_MALAMUTE,
                    0x838786, 0x1f1f1f, new Item.Properties()));

    public static final Supplier<Item> BERNESE_MOUNTAIN_DOG_SPAWN_EGG = ITEMS.register(
            "bernese_mountain_dog_spawn_egg", () -> new DeferredSpawnEggItem(
                    ModEntityTypes.BERNESE_MOUNTAIN_DOG,0x1e1e1e, 0xad552f,
                    new Item.Properties()));

    public static final Supplier<Item> SAINT_BERNARD_SPAWN_EGG = ITEMS.register("saint_bernard_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.SAINT_BERNARD,0x915d44,
                    0xeaeaea, new Item.Properties()));

    public static final Supplier<Item> BLOODHOUND_SPAWN_EGG = ITEMS.register("bloodhound_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.BLOODHOUND,0xb38160, 0x181716,
                    new Item.Properties()));

    public static final Supplier<Item> BOXER_SPAWN_EGG = ITEMS.register("boxer_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.BOXER,0xb37a4b, 0xf4f4f4,
                    new Item.Properties()));

    public static final Supplier<Item> GREYHOUND_SPAWN_EGG = ITEMS.register("greyhound_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.GREYHOUND,0x282827, 0xdedede,
                    new Item.Properties()));

    public static final Supplier<Item> PIT_BULL_SPAWN_EGG = ITEMS.register("pit_bull_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.PIT_BULL,0x825a37, 0xeaeaea,
                    new Item.Properties()));

    public static final Supplier<Item> GREAT_DANE_SPAWN_EGG = ITEMS.register("great_dane_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.GREAT_DANE,0xb28154, 0x221e1e,
                    new Item.Properties()));

    public static final Supplier<Item> MASTIFF_SPAWN_EGG = ITEMS.register("mastiff_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.MASTIFF,0xe6ca9a, 0x212121,
                    new Item.Properties()));

    public static final Supplier<Item> SHIBA_INU_SPAWN_EGG = ITEMS.register("shiba_inu_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.SHIBA_INU,0xd49c61, 0xf6f7f2,
                    new Item.Properties()));

    public static final Supplier<Item> SHETLAND_SHEEPDOG_SPAWN_EGG = ITEMS.register(
            "shetland_sheepdog_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.SHETLAND_SHEEPDOG,
                    0xce8c55, 0xf2f0ef, new Item.Properties()));

    public static final Supplier<Item> BOSTON_TERRIER_SPAWN_EGG = ITEMS.register("boston_terrier_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.BOSTON_TERRIER,0x222423,
                    0xebf0ef, new Item.Properties()));

    public static final Supplier<Item> SCOTTISH_TERRIER_SPAWN_EGG = ITEMS.register(
            "scottish_terrier_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.SCOTTISH_TERRIER,
                    0x272423, 0xefddc5, new Item.Properties()));

    public static final Supplier<Item> CK_CHARLES_SPANIEL_SPAWN_EGG = ITEMS.register(
            "ck_charles_spaniel_spawn_egg", () -> new DeferredSpawnEggItem(
                    ModEntityTypes.CK_CHARLES_SPANIEL,0xeaeaea, 0x8f5136,
                    new Item.Properties()));

    public static final Supplier<Item> ITALIAN_GREYHOUND_SPAWN_EGG = ITEMS.register(
            "italian_greyhound_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.ITALIAN_GREYHOUND,
                    0x898f8e, 0x7e8685, new Item.Properties()));

    public static final Supplier<Item> AUSTRALIAN_SHEPHERD_SPAWN_EGG = ITEMS.register(
            "australian_shepherd_spawn_egg", () -> new DeferredSpawnEggItem(
                    ModEntityTypes.AUSTRALIAN_SHEPHERD,0x5c3a25, 0x5c3a25,
                    new Item.Properties()));

    public static final Supplier<Item> BASENJI_SPAWN_EGG = ITEMS.register("basenji_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.BASENJI,0xba7e43, 0xebebeb,
                    new Item.Properties()));

    public static final Supplier<Item> PUG_SPAWN_EGG = ITEMS.register("pug_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.PUG,0xe1caaa, 0x201e1c,
                    new Item.Properties()));
    public static final Supplier<Item> COCKER_SPANIEL_SPAWN_EGG = ITEMS.register("cocker_spaniel_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.COCKER_SPANIEL,0x985535,
                    0x462a1c, new Item.Properties()));

    public static final Supplier<Item> BULL_TERRIER_SPAWN_EGG = ITEMS.register("bull_terrier_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.BULL_TERRIER,0xebebeb,
                    0x1c1b1a, new Item.Properties()));

    public static final Supplier<Item> MINI_BULL_TERRIER_SPAWN_EGG = ITEMS.register(
            "mini_bull_terrier_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.MINI_BULL_TERRIER,
                    0xf1f1f1, 0x9b612b, new Item.Properties()));

    public static final Supplier<Item> SCHNAUZER_SPAWN_EGG = ITEMS.register("schnauzer_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.SCHNAUZER,0x616161, 0xc1c1c1,
                    new Item.Properties()));
    public static final Supplier<Item> MINI_SCHNAUZER_SPAWN_EGG = ITEMS.register("mini_schnauzer_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.MINI_SCHNAUZER,0x252121,
                    0xc1c1c1, new Item.Properties()));

    public static final Supplier<Item> POODLE_SPAWN_EGG = ITEMS.register("poodle_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.POODLE,0x222120, 0x38251a,
                    new Item.Properties()));

    public static final Supplier<Item> TOY_POODLE_SPAWN_EGG = ITEMS.register("toy_poodle_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.TOY_POODLE,0x1c1b1a, 0x7c492c,
                    new Item.Properties()));

    public static final Supplier<Item> DOBERMAN_SPAWN_EGG = ITEMS.register("doberman_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.DOBERMAN,0x202020, 0xa66f3c,
                    new Item.Properties()));

    public static final Supplier<Item> MINI_PINSCHER_SPAWN_EGG = ITEMS.register("mini_pinscher_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.MINI_PINSCHER,0x7a3c1e,
                    0x202020, new Item.Properties()));

    public static final Supplier<Item> HUSKY_SPAWN_EGG = ITEMS.register("husky_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.HUSKY,0x494949, 0x878c8a,
                    new Item.Properties()));

    public static final Supplier<Item> REDBONE_COONHOUND_SPAWN_EGG = ITEMS.register(
            "redbone_coonhound_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.REDBONE_COONHOUND,
                    0x583028, 0x864b2f, new Item.Properties()));

    public static final Supplier<Item> TREE_WALK_HOUND_SPAWN_EGG = ITEMS.register(
            "tree_walk_hound_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.TREE_WALK_HOUND,
                    0x885434, 0x222423, new Item.Properties()));

    public static final Supplier<Item> AIREDALE_TERRIER_SPAWN_EGG = ITEMS.register(
            "airedale_terrier_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.AIREDALE_TERRIER,
                    0xb7946e, 0x8e6c49, new Item.Properties()));

    public static final Supplier<Item> AMERICAN_FOXHOUND_SPAWN_EGG = ITEMS.register(
            "american_foxhound_spawn_egg", () -> new DeferredSpawnEggItem(ModEntityTypes.AMERICAN_FOXHOUND,
                    0xb68648, 0x2a2624, new Item.Properties()));

    public static final Supplier<Item> BULLDOG_SPAWN_EGG = ITEMS.register("bulldog_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.BULLDOG,0x855b33, 0xebebeb,
                    new Item.Properties()));

    public static final Supplier<Item> COLLIE_SPAWN_EGG = ITEMS.register("collie_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.COLLIE,0xb07f51, 0x362d25,
                    new Item.Properties()));

    public static final Supplier<Item> MUDI_SPAWN_EGG = ITEMS.register("mudi_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.MUDI,0xa6a8aa, 0x222426,
                    new Item.Properties()));

    public static final Supplier<Item> NORWEGIAN_ELKHOUND_SPAWN_EGG = ITEMS.register(
            "norwegian_elkhound_spawn_egg", () -> new DeferredSpawnEggItem(
                    ModEntityTypes.NORWEGIAN_ELKHOUND,0x959391, 0xcdc8c3,
                    new Item.Properties()));

    public static final Supplier<Item> BEAGLE_SPAWN_EGG = ITEMS.register("beagle_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.BEAGLE,0x956d3c, 0x241e17,
                    new Item.Properties()));

    public static final Supplier<Item> ROTTWEILER_SPAWN_EGG = ITEMS.register("rottweiler_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.ROTTWEILER,0x202020, 0xbb8057,
                    new Item.Properties()));

    public static final Supplier<Item> IRISH_SETTER_SPAWN_EGG = ITEMS.register("irish_setter_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.IRISH_SETTER,0xa36b41,
                    0x713c25, new Item.Properties()));

    public static final Supplier<Item> GERMAN_SPITZ_SPAWN_EGG = ITEMS.register("german_spitz_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.GERMAN_SPITZ,0xebebeb,
                    0xa96833, new Item.Properties()));

    public static final Supplier<Item> WHIPPET_SPAWN_EGG = ITEMS.register("whippet_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntityTypes.WHIPPET,0xca8b68, 0xdedede,
                    new Item.Properties()));

    @SubscribeEvent
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
