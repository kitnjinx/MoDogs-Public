package com.kitnjinx.modogs.event;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MoDogs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
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
    }
}
