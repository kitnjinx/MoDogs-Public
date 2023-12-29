package com.kitnjinx.modogs;

import com.kitnjinx.modogs.block.ModBlocks;
import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.client.renderer.*;
import com.kitnjinx.modogs.entity.custom.AbstractDog;
import com.kitnjinx.modogs.item.ModItems;
import com.kitnjinx.modogs.villager.ModVillagers;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod(MoDogs.MOD_ID)
public class MoDogs {
    public static final String MOD_ID = "modogs";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MoDogs() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModVillagers.register(modEventBus);

        ModEntityTypes.register(modEventBus);

        GeckoLib.initialize();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModVillagers.registerPOIS();

            SpawnPlacements.register(ModEntityTypes.GERMAN_SHEPHERD.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.BORDER_COLLIE.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.GOLDEN_RETRIEVER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.LAB_RETRIEVER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.DACHSHUND.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.DALMATIAN.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.CARDIGAN_CORGI.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.PEMBROKE_CORGI.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.RUSSELL_TERRIER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.ALASKAN_MALAMUTE.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.SAINT_BERNARD.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.BLOODHOUND.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.BOXER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.GREYHOUND.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.BOXER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.PIT_BULL.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.GREAT_DANE.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.MASTIFF.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.SHIBA_INU.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.SHETLAND_SHEEPDOG.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.BOSTON_TERRIER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.SCOTTISH_TERRIER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.CK_CHARLES_SPANIEL.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.ITALIAN_GREYHOUND.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.AUSTRALIAN_SHEPHERD.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.BASENJI.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.PUG.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.COCKER_SPANIEL.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.BULL_TERRIER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.MINI_BULL_TERRIER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.SCHNAUZER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.MINI_SCHNAUZER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.POODLE.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.TOY_POODLE.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.DOBERMAN.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.MINI_PINSCHER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.HUSKY.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.REDBONE_COONHOUND.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.TREE_WALK_HOUND.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.AIREDALE_TERRIER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.AMERICAN_FOXHOUND.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.BULLDOG.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.COLLIE.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);

            SpawnPlacements.register(ModEntityTypes.MUDI.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.NORWEGIAN_ELKHOUND.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.BEAGLE.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.ROTTWEILER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.IRISH_SETTER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.GERMAN_SPITZ.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);

            SpawnPlacements.register(ModEntityTypes.WHIPPET.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    AbstractDog::checkDogSpawnRules);
        });
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
        }
    }
}
