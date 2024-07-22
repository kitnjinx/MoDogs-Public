package com.kitnjinx.modogs.item;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoDogs.MOD_ID);

    public static final Supplier<CreativeModeTab> MODOGS_ITEMS_TAB = CREATIVE_MODE_TABS.register(
            "modogs_items_tab",  () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativemodetab.modogs_items_tab"))
                    .icon(() -> new ItemStack(ModItems.RABBIT_TREAT.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.BACON_TREAT.get());
                        output.accept(ModItems.BEEF_TREAT.get());
                        output.accept(ModItems.CHICKEN_TREAT.get());
                        output.accept(ModItems.MUTTON_TREAT.get());
                        output.accept(ModItems.RABBIT_TREAT.get());
                        output.accept(ModItems.SALMON_TREAT.get());
                        output.accept(ModItems.HEALING_TREAT.get());

                        output.accept(ModItems.GENE_TESTER.get());
                        output.accept(ModItems.GENO_READER.get());
                        output.accept(ModItems.PAPER_STRIP.get());
                        output.accept(ModItems.USED_PAPER_STRIP.get());

                        output.accept(ModItems.WHITE_COLLAR.get());
                        output.accept(ModItems.LIGHT_GRAY_COLLAR.get());
                        output.accept(ModItems.GRAY_COLLAR.get());
                        output.accept(ModItems.BLACK_COLLAR.get());
                        output.accept(ModItems.BROWN_COLLAR.get());
                        output.accept(ModItems.RED_COLLAR.get());
                        output.accept(ModItems.ORANGE_COLLAR.get());
                        output.accept(ModItems.YELLOW_COLLAR.get());
                        output.accept(ModItems.LIME_COLLAR.get());
                        output.accept(ModItems.GREEN_COLLAR.get());
                        output.accept(ModItems.CYAN_COLLAR.get());
                        output.accept(ModItems.LIGHT_BLUE_COLLAR.get());
                        output.accept(ModItems.BLUE_COLLAR.get());
                        output.accept(ModItems.PURPLE_COLLAR.get());
                        output.accept(ModItems.MAGENTA_COLLAR.get());
                        output.accept(ModItems.PINK_COLLAR.get());

                        output.accept(ModItems.WHITE_REINFORCED_COLLAR.get());
                        output.accept(ModItems.LIGHT_GRAY_REINFORCED_COLLAR.get());
                        output.accept(ModItems.GRAY_REINFORCED_COLLAR.get());
                        output.accept(ModItems.BLACK_REINFORCED_COLLAR.get());
                        output.accept(ModItems.BROWN_REINFORCED_COLLAR.get());
                        output.accept(ModItems.RED_REINFORCED_COLLAR.get());
                        output.accept(ModItems.ORANGE_REINFORCED_COLLAR.get());
                        output.accept(ModItems.YELLOW_REINFORCED_COLLAR.get());
                        output.accept(ModItems.LIME_REINFORCED_COLLAR.get());
                        output.accept(ModItems.GREEN_REINFORCED_COLLAR.get());
                        output.accept(ModItems.CYAN_REINFORCED_COLLAR.get());
                        output.accept(ModItems.LIGHT_BLUE_REINFORCED_COLLAR.get());
                        output.accept(ModItems.BLUE_REINFORCED_COLLAR.get());
                        output.accept(ModItems.PURPLE_REINFORCED_COLLAR.get());
                        output.accept(ModItems.MAGENTA_REINFORCED_COLLAR.get());
                        output.accept(ModItems.PINK_REINFORCED_COLLAR.get());

                        output.accept(ModItems.WHITE_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.LIGHT_GRAY_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.GRAY_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.BLACK_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.BROWN_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.RED_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.ORANGE_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.YELLOW_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.LIME_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.GREEN_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.CYAN_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.LIGHT_BLUE_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.BLUE_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.PURPLE_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.MAGENTA_GOLD_PLATED_COLLAR.get());
                        output.accept(ModItems.PINK_GOLD_PLATED_COLLAR.get());

                        output.accept(ModItems.WHITE_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.LIGHT_GRAY_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.GRAY_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.BLACK_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.BROWN_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.RED_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.ORANGE_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.YELLOW_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.LIME_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.GREEN_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.CYAN_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.LIGHT_BLUE_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.BLUE_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.PURPLE_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.MAGENTA_IRON_INFUSED_COLLAR.get());
                        output.accept(ModItems.PINK_IRON_INFUSED_COLLAR.get());

                        output.accept(ModItems.WHITE_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.LIGHT_GRAY_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.GRAY_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.BLACK_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.BROWN_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.RED_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.ORANGE_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.YELLOW_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.LIME_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.GREEN_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.CYAN_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.LIGHT_BLUE_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.BLUE_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.PURPLE_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.MAGENTA_DIAMOND_CRUSTED_COLLAR.get());
                        output.accept(ModItems.PINK_DIAMOND_CRUSTED_COLLAR.get());

                        output.accept(ModItems.WHITE_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.LIGHT_GRAY_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.GRAY_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.BLACK_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.BROWN_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.RED_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.ORANGE_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.YELLOW_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.LIME_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.GREEN_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.CYAN_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.LIGHT_BLUE_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.BLUE_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.PURPLE_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.MAGENTA_NETHERITE_LACED_COLLAR.get());
                        output.accept(ModItems.PINK_NETHERITE_LACED_COLLAR.get());

                        output.accept(ModBlocks.CARE_STATION.get());
                        output.accept(ModBlocks.GENO_PRINTER.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> MODOGS_SPAWNER_TAB = CREATIVE_MODE_TABS.register(
            "modogs_spawner_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativemodetab.modogs_spawner_tab"))
                    .icon(() -> new ItemStack(ModItems.GERMAN_SHEPHERD_SPAWN_EGG.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.GERMAN_SHEPHERD_SPAWN_EGG.get());
                        output.accept(ModItems.BORDER_COLLIE_SPAWN_EGG.get());
                        output.accept(ModItems.GOLDEN_RETRIEVER_SPAWN_EGG.get());
                        output.accept(ModItems.LAB_RETRIEVER_SPAWN_EGG.get());
                        output.accept(ModItems.DACHSHUND_SPAWN_EGG.get());
                        output.accept(ModItems.DALMATIAN_SPAWN_EGG.get());
                        output.accept(ModItems.CARDIGAN_CORGI_SPAWN_EGG.get());
                        output.accept(ModItems.PEMBROKE_CORGI_SPAWN_EGG.get());
                        output.accept(ModItems.RUSSELL_TERRIER_SPAWN_EGG.get());
                        output.accept(ModItems.ALASKAN_MALAMUTE_SPAWN_EGG.get());
                        output.accept(ModItems.BERNESE_MOUNTAIN_DOG_SPAWN_EGG.get());
                        output.accept(ModItems.SAINT_BERNARD_SPAWN_EGG.get());
                        output.accept(ModItems.BLOODHOUND_SPAWN_EGG.get());
                        output.accept(ModItems.BOXER_SPAWN_EGG.get());
                        output.accept(ModItems.GREYHOUND_SPAWN_EGG.get());
                        output.accept(ModItems.PIT_BULL_SPAWN_EGG.get());
                        output.accept(ModItems.GREAT_DANE_SPAWN_EGG.get());
                        output.accept(ModItems.MASTIFF_SPAWN_EGG.get());
                        output.accept(ModItems.SHIBA_INU_SPAWN_EGG.get());
                        output.accept(ModItems.SHETLAND_SHEEPDOG_SPAWN_EGG.get());
                        output.accept(ModItems.BOSTON_TERRIER_SPAWN_EGG.get());
                        output.accept(ModItems.SCOTTISH_TERRIER_SPAWN_EGG.get());
                        output.accept(ModItems.CK_CHARLES_SPANIEL_SPAWN_EGG.get());
                        output.accept(ModItems.ITALIAN_GREYHOUND_SPAWN_EGG.get());
                        output.accept(ModItems.AUSTRALIAN_SHEPHERD_SPAWN_EGG.get());
                        output.accept(ModItems.BASENJI_SPAWN_EGG.get());
                        output.accept(ModItems.PUG_SPAWN_EGG.get());
                        output.accept(ModItems.COCKER_SPANIEL_SPAWN_EGG.get());
                        output.accept(ModItems.BULL_TERRIER_SPAWN_EGG.get());
                        output.accept(ModItems.MINI_BULL_TERRIER_SPAWN_EGG.get());
                        output.accept(ModItems.SCHNAUZER_SPAWN_EGG.get());
                        output.accept(ModItems.MINI_SCHNAUZER_SPAWN_EGG.get());
                        output.accept(ModItems.POODLE_SPAWN_EGG.get());
                        output.accept(ModItems.TOY_POODLE_SPAWN_EGG.get());
                        output.accept(ModItems.DOBERMAN_SPAWN_EGG.get());
                        output.accept(ModItems.MINI_PINSCHER_SPAWN_EGG.get());
                        output.accept(ModItems.HUSKY_SPAWN_EGG.get());
                        output.accept(ModItems.REDBONE_COONHOUND_SPAWN_EGG.get());
                        output.accept(ModItems.TREE_WALK_HOUND_SPAWN_EGG.get());
                        output.accept(ModItems.AIREDALE_TERRIER_SPAWN_EGG.get());
                        output.accept(ModItems.AMERICAN_FOXHOUND_SPAWN_EGG.get());
                        output.accept(ModItems.BULLDOG_SPAWN_EGG.get());
                        output.accept(ModItems.COLLIE_SPAWN_EGG.get());
                        output.accept(ModItems.MUDI_SPAWN_EGG.get());
                        output.accept(ModItems.NORWEGIAN_ELKHOUND_SPAWN_EGG.get());
                        output.accept(ModItems.BEAGLE_SPAWN_EGG.get());
                        output.accept(ModItems.ROTTWEILER_SPAWN_EGG.get());
                        output.accept(ModItems.IRISH_SETTER_SPAWN_EGG.get());
                        output.accept(ModItems.GERMAN_SPITZ_SPAWN_EGG.get());
                        output.accept(ModItems.WHIPPET_SPAWN_EGG.get());
                    })
                    .build());

    @SubscribeEvent
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
