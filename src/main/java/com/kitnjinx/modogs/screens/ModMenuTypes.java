package com.kitnjinx.modogs.screens;

import com.kitnjinx.modogs.MoDogs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MoDogs.MOD_ID);

    public static final Supplier<MenuType<GenoPrinterMenu>> GENO_PRINTER_MENU = MENUS.register(
            "geno_printer_menu", () -> new MenuType<>(GenoPrinterMenu::new, FeatureFlags.DEFAULT_FLAGS));

    @SubscribeEvent
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
