package com.kitnjinx.modogs;

import com.kitnjinx.modogs.block.ModBlocks;
import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.custom.*;
import com.kitnjinx.modogs.item.ModItems;
import com.kitnjinx.modogs.villager.ModVillagers;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MoDogs.MOD_ID)
public class MoDogs
{
    public static final String MOD_ID = "modogs";

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public MoDogs() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);

        ModVillagers.register(eventBus);
        ModEntityTypes.register(eventBus);

        eventBus.addListener(this::setup);
        //eventBus.addListener(this::clientSetup);

        GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    //clientSetup method moved to ModEventClientBusEvents class
    /*
    private void clientSetup(final FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntityTypes.GERMAN_SHEPHERD.get(), GermanShepherdRenderer::new);
    }
     */

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModVillagers.registerPOIs();

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
                    Animal::checkAnimalSpawnRules);

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
        });
    }
}
