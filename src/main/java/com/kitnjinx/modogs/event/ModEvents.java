package com.kitnjinx.modogs.event;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.ModEntityTypes;
import com.kitnjinx.modogs.entity.custom.*;
import com.kitnjinx.modogs.item.ModItems;
import com.kitnjinx.modogs.villager.ModVillagers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = MoDogs.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void addCustomTrades(VillagerTradesEvent event) {
            /* LEVEL 1 (NOVICE) TRADES */
            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(Items.EMERALD, 1);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.MUTTON_TREAT.get(), 15),
                        stack, 10, 2, 0.02f));
            }

            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(Items.EMERALD, 1);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.RABBIT_TREAT.get(), 15),
                        stack, 10, 2, 0.02f));
            }

            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(Items.EMERALD, 1);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.CHICKEN_TREAT.get(), 15),
                        stack, 10, 2, 0.02f));
            }

            /* LEVEL 2 (APPRENTICE) TRADES */
            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(Items.EMERALD, 1);
                int villagerLevel = 2;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.BEEF_TREAT.get(), 15),
                        stack, 10, 5, 0.02f));
            }

            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.BROWN_COLLAR.get(), 1);
                int villagerLevel = 2;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 3),
                        stack, 5, 10, 0.1f));
            }

            /* LEVEL 3 (JOURNEYMAN) TRADES */
            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(Items.EMERALD, 1);
                int villagerLevel = 3;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.SALMON_TREAT.get(), 15),
                        stack, 10, 10, 0.02f));
            }

            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.BROWN_REINFORCED_COLLAR.get(), 1);
                int villagerLevel = 3;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 6),
                        stack, 5, 15, 0.1f));
            }

            /* LEVEL 4 (EXPERT) TRADES */
            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(Items.EMERALD, 1);
                int villagerLevel = 4;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(ModItems.BACON_TREAT.get(), 15),
                        stack, 10, 15, 0.02f));
            }

            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.BROWN_IRON_INFUSED_COLLAR.get(), 1);
                int villagerLevel = 4;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 12),
                        stack, 5, 20, 0.1f));
            }

            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.BROWN_GOLD_PLATED_COLLAR.get(), 1);
                int villagerLevel = 4;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 10),
                        stack, 5, 20, 0.1f));
            }

            /* LEVEL 5 (MASTER) TRADES */
            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.BROWN_DIAMOND_CRUSTED_COLLAR.get(), 1);
                int villagerLevel = 5;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 18),
                        stack, 5, 20, 0.2f));
            }

            if (event.getType() == ModVillagers.SHELTER_WORKER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.BROWN_NETHERITE_LACED_COLLAR.get(), 1);
                int villagerLevel = 5;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 32),
                        stack, 5, 20, 0.2f));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MoDogs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
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
            event.put(ModEntityTypes.BEAGLE.get(), BeagleEntity.setAttributes());
            event.put(ModEntityTypes.ROTTWEILER.get(), RottweilerEntity.setAttributes());
            event.put(ModEntityTypes.IRISH_SETTER.get(), IrishSetterEntity.setAttributes());
            event.put(ModEntityTypes.GERMAN_SPITZ.get(), GermanSpitzEntity.setAttributes());
            event.put(ModEntityTypes.WHIPPET.get(), WhippetEntity.setAttributes());
        }

        @SubscribeEvent
        public static void entitySpawnRestriction(SpawnPlacementRegisterEvent event) {
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
        }
    }
}
