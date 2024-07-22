package com.kitnjinx.modogs.villager;

import com.google.common.collect.ImmutableSet;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.block.ModBlocks;
import com.kitnjinx.modogs.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, MoDogs.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, MoDogs.MOD_ID);

    public static final Supplier<PoiType> CARE_STATION_POI = POI_TYPES.register("care_station_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.CARE_STATION.get().getStateDefinition()
                    .getPossibleStates()), 1, 1));

    public static final Supplier<VillagerProfession> SHELTER_WORKER = VILLAGER_PROFESSIONS.register(
            "shelter_worker", () -> new VillagerProfession("shelter_worker",
                    x -> x.is(ModTags.SHELTER_WORKER), x -> x.is(ModTags.SHELTER_WORKER), ImmutableSet.of(),
                    ImmutableSet.of(), SoundEvents.VILLAGER_WORK_SHEPHERD));

    @SubscribeEvent
    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
