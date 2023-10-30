package com.kitnjinx.modogs.event;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.custom.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoDogs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.GERMAN_SHEPHERD.get(), GermanShepherdEntity.setAttributes());
        event.put(ModEntityTypes.BORDER_COLLIE.get(), BorderCollieEntity.setAttributes());
        event.put(ModEntityTypes.GOLDEN_RETRIEVER.get(), GoldenRetrieverEntity.setAttributes());
        event.put(ModEntityTypes.LAB_RETRIEVER.get(), LabRetrieverEntity.setAttributes());
        event.put(ModEntityTypes.DACHSHUND.get(), DachshundEntity.setAttributes());
        event.put(ModEntityTypes.DALMATIAN.get(), DalmatianEntity.setAttributes());
        event.put(ModEntityTypes.CARDIGAN_CORGI.get(), CardiganCorgiEntity.setAttributes());
        event.put(ModEntityTypes.PEMBROKE_CORGI.get(), PembrokeCorgiEntity.setAttributes());
        event.put(ModEntityTypes.RUSSELL_TERRIER.get(), RussellTerrierEntity.setAttributes());
        event.put(ModEntityTypes.ALASKAN_MALAMUTE.get(), AlaskanMalamuteEntity.setAttributes());
        event.put(ModEntityTypes.BERNESE_MOUNTAIN_DOG.get(), BerneseMountainDogEntity.setAttributes());
        event.put(ModEntityTypes.SAINT_BERNARD.get(), SaintBernardEntity.setAttributes());
        event.put(ModEntityTypes.BLOODHOUND.get(), BloodhoundEntity.setAttributes());
        event.put(ModEntityTypes.BOXER.get(), BoxerEntity.setAttributes());
        event.put(ModEntityTypes.GREYHOUND.get(), GreyhoundEntity.setAttributes());
        event.put(ModEntityTypes.PIT_BULL.get(), PitBullEntity.setAttributes());
        event.put(ModEntityTypes.GREAT_DANE.get(), GreatDaneEntity.setAttributes());
        event.put(ModEntityTypes.MASTIFF.get(), MastiffEntity.setAttributes());
        event.put(ModEntityTypes.SHIBA_INU.get(), ShibaInuEntity.setAttributes());
        event.put(ModEntityTypes.SHETLAND_SHEEPDOG.get(), ShetlandSheepdogEntity.setAttributes());
        event.put(ModEntityTypes.BOSTON_TERRIER.get(), BostonTerrierEntity.setAttributes());
        event.put(ModEntityTypes.SCOTTISH_TERRIER.get(), ScottishTerrierEntity.setAttributes());
    }
}
