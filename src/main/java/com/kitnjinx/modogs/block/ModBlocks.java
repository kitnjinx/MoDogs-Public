package com.kitnjinx.modogs.block;

import com.kitnjinx.modogs.MoDogs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MoDogs.MOD_ID);

    public static final DeferredBlock<Block> CARE_STATION = BLOCKS.register("care_station",
            () -> new CareStationBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS).strength(2F).sound(SoundType.WOOD).
                    ignitedByLava()));

    public static final DeferredBlock<Block> GENO_PRINTER = BLOCKS.register("geno_printer",
            () -> new GenoPrinterBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops().strength(5F, 1200.0F)
                    .sound(SoundType.METAL).noOcclusion()));

    @SubscribeEvent
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
