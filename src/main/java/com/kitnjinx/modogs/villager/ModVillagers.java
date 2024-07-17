package com.kitnjinx.modogs.villager;

import com.google.common.collect.ImmutableSet;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.block.ModBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, MoDogs.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, MoDogs.MOD_ID);

    public static final RegistryObject<PoiType> CARE_STATION_POI = POI_TYPES.register("care_station_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.CARE_STATION.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> SHELTER_WORKER = VILLAGER_PROFESSIONS.register("shelter_worker",
            () -> new VillagerProfession("shelter_worker", x -> x.get() == CARE_STATION_POI.get(),
                    x -> x.get() == CARE_STATION_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_SHEPHERD));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
