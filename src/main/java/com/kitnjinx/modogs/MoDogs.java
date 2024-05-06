package com.kitnjinx.modogs;

import com.kitnjinx.modogs.block.ModBlocks;
import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.client.renderer.*;
import com.kitnjinx.modogs.item.ModCreativeModeTab;
import com.kitnjinx.modogs.item.ModItems;
import com.kitnjinx.modogs.loot.ModLootModifiers;
import com.kitnjinx.modogs.screens.GenoPrinterScreen;
import com.kitnjinx.modogs.screens.ModMenuTypes;
import com.kitnjinx.modogs.villager.ModVillagers;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(MoDogs.MOD_ID)
public class MoDogs {
    public static final String MOD_ID = "modogs";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MoDogs() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModVillagers.register(modEventBus);

        ModEntityTypes.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        GeckoLib.initialize();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        /*event.enqueueWork(() -> {

        });*/
    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == ModCreativeModeTab.MODOGS_ITEMS_TAB) {
            event.accept(ModItems.BACON_TREAT);
            event.accept(ModItems.BEEF_TREAT);
            event.accept(ModItems.CHICKEN_TREAT);
            event.accept(ModItems.MUTTON_TREAT);
            event.accept(ModItems.RABBIT_TREAT);
            event.accept(ModItems.SALMON_TREAT);
            event.accept(ModItems.HEALING_TREAT);

            event.accept(ModItems.GENE_TESTER);
            event.accept(ModItems.GENO_READER);
            event.accept(ModItems.PAPER_STRIP);

            event.accept(ModItems.WHITE_COLLAR);
            event.accept(ModItems.LIGHT_GRAY_COLLAR);
            event.accept(ModItems.GRAY_COLLAR);
            event.accept(ModItems.BLACK_COLLAR);
            event.accept(ModItems.BROWN_COLLAR);
            event.accept(ModItems.RED_COLLAR);
            event.accept(ModItems.ORANGE_COLLAR);
            event.accept(ModItems.YELLOW_COLLAR);
            event.accept(ModItems.LIME_COLLAR);
            event.accept(ModItems.GREEN_COLLAR);
            event.accept(ModItems.CYAN_COLLAR);
            event.accept(ModItems.LIGHT_BLUE_COLLAR);
            event.accept(ModItems.BLUE_COLLAR);
            event.accept(ModItems.PURPLE_COLLAR);
            event.accept(ModItems.MAGENTA_COLLAR);
            event.accept(ModItems.PINK_COLLAR);

            event.accept(ModItems.WHITE_REINFORCED_COLLAR);
            event.accept(ModItems.LIGHT_GRAY_REINFORCED_COLLAR);
            event.accept(ModItems.GRAY_REINFORCED_COLLAR);
            event.accept(ModItems.BLACK_REINFORCED_COLLAR);
            event.accept(ModItems.BROWN_REINFORCED_COLLAR);
            event.accept(ModItems.RED_REINFORCED_COLLAR);
            event.accept(ModItems.ORANGE_REINFORCED_COLLAR);
            event.accept(ModItems.YELLOW_REINFORCED_COLLAR);
            event.accept(ModItems.LIME_REINFORCED_COLLAR);
            event.accept(ModItems.GREEN_REINFORCED_COLLAR);
            event.accept(ModItems.CYAN_REINFORCED_COLLAR);
            event.accept(ModItems.LIGHT_BLUE_REINFORCED_COLLAR);
            event.accept(ModItems.BLUE_REINFORCED_COLLAR);
            event.accept(ModItems.PURPLE_REINFORCED_COLLAR);
            event.accept(ModItems.MAGENTA_REINFORCED_COLLAR);
            event.accept(ModItems.PINK_REINFORCED_COLLAR);

            event.accept(ModItems.WHITE_GOLD_PLATED_COLLAR);
            event.accept(ModItems.LIGHT_GRAY_GOLD_PLATED_COLLAR);
            event.accept(ModItems.GRAY_GOLD_PLATED_COLLAR);
            event.accept(ModItems.BLACK_GOLD_PLATED_COLLAR);
            event.accept(ModItems.BROWN_GOLD_PLATED_COLLAR);
            event.accept(ModItems.RED_GOLD_PLATED_COLLAR);
            event.accept(ModItems.ORANGE_GOLD_PLATED_COLLAR);
            event.accept(ModItems.YELLOW_GOLD_PLATED_COLLAR);
            event.accept(ModItems.LIME_GOLD_PLATED_COLLAR);
            event.accept(ModItems.GREEN_GOLD_PLATED_COLLAR);
            event.accept(ModItems.CYAN_GOLD_PLATED_COLLAR);
            event.accept(ModItems.LIGHT_BLUE_GOLD_PLATED_COLLAR);
            event.accept(ModItems.BLUE_GOLD_PLATED_COLLAR);
            event.accept(ModItems.PURPLE_GOLD_PLATED_COLLAR);
            event.accept(ModItems.MAGENTA_GOLD_PLATED_COLLAR);
            event.accept(ModItems.PINK_GOLD_PLATED_COLLAR);

            event.accept(ModItems.WHITE_IRON_INFUSED_COLLAR);
            event.accept(ModItems.LIGHT_GRAY_IRON_INFUSED_COLLAR);
            event.accept(ModItems.GRAY_IRON_INFUSED_COLLAR);
            event.accept(ModItems.BLACK_IRON_INFUSED_COLLAR);
            event.accept(ModItems.BROWN_IRON_INFUSED_COLLAR);
            event.accept(ModItems.RED_IRON_INFUSED_COLLAR);
            event.accept(ModItems.ORANGE_IRON_INFUSED_COLLAR);
            event.accept(ModItems.YELLOW_IRON_INFUSED_COLLAR);
            event.accept(ModItems.LIME_IRON_INFUSED_COLLAR);
            event.accept(ModItems.GREEN_IRON_INFUSED_COLLAR);
            event.accept(ModItems.CYAN_IRON_INFUSED_COLLAR);
            event.accept(ModItems.LIGHT_BLUE_IRON_INFUSED_COLLAR);
            event.accept(ModItems.BLUE_IRON_INFUSED_COLLAR);
            event.accept(ModItems.PURPLE_IRON_INFUSED_COLLAR);
            event.accept(ModItems.MAGENTA_IRON_INFUSED_COLLAR);
            event.accept(ModItems.PINK_IRON_INFUSED_COLLAR);

            event.accept(ModItems.WHITE_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.LIGHT_GRAY_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.GRAY_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.BLACK_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.BROWN_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.RED_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.ORANGE_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.YELLOW_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.LIME_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.GREEN_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.CYAN_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.LIGHT_BLUE_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.BLUE_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.PURPLE_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.MAGENTA_DIAMOND_CRUSTED_COLLAR);
            event.accept(ModItems.PINK_DIAMOND_CRUSTED_COLLAR);

            event.accept(ModItems.WHITE_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.LIGHT_GRAY_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.GRAY_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.BLACK_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.BROWN_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.RED_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.ORANGE_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.YELLOW_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.LIME_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.GREEN_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.CYAN_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.LIGHT_BLUE_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.BLUE_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.PURPLE_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.MAGENTA_NETHERITE_LACED_COLLAR);
            event.accept(ModItems.PINK_NETHERITE_LACED_COLLAR);

            event.accept(ModBlocks.CARE_STATION);
            event.accept(ModBlocks.GENO_PRINTER);
        }

        if (event.getTab() == ModCreativeModeTab.MODOGS_SPAWNER_TAB) {
            event.accept(ModItems.GERMAN_SHEPHERD_SPAWN_EGG);
            event.accept(ModItems.BORDER_COLLIE_SPAWN_EGG);
            event.accept(ModItems.GOLDEN_RETRIEVER_SPAWN_EGG);
            event.accept(ModItems.LAB_RETRIEVER_SPAWN_EGG);
            event.accept(ModItems.DACHSHUND_SPAWN_EGG);
            event.accept(ModItems.DALMATIAN_SPAWN_EGG);
            event.accept(ModItems.CARDIGAN_CORGI_SPAWN_EGG);
            event.accept(ModItems.PEMBROKE_CORGI_SPAWN_EGG);
            event.accept(ModItems.RUSSELL_TERRIER_SPAWN_EGG);
            event.accept(ModItems.ALASKAN_MALAMUTE_SPAWN_EGG);
            event.accept(ModItems.BERNESE_MOUNTAIN_DOG_SPAWN_EGG);
            event.accept(ModItems.SAINT_BERNARD_SPAWN_EGG);
            event.accept(ModItems.BLOODHOUND_SPAWN_EGG);
            event.accept(ModItems.BOXER_SPAWN_EGG);
            event.accept(ModItems.GREYHOUND_SPAWN_EGG);
            event.accept(ModItems.PIT_BULL_SPAWN_EGG);
            event.accept(ModItems.GREAT_DANE_SPAWN_EGG);
            event.accept(ModItems.MASTIFF_SPAWN_EGG);
            event.accept(ModItems.SHIBA_INU_SPAWN_EGG);
            event.accept(ModItems.SHETLAND_SHEEPDOG_SPAWN_EGG);
            event.accept(ModItems.BOSTON_TERRIER_SPAWN_EGG);
            event.accept(ModItems.SCOTTISH_TERRIER_SPAWN_EGG);
            event.accept(ModItems.CK_CHARLES_SPANIEL_SPAWN_EGG);
            event.accept(ModItems.ITALIAN_GREYHOUND_SPAWN_EGG);
            event.accept(ModItems.AUSTRALIAN_SHEPHERD_SPAWN_EGG);
            event.accept(ModItems.BASENJI_SPAWN_EGG);
            event.accept(ModItems.PUG_SPAWN_EGG);
            event.accept(ModItems.COCKER_SPANIEL_SPAWN_EGG);
            event.accept(ModItems.BULL_TERRIER_SPAWN_EGG);
            event.accept(ModItems.MINI_BULL_TERRIER_SPAWN_EGG);
            event.accept(ModItems.SCHNAUZER_SPAWN_EGG);
            event.accept(ModItems.MINI_SCHNAUZER_SPAWN_EGG);
            event.accept(ModItems.POODLE_SPAWN_EGG);
            event.accept(ModItems.TOY_POODLE_SPAWN_EGG);
            event.accept(ModItems.DOBERMAN_SPAWN_EGG);
            event.accept(ModItems.MINI_PINSCHER_SPAWN_EGG);
            event.accept(ModItems.HUSKY_SPAWN_EGG);
            event.accept(ModItems.REDBONE_COONHOUND_SPAWN_EGG);
            event.accept(ModItems.TREE_WALK_HOUND_SPAWN_EGG);
            event.accept(ModItems.AIREDALE_TERRIER_SPAWN_EGG);
            event.accept(ModItems.AMERICAN_FOXHOUND_SPAWN_EGG);
            event.accept(ModItems.BULLDOG_SPAWN_EGG);
            event.accept(ModItems.COLLIE_SPAWN_EGG);
            event.accept(ModItems.MUDI_SPAWN_EGG);
            event.accept(ModItems.NORWEGIAN_ELKHOUND_SPAWN_EGG);
            event.accept(ModItems.BEAGLE_SPAWN_EGG);
            event.accept(ModItems.ROTTWEILER_SPAWN_EGG);
            event.accept(ModItems.IRISH_SETTER_SPAWN_EGG);
            event.accept(ModItems.GERMAN_SPITZ_SPAWN_EGG);
            event.accept(ModItems.WHIPPET_SPAWN_EGG);
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntityTypes.GERMAN_SHEPHERD.get(), GermanShepherdRenderer::new);
            EntityRenderers.register(ModEntityTypes.BORDER_COLLIE.get(), BorderCollieRenderer::new);
            EntityRenderers.register(ModEntityTypes.GOLDEN_RETRIEVER.get(), GoldenRetrieverRenderer::new);
            EntityRenderers.register(ModEntityTypes.LAB_RETRIEVER.get(), LabRetrieverRenderer::new);
            EntityRenderers.register(ModEntityTypes.DACHSHUND.get(), DachshundRenderer::new);
            EntityRenderers.register(ModEntityTypes.DALMATIAN.get(), DalmatianRenderer::new);
            EntityRenderers.register(ModEntityTypes.CARDIGAN_CORGI.get(), CardiganCorgiRenderer::new);
            EntityRenderers.register(ModEntityTypes.PEMBROKE_CORGI.get(), PembrokeCorgiRenderer::new);
            EntityRenderers.register(ModEntityTypes.RUSSELL_TERRIER.get(), RussellTerrierRenderer::new);
            EntityRenderers.register(ModEntityTypes.ALASKAN_MALAMUTE.get(), AlaskanMalamuteRenderer::new);
            EntityRenderers.register(ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(), BerneseMountainDogRenderer::new);
            EntityRenderers.register(ModEntityTypes.SAINT_BERNARD.get(), SaintBernardRenderer::new);
            EntityRenderers.register(ModEntityTypes.BLOODHOUND.get(), BloodhoundRenderer::new);
            EntityRenderers.register(ModEntityTypes.BOXER.get(), BoxerRenderer::new);
            EntityRenderers.register(ModEntityTypes.GREYHOUND.get(), GreyhoundRenderer::new);
            EntityRenderers.register(ModEntityTypes.PIT_BULL.get(), PitBullRenderer::new);
            EntityRenderers.register(ModEntityTypes.GREAT_DANE.get(), GreatDaneRenderer::new);
            EntityRenderers.register(ModEntityTypes.MASTIFF.get(), MastiffRenderer::new);
            EntityRenderers.register(ModEntityTypes.SHIBA_INU.get(), ShibaInuRenderer::new);
            EntityRenderers.register(ModEntityTypes.SHETLAND_SHEEPDOG.get(), ShetlandSheepdogRenderer::new);
            EntityRenderers.register(ModEntityTypes.BOSTON_TERRIER.get(), BostonTerrierRenderer::new);
            EntityRenderers.register(ModEntityTypes.SCOTTISH_TERRIER.get(), ScottishTerrierRenderer::new);
            EntityRenderers.register(ModEntityTypes.CK_CHARLES_SPANIEL.get(), CKCharlesSpanielRenderer::new);
            EntityRenderers.register(ModEntityTypes.ITALIAN_GREYHOUND.get(), ItalianGreyhoundRenderer::new);
            EntityRenderers.register(ModEntityTypes.AUSTRALIAN_SHEPHERD.get(), AustralianShepherdRenderer::new);
            EntityRenderers.register(ModEntityTypes.BASENJI.get(), BasenjiRenderer::new);
            EntityRenderers.register(ModEntityTypes.PUG.get(), PugRenderer::new);
            EntityRenderers.register(ModEntityTypes.COCKER_SPANIEL.get(), CockerSpanielRenderer::new);
            EntityRenderers.register(ModEntityTypes.BULL_TERRIER.get(), BullTerrierRenderer::new);
            EntityRenderers.register(ModEntityTypes.MINI_BULL_TERRIER.get(), MiniBullTerrierRenderer::new);
            EntityRenderers.register(ModEntityTypes.SCHNAUZER.get(), SchnauzerRenderer::new);
            EntityRenderers.register(ModEntityTypes.MINI_SCHNAUZER.get(), MiniSchnauzerRenderer::new);
            EntityRenderers.register(ModEntityTypes.POODLE.get(), PoodleRenderer::new);
            EntityRenderers.register(ModEntityTypes.TOY_POODLE.get(), ToyPoodleRenderer::new);
            EntityRenderers.register(ModEntityTypes.DOBERMAN.get(), DobermanRenderer::new);
            EntityRenderers.register(ModEntityTypes.MINI_PINSCHER.get(), MiniPinscherRenderer::new);
            EntityRenderers.register(ModEntityTypes.HUSKY.get(), HuskyRenderer::new);
            EntityRenderers.register(ModEntityTypes.REDBONE_COONHOUND.get(), RedboneCoonhoundRenderer::new);
            EntityRenderers.register(ModEntityTypes.TREE_WALK_HOUND.get(), TreeWalkHoundRenderer::new);
            EntityRenderers.register(ModEntityTypes.AIREDALE_TERRIER.get(), AiredaleTerrierRenderer::new);
            EntityRenderers.register(ModEntityTypes.AMERICAN_FOXHOUND.get(), AmericanFoxhoundRenderer::new);
            EntityRenderers.register(ModEntityTypes.BULLDOG.get(), BulldogRenderer::new);
            EntityRenderers.register(ModEntityTypes.COLLIE.get(), CollieRenderer::new);
            EntityRenderers.register(ModEntityTypes.MUDI.get(), MudiRenderer::new);
            EntityRenderers.register(ModEntityTypes.NORWEGIAN_ELKHOUND.get(), NorwegianElkhoundRenderer::new);
            EntityRenderers.register(ModEntityTypes.BEAGLE.get(), BeagleRenderer::new);
            EntityRenderers.register(ModEntityTypes.ROTTWEILER.get(), RottweilerRenderer::new);
            EntityRenderers.register(ModEntityTypes.IRISH_SETTER.get(), IrishSetterRenderer::new);
            EntityRenderers.register(ModEntityTypes.GERMAN_SPITZ.get(), GermanSpitzRenderer::new);
            EntityRenderers.register(ModEntityTypes.WHIPPET.get(), WhippetRenderer::new);

            MenuScreens.register(ModMenuTypes.GENO_PRINTER_MENU.get(), GenoPrinterScreen::new);
        }
    }
}
