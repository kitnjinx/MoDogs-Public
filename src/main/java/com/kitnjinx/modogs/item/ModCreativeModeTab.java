package com.kitnjinx.modogs.item;

import com.kitnjinx.modogs.MoDogs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoDogs.MOD_ID);

    public static RegistryObject<CreativeModeTab> MODOGS_ITEMS_TAB =
            CREATIVE_MODE_TABS.register("modogs_items_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.RABBIT_TREAT.get()))
                    .title(Component.translatable("creativemodetab.modogs_items_tab")).build());

    public static RegistryObject<CreativeModeTab> MODOGS_SPAWNER_TAB =
            CREATIVE_MODE_TABS.register("modogs_spawner_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.GERMAN_SHEPHERD_SPAWN_EGG.get()))
                    .title(Component.translatable("creativemodetab.modogs_spawner_tab")).build());;

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
