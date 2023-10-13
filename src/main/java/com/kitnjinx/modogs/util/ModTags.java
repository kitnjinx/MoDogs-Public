package com.kitnjinx.modogs.util;

import com.kitnjinx.modogs.MoDogs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> DOG_TREAT = tag("dog_treat");
        public static final TagKey<Item> HEALING_TREAT_INGREDIENT = tag("healing_treat_ingredient");
        public static final TagKey<Item> COLLAR = tag("collar");
        public static final TagKey<Item> REINFORCED_COLLAR = tag("reinforced_collar");
        public static final TagKey<Item> GOLD_PLATED_COLLAR = tag("gold_plated_collar");
        public static final TagKey<Item> IRON_INFUSED_COLLAR = tag("iron_infused_collar");
        public static final TagKey<Item> DIAMOND_CRUSTED_COLLAR = tag("diamond_crusted_collar");
        public static final TagKey<Item> NETHERITE_LACED_COLLAR = tag("netherite_laced_collar");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(MoDogs.MOD_ID, name));
        }
    }
}
