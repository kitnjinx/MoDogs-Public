package com.kitnjinx.modogs.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab MODOGS_ITEMS_TAB = new CreativeModeTab("modogs_items_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.RABBIT_TREAT.get());
        }
    };

    /*public static final CreativeModeTab MODOGS_SPAWNER_TAB = new CreativeModeTab("modogs_spawner_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.GERMAN_SHEPHERD_SPAWN_EGG.get());
        }
    };*/
}
