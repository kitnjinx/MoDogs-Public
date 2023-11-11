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
    }
}
