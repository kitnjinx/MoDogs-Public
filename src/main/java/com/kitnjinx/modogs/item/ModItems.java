package com.kitnjinx.modogs.item;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.ModEntityTypes;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // simplified explanation: list of items we're creating with the mod
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS,MoDogs.MOD_ID);

    /* DOG TREATS */
    public static final RegistryObject<Item> BACON_TREAT = ITEMS.register("bacon_treat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BEEF_TREAT = ITEMS.register("beef_treat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> CHICKEN_TREAT = ITEMS.register("chicken_treat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> MUTTON_TREAT = ITEMS.register("mutton_treat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> RABBIT_TREAT = ITEMS.register("rabbit_treat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> SALMON_TREAT = ITEMS.register("salmon_treat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> HEALING_TREAT = ITEMS.register("healing_treat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    /* GENE TESTING */
    public static final RegistryObject<Item> GENE_TESTER = ITEMS.register("gene_tester",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));
    public static final RegistryObject<Item> GENO_READER = ITEMS.register("geno_reader",
            () -> new GenoReaderItem(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB).stacksTo(1)));

    /* DOG COLLARS */
    public static final RegistryObject<Item> WHITE_COLLAR = ITEMS.register("white_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_GRAY_COLLAR = ITEMS.register("light_gray_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GRAY_COLLAR = ITEMS.register("gray_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLACK_COLLAR = ITEMS.register("black_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BROWN_COLLAR = ITEMS.register("brown_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> RED_COLLAR = ITEMS.register("red_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> ORANGE_COLLAR = ITEMS.register("orange_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> YELLOW_COLLAR = ITEMS.register("yellow_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIME_COLLAR = ITEMS.register("lime_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GREEN_COLLAR = ITEMS.register("green_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> CYAN_COLLAR = ITEMS.register("cyan_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_BLUE_COLLAR = ITEMS.register("light_blue_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLUE_COLLAR = ITEMS.register("blue_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PURPLE_COLLAR = ITEMS.register("purple_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> MAGENTA_COLLAR = ITEMS.register("magenta_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PINK_COLLAR = ITEMS.register("pink_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    /* REINFORCED DOG COLLARS */
    public static final RegistryObject<Item> WHITE_REINFORCED_COLLAR = ITEMS.register("white_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_GRAY_REINFORCED_COLLAR = ITEMS.register("light_gray_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GRAY_REINFORCED_COLLAR = ITEMS.register("gray_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLACK_REINFORCED_COLLAR = ITEMS.register("black_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BROWN_REINFORCED_COLLAR = ITEMS.register("brown_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> RED_REINFORCED_COLLAR = ITEMS.register("red_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> ORANGE_REINFORCED_COLLAR = ITEMS.register("orange_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> YELLOW_REINFORCED_COLLAR = ITEMS.register("yellow_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIME_REINFORCED_COLLAR = ITEMS.register("lime_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GREEN_REINFORCED_COLLAR = ITEMS.register("green_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> CYAN_REINFORCED_COLLAR = ITEMS.register("cyan_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_BLUE_REINFORCED_COLLAR = ITEMS.register("light_blue_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLUE_REINFORCED_COLLAR = ITEMS.register("blue_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PURPLE_REINFORCED_COLLAR = ITEMS.register("purple_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> MAGENTA_REINFORCED_COLLAR = ITEMS.register("magenta_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PINK_REINFORCED_COLLAR = ITEMS.register("pink_reinforced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    /* GOLD-PLATED DOG COLLARS */
    public static final RegistryObject<Item> WHITE_GOLD_PLATED_COLLAR = ITEMS.register("white_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_GRAY_GOLD_PLATED_COLLAR = ITEMS.register("light_gray_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GRAY_GOLD_PLATED_COLLAR = ITEMS.register("gray_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLACK_GOLD_PLATED_COLLAR = ITEMS.register("black_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BROWN_GOLD_PLATED_COLLAR = ITEMS.register("brown_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> RED_GOLD_PLATED_COLLAR = ITEMS.register("red_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> ORANGE_GOLD_PLATED_COLLAR = ITEMS.register("orange_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> YELLOW_GOLD_PLATED_COLLAR = ITEMS.register("yellow_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIME_GOLD_PLATED_COLLAR = ITEMS.register("lime_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GREEN_GOLD_PLATED_COLLAR = ITEMS.register("green_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> CYAN_GOLD_PLATED_COLLAR = ITEMS.register("cyan_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_BLUE_GOLD_PLATED_COLLAR = ITEMS.register("light_blue_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLUE_GOLD_PLATED_COLLAR = ITEMS.register("blue_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PURPLE_GOLD_PLATED_COLLAR = ITEMS.register("purple_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> MAGENTA_GOLD_PLATED_COLLAR = ITEMS.register("magenta_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PINK_GOLD_PLATED_COLLAR = ITEMS.register("pink_gold_plated_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    /* IRON-INFUSED DOG COLLARS */
    public static final RegistryObject<Item> WHITE_IRON_INFUSED_COLLAR = ITEMS.register("white_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_GRAY_IRON_INFUSED_COLLAR = ITEMS.register("light_gray_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GRAY_IRON_INFUSED_COLLAR = ITEMS.register("gray_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLACK_IRON_INFUSED_COLLAR = ITEMS.register("black_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BROWN_IRON_INFUSED_COLLAR = ITEMS.register("brown_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> RED_IRON_INFUSED_COLLAR = ITEMS.register("red_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> ORANGE_IRON_INFUSED_COLLAR = ITEMS.register("orange_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> YELLOW_IRON_INFUSED_COLLAR = ITEMS.register("yellow_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIME_IRON_INFUSED_COLLAR = ITEMS.register("lime_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GREEN_IRON_INFUSED_COLLAR = ITEMS.register("green_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> CYAN_IRON_INFUSED_COLLAR = ITEMS.register("cyan_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_BLUE_IRON_INFUSED_COLLAR = ITEMS.register("light_blue_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLUE_IRON_INFUSED_COLLAR = ITEMS.register("blue_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PURPLE_IRON_INFUSED_COLLAR = ITEMS.register("purple_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> MAGENTA_IRON_INFUSED_COLLAR = ITEMS.register("magenta_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PINK_IRON_INFUSED_COLLAR = ITEMS.register("pink_iron_infused_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    /* DIAMOND-CRUSTED DOG COLLARS */
    public static final RegistryObject<Item> WHITE_DIAMOND_CRUSTED_COLLAR = ITEMS.register("white_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_GRAY_DIAMOND_CRUSTED_COLLAR = ITEMS.register("light_gray_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GRAY_DIAMOND_CRUSTED_COLLAR = ITEMS.register("gray_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLACK_DIAMOND_CRUSTED_COLLAR = ITEMS.register("black_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BROWN_DIAMOND_CRUSTED_COLLAR = ITEMS.register("brown_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> RED_DIAMOND_CRUSTED_COLLAR = ITEMS.register("red_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> ORANGE_DIAMOND_CRUSTED_COLLAR = ITEMS.register("orange_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> YELLOW_DIAMOND_CRUSTED_COLLAR = ITEMS.register("yellow_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIME_DIAMOND_CRUSTED_COLLAR = ITEMS.register("lime_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GREEN_DIAMOND_CRUSTED_COLLAR = ITEMS.register("green_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> CYAN_DIAMOND_CRUSTED_COLLAR = ITEMS.register("cyan_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_BLUE_DIAMOND_CRUSTED_COLLAR = ITEMS.register("light_blue_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLUE_DIAMOND_CRUSTED_COLLAR = ITEMS.register("blue_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PURPLE_DIAMOND_CRUSTED_COLLAR = ITEMS.register("purple_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> MAGENTA_DIAMOND_CRUSTED_COLLAR = ITEMS.register("magenta_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PINK_DIAMOND_CRUSTED_COLLAR = ITEMS.register("pink_diamond_crusted_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    /* NETHERITE-LACED DOG COLLARS */
    public static final RegistryObject<Item> WHITE_NETHERITE_LACED_COLLAR = ITEMS.register("white_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_GRAY_NETHERITE_LACED_COLLAR = ITEMS.register("light_gray_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GRAY_NETHERITE_LACED_COLLAR = ITEMS.register("gray_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLACK_NETHERITE_LACED_COLLAR = ITEMS.register("black_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BROWN_NETHERITE_LACED_COLLAR = ITEMS.register("brown_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> RED_NETHERITE_LACED_COLLAR = ITEMS.register("red_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> ORANGE_NETHERITE_LACED_COLLAR = ITEMS.register("orange_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> YELLOW_NETHERITE_LACED_COLLAR = ITEMS.register("yellow_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIME_NETHERITE_LACED_COLLAR = ITEMS.register("lime_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> GREEN_NETHERITE_LACED_COLLAR = ITEMS.register("green_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> CYAN_NETHERITE_LACED_COLLAR = ITEMS.register("cyan_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> LIGHT_BLUE_NETHERITE_LACED_COLLAR = ITEMS.register("light_blue_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> BLUE_NETHERITE_LACED_COLLAR = ITEMS.register("blue_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PURPLE_NETHERITE_LACED_COLLAR = ITEMS.register("purple_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> MAGENTA_NETHERITE_LACED_COLLAR = ITEMS.register("magenta_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    public static final RegistryObject<Item> PINK_NETHERITE_LACED_COLLAR = ITEMS.register("pink_netherite_laced_collar",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MODOGS_ITEMS_TAB)));

    /* DOG SPAWN EGGS */
    public static final RegistryObject<Item> GERMAN_SHEPHERD_SPAWN_EGG = ITEMS.register("german_shepherd_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.GERMAN_SHEPHERD,0x9C6337, 0x191919,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> BORDER_COLLIE_SPAWN_EGG = ITEMS.register("border_collie_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.BORDER_COLLIE,0x202020, 0xf5f5f5,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> GOLDEN_RETRIEVER_SPAWN_EGG = ITEMS.register("golden_retriever_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.GOLDEN_RETRIEVER,0xf3c394, 0xc57f4d,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> LAB_RETRIEVER_SPAWN_EGG = ITEMS.register("lab_retriever_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.LAB_RETRIEVER,0x161616, 0x1a1a1a,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> DACHSHUND_SPAWN_EGG = ITEMS.register("dachshund_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.DACHSHUND,0x202020, 0xa87347,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> DALMATIAN_SPAWN_EGG = ITEMS.register("dalmatian_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.DALMATIAN,0xeeeeee, 0x111111,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> CARDIGAN_CORGI_SPAWN_EGG = ITEMS.register("cardigan_corgi_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.CARDIGAN_CORGI,0xc46d32, 0xebebeb,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> PEMBROKE_CORGI_SPAWN_EGG = ITEMS.register("pembroke_corgi_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.PEMBROKE_CORGI,0xc68a41, 0xebebeb,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> RUSSELL_TERRIER_SPAWN_EGG = ITEMS.register("russell_terrier_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.RUSSELL_TERRIER,0xebebeb, 0x9b6c3c,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> ALASKAN_MALAMUTE_SPAWN_EGG = ITEMS.register("alaskan_malamute_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.ALASKAN_MALAMUTE,0x838786, 0x1f1f1f,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> BERNESE_MOUNTAIN_DOG_SPAWN_EGG = ITEMS.register("bernese_mountain_dog_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.BERNESE_MOUNTAIN_DOG,0x1e1e1e, 0xad552f,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> SAINT_BERNARD_SPAWN_EGG = ITEMS.register("saint_bernard_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SAINT_BERNARD,0x915d44, 0xeaeaea,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> BLOODHOUND_SPAWN_EGG = ITEMS.register("bloodhound_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.BLOODHOUND,0xb38160, 0x181716,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> BOXER_SPAWN_EGG = ITEMS.register("boxer_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.BOXER,0xb37a4b, 0xf4f4f4,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> GREYHOUND_SPAWN_EGG = ITEMS.register("greyhound_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.GREYHOUND,0x282827, 0xdedede,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> PIT_BULL_SPAWN_EGG = ITEMS.register("pit_bull_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.PIT_BULL,0x825a37, 0xeaeaea,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> GREAT_DANE_SPAWN_EGG = ITEMS.register("great_dane_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.GREAT_DANE,0xb28154, 0x221e1e,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> MASTIFF_SPAWN_EGG = ITEMS.register("mastiff_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.MASTIFF,0xe6ca9a, 0x212121,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> SHIBA_INU_SPAWN_EGG = ITEMS.register("shiba_inu_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SHIBA_INU,0xd49c61, 0xf6f7f2,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> SHETLAND_SHEEPDOG_SPAWN_EGG = ITEMS.register("shetland_sheepdog_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SHETLAND_SHEEPDOG,0xce8c55, 0xf2f0ef,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> BOSTON_TERRIER_SPAWN_EGG = ITEMS.register("boston_terrier_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.BOSTON_TERRIER,0x222423, 0xebf0ef,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    public static final RegistryObject<Item> SCOTTISH_TERRIER_SPAWN_EGG = ITEMS.register("scottish_terrier_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.SCOTTISH_TERRIER,0x272423, 0xefddc5,
                    new Item.Properties().tab(ModCreativeModeTab.MODOGS_SPAWNER_TAB)));

    // Register items
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
