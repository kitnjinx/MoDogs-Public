package com.kitnjinx.modogs.item;

import com.kitnjinx.modogs.MoDogs;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoDogs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTab {
    public static CreativeModeTab MODOGS_ITEMS_TAB;

    public static CreativeModeTab MODOGS_SPAWNER_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        MODOGS_ITEMS_TAB = event.registerCreativeModeTab(new ResourceLocation(MoDogs.MOD_ID, "modogs_items_tab"),
                builder -> builder.icon(() -> new ItemStack(ModItems.RABBIT_TREAT.get()))
                        .title(Component.translatable("creativemodetab.modogs_items_tab")).build());

        MODOGS_SPAWNER_TAB = event.registerCreativeModeTab(new ResourceLocation(MoDogs.MOD_ID, "modogs_spawner_tab"),
                builder -> builder.icon(() -> new ItemStack(ModItems.GERMAN_SHEPHERD_SPAWN_EGG.get()))
                        .title(Component.translatable("creativemodetab.modogs_spawner_tab")).build());
    }
}
