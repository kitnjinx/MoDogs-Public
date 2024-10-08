package com.kitnjinx.modogs;

import com.kitnjinx.modogs.block.ModBlocks;
import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.client.renderer.*;
import com.kitnjinx.modogs.item.ModCreativeModeTab;
import com.kitnjinx.modogs.item.ModItems;
import com.kitnjinx.modogs.screens.ModMenuTypes;
import com.kitnjinx.modogs.villager.ModVillagers;
import com.kitnjinx.modogs.world.village.VillageAddition;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLibClient;

@Mod(MoDogs.MOD_ID)
public class MoDogs {
    public static final String MOD_ID = "modogs";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MoDogs(IEventBus eventBus, ModContainer container) {

        eventBus.addListener(this::commonSetup);

        ModCreativeModeTab.register(eventBus);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModMenuTypes.register(eventBus);

        ModVillagers.register(eventBus);

        ModEntityTypes.register(eventBus);

        GeckoLibClient.init();

        eventBus.addListener(this::commonSetup);

        //eventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        /*event.enqueueWork(() -> {

        });*/
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModCreativeModeTab.MODOGS_ITEMS_TAB.get()) {
            event.accept(ModItems.BACON_TREAT.get());
            event.accept(ModItems.BEEF_TREAT.get());
            event.accept(ModItems.CHICKEN_TREAT.get());
            event.accept(ModItems.MUTTON_TREAT.get());
            event.accept(ModItems.RABBIT_TREAT.get());
            event.accept(ModItems.SALMON_TREAT.get());
            event.accept(ModItems.HEALING_TREAT.get());

            event.accept(ModItems.GENE_TESTER.get());
            event.accept(ModItems.GENO_READER.get());
            event.accept(ModItems.PAPER_STRIP.get());
            event.accept(ModItems.USED_PAPER_STRIP.get());

            event.accept(ModItems.WHITE_COLLAR.get());
            event.accept(ModItems.LIGHT_GRAY_COLLAR.get());
            event.accept(ModItems.GRAY_COLLAR.get());
            event.accept(ModItems.BLACK_COLLAR.get());
            event.accept(ModItems.BROWN_COLLAR.get());
            event.accept(ModItems.RED_COLLAR.get());
            event.accept(ModItems.ORANGE_COLLAR.get());
            event.accept(ModItems.YELLOW_COLLAR.get());
            event.accept(ModItems.LIME_COLLAR.get());
            event.accept(ModItems.GREEN_COLLAR.get());
            event.accept(ModItems.CYAN_COLLAR.get());
            event.accept(ModItems.LIGHT_BLUE_COLLAR.get());
            event.accept(ModItems.BLUE_COLLAR.get());
            event.accept(ModItems.PURPLE_COLLAR.get());
            event.accept(ModItems.MAGENTA_COLLAR.get());
            event.accept(ModItems.PINK_COLLAR.get());

            event.accept(ModItems.WHITE_REINFORCED_COLLAR.get());
            event.accept(ModItems.LIGHT_GRAY_REINFORCED_COLLAR.get());
            event.accept(ModItems.GRAY_REINFORCED_COLLAR.get());
            event.accept(ModItems.BLACK_REINFORCED_COLLAR.get());
            event.accept(ModItems.BROWN_REINFORCED_COLLAR.get());
            event.accept(ModItems.RED_REINFORCED_COLLAR.get());
            event.accept(ModItems.ORANGE_REINFORCED_COLLAR.get());
            event.accept(ModItems.YELLOW_REINFORCED_COLLAR.get());
            event.accept(ModItems.LIME_REINFORCED_COLLAR.get());
            event.accept(ModItems.GREEN_REINFORCED_COLLAR.get());
            event.accept(ModItems.CYAN_REINFORCED_COLLAR.get());
            event.accept(ModItems.LIGHT_BLUE_REINFORCED_COLLAR.get());
            event.accept(ModItems.BLUE_REINFORCED_COLLAR.get());
            event.accept(ModItems.PURPLE_REINFORCED_COLLAR.get());
            event.accept(ModItems.MAGENTA_REINFORCED_COLLAR.get());
            event.accept(ModItems.PINK_REINFORCED_COLLAR.get());

            event.accept(ModItems.WHITE_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.LIGHT_GRAY_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.GRAY_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.BLACK_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.BROWN_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.RED_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.ORANGE_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.YELLOW_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.LIME_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.GREEN_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.CYAN_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.LIGHT_BLUE_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.BLUE_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.PURPLE_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.MAGENTA_GOLD_PLATED_COLLAR.get());
            event.accept(ModItems.PINK_GOLD_PLATED_COLLAR.get());

            event.accept(ModItems.WHITE_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.LIGHT_GRAY_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.GRAY_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.BLACK_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.BROWN_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.RED_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.ORANGE_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.YELLOW_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.LIME_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.GREEN_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.CYAN_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.LIGHT_BLUE_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.BLUE_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.PURPLE_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.MAGENTA_IRON_INFUSED_COLLAR.get());
            event.accept(ModItems.PINK_IRON_INFUSED_COLLAR.get());

            event.accept(ModItems.WHITE_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.LIGHT_GRAY_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.GRAY_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.BLACK_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.BROWN_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.RED_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.ORANGE_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.YELLOW_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.LIME_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.GREEN_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.CYAN_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.LIGHT_BLUE_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.BLUE_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.PURPLE_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.MAGENTA_DIAMOND_CRUSTED_COLLAR.get());
            event.accept(ModItems.PINK_DIAMOND_CRUSTED_COLLAR.get());

            event.accept(ModItems.WHITE_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.LIGHT_GRAY_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.GRAY_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.BLACK_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.BROWN_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.RED_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.ORANGE_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.YELLOW_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.LIME_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.GREEN_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.CYAN_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.LIGHT_BLUE_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.BLUE_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.PURPLE_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.MAGENTA_NETHERITE_LACED_COLLAR.get());
            event.accept(ModItems.PINK_NETHERITE_LACED_COLLAR.get());

            event.accept(ModBlocks.CARE_STATION.get());
            event.accept(ModBlocks.GENO_PRINTER.get());
        }

        if (event.getTab() == ModCreativeModeTab.MODOGS_SPAWNER_TAB.get()) {
            event.accept(ModItems.GERMAN_SHEPHERD_SPAWN_EGG.get());
            event.accept(ModItems.BORDER_COLLIE_SPAWN_EGG.get());
            event.accept(ModItems.GOLDEN_RETRIEVER_SPAWN_EGG.get());
            event.accept(ModItems.LAB_RETRIEVER_SPAWN_EGG.get());
            event.accept(ModItems.DACHSHUND_SPAWN_EGG.get());
            event.accept(ModItems.DALMATIAN_SPAWN_EGG.get());
            event.accept(ModItems.CARDIGAN_CORGI_SPAWN_EGG.get());
            event.accept(ModItems.PEMBROKE_CORGI_SPAWN_EGG.get());
            event.accept(ModItems.RUSSELL_TERRIER_SPAWN_EGG.get());
            event.accept(ModItems.ALASKAN_MALAMUTE_SPAWN_EGG.get());
            event.accept(ModItems.BERNESE_MOUNTAIN_DOG_SPAWN_EGG.get());
            event.accept(ModItems.SAINT_BERNARD_SPAWN_EGG.get());
            event.accept(ModItems.BLOODHOUND_SPAWN_EGG.get());
            event.accept(ModItems.BOXER_SPAWN_EGG.get());
            event.accept(ModItems.GREYHOUND_SPAWN_EGG.get());
            event.accept(ModItems.PIT_BULL_SPAWN_EGG.get());
            event.accept(ModItems.GREAT_DANE_SPAWN_EGG.get());
            event.accept(ModItems.MASTIFF_SPAWN_EGG.get());
            event.accept(ModItems.SHIBA_INU_SPAWN_EGG.get());
            event.accept(ModItems.SHETLAND_SHEEPDOG_SPAWN_EGG.get());
            event.accept(ModItems.BOSTON_TERRIER_SPAWN_EGG.get());
            event.accept(ModItems.SCOTTISH_TERRIER_SPAWN_EGG.get());
            event.accept(ModItems.CK_CHARLES_SPANIEL_SPAWN_EGG.get());
            event.accept(ModItems.ITALIAN_GREYHOUND_SPAWN_EGG.get());
            event.accept(ModItems.AUSTRALIAN_SHEPHERD_SPAWN_EGG.get());
            event.accept(ModItems.BASENJI_SPAWN_EGG.get());
            event.accept(ModItems.PUG_SPAWN_EGG.get());
            event.accept(ModItems.COCKER_SPANIEL_SPAWN_EGG.get());
            event.accept(ModItems.BULL_TERRIER_SPAWN_EGG.get());
            event.accept(ModItems.MINI_BULL_TERRIER_SPAWN_EGG.get());
            event.accept(ModItems.SCHNAUZER_SPAWN_EGG.get());
            event.accept(ModItems.MINI_SCHNAUZER_SPAWN_EGG.get());
            event.accept(ModItems.POODLE_SPAWN_EGG.get());
            event.accept(ModItems.TOY_POODLE_SPAWN_EGG.get());
            event.accept(ModItems.DOBERMAN_SPAWN_EGG.get());
            event.accept(ModItems.MINI_PINSCHER_SPAWN_EGG.get());
            event.accept(ModItems.HUSKY_SPAWN_EGG.get());
            event.accept(ModItems.REDBONE_COONHOUND_SPAWN_EGG.get());
            event.accept(ModItems.TREE_WALK_HOUND_SPAWN_EGG.get());
            event.accept(ModItems.AIREDALE_TERRIER_SPAWN_EGG.get());
            event.accept(ModItems.AMERICAN_FOXHOUND_SPAWN_EGG.get());
            event.accept(ModItems.BULLDOG_SPAWN_EGG.get());
            event.accept(ModItems.COLLIE_SPAWN_EGG.get());
            event.accept(ModItems.MUDI_SPAWN_EGG.get());
            event.accept(ModItems.NORWEGIAN_ELKHOUND_SPAWN_EGG.get());
            event.accept(ModItems.BEAGLE_SPAWN_EGG.get());
            event.accept(ModItems.ROTTWEILER_SPAWN_EGG.get());
            event.accept(ModItems.IRISH_SETTER_SPAWN_EGG.get());
            event.accept(ModItems.GERMAN_SPITZ_SPAWN_EGG.get());
            event.accept(ModItems.WHIPPET_SPAWN_EGG.get());
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    //@Mod(MOD_ID)
    @EventBusSubscriber(modid = MoDogs.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
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
        }
    }
}