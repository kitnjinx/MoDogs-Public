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
        event.put(ModEntityTypes.CK_CHARLES_SPANIEL.get(), CKCharlesSpanielEntity.setAttributes());
        event.put(ModEntityTypes.ITALIAN_GREYHOUND.get(), ItalianGreyhoundEntity.setAttributes());
        event.put(ModEntityTypes.AUSTRALIAN_SHEPHERD.get(), AustralianShepherdEntity.setAttributes());
        event.put(ModEntityTypes.BASENJI.get(), BasenjiEntity.setAttributes());
        event.put(ModEntityTypes.PUG.get(), PugEntity.setAttributes());
        event.put(ModEntityTypes.COCKER_SPANIEL.get(), CockerSpanielEntity.setAttributes());
        event.put(ModEntityTypes.BULL_TERRIER.get(), BullTerrierEntity.setAttributes());
        event.put(ModEntityTypes.MINI_BULL_TERRIER.get(), MiniBullTerrierEntity.setAttributes());
        event.put(ModEntityTypes.SCHNAUZER.get(), SchnauzerEntity.setAttributes());
        event.put(ModEntityTypes.MINI_SCHNAUZER.get(), MiniSchnauzerEntity.setAttributes());
        event.put(ModEntityTypes.POODLE.get(), PoodleEntity.setAttributes());
        event.put(ModEntityTypes.TOY_POODLE.get(), ToyPoodleEntity.setAttributes());
        event.put(ModEntityTypes.DOBERMAN.get(), DobermanEntity.setAttributes());
        event.put(ModEntityTypes.MINI_PINSCHER.get(), MiniPinscherEntity.setAttributes());
        event.put(ModEntityTypes.HUSKY.get(), HuskyEntity.setAttributes());
        event.put(ModEntityTypes.REDBONE_COONHOUND.get(), RedboneCoonhoundEntity.setAttributes());
        event.put(ModEntityTypes.TREE_WALK_HOUND.get(), TreeWalkHoundEntity.setAttributes());
        event.put(ModEntityTypes.AIREDALE_TERRIER.get(), AiredaleTerrierEntity.setAttributes());
        event.put(ModEntityTypes.AMERICAN_FOXHOUND.get(), AmericanFoxhoundEntity.setAttributes());
        event.put(ModEntityTypes.BULLDOG.get(), BulldogEntity.setAttributes());
        event.put(ModEntityTypes.COLLIE.get(), CollieEntity.setAttributes());
        event.put(ModEntityTypes.MUDI.get(), MudiEntity.setAttributes());
        event.put(ModEntityTypes.NORWEGIAN_ELKHOUND.get(), NorwegianElkhoundEntity.setAttributes());
    }
}
