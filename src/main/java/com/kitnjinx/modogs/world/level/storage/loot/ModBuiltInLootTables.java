package com.kitnjinx.modogs.world.level.storage.loot;

import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.Set;

public class ModBuiltInLootTables {
    private static final Set<ResourceLocation> LOCATIONS = Sets.newHashSet();
    private static final Set<ResourceLocation> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);

    public static final ResourceKey<LootTable> WHITE_COLLAR = register("modogs:entities/collars/white");
    public static final ResourceKey<LootTable> LIGHT_GRAY_COLLAR = register("modogs:entities/collars/light_gray");
    public static final ResourceKey<LootTable> GRAY_COLLAR = register("modogs:entities/collars/gray");
    public static final ResourceKey<LootTable> BLACK_COLLAR = register("modogs:entities/collars/black");
    public static final ResourceKey<LootTable> BROWN_COLLAR = register("modogs:entities/collars/brown");
    public static final ResourceKey<LootTable> RED_COLLAR = register("modogs:entities/collars/red");
    public static final ResourceKey<LootTable> ORANGE_COLLAR = register("modogs:entities/collars/orange");
    public static final ResourceKey<LootTable> YELLOW_COLLAR = register("modogs:entities/collars/yellow");
    public static final ResourceKey<LootTable> LIME_COLLAR = register("modogs:entities/collars/lime");
    public static final ResourceKey<LootTable> GREEN_COLLAR = register("modogs:entities/collars/green");
    public static final ResourceKey<LootTable> CYAN_COLLAR = register("modogs:entities/collars/cyan");
    public static final ResourceKey<LootTable> LIGHT_BLUE_COLLAR = register("modogs:entities/collars/light_blue");
    public static final ResourceKey<LootTable> BLUE_COLLAR = register("modogs:entities/collars/blue");
    public static final ResourceKey<LootTable> PURPLE_COLLAR = register("modogs:entities/collars/purple");
    public static final ResourceKey<LootTable> MAGENTA_COLLAR = register("modogs:entities/collars/magenta");
    public static final ResourceKey<LootTable> PINK_COLLAR = register("modogs:entities/collars/pink");

    public static final ResourceKey<LootTable> WHITE_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/white");
    public static final ResourceKey<LootTable> LIGHT_GRAY_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/light_gray");
    public static final ResourceKey<LootTable> GRAY_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/gray");
    public static final ResourceKey<LootTable> BLACK_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/black");
    public static final ResourceKey<LootTable> BROWN_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/brown");
    public static final ResourceKey<LootTable> RED_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/red");
    public static final ResourceKey<LootTable> ORANGE_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/orange");
    public static final ResourceKey<LootTable> YELLOW_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/yellow");
    public static final ResourceKey<LootTable> LIME_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/lime");
    public static final ResourceKey<LootTable> GREEN_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/green");
    public static final ResourceKey<LootTable> CYAN_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/cyan");
    public static final ResourceKey<LootTable> LIGHT_BLUE_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/light_blue");
    public static final ResourceKey<LootTable> BLUE_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/blue");
    public static final ResourceKey<LootTable> PURPLE_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/purple");
    public static final ResourceKey<LootTable> MAGENTA_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/magenta");
    public static final ResourceKey<LootTable> PINK_REINFORCED_COLLAR = register("modogs:entities/reinforced_collars/pink");

    public static final ResourceKey<LootTable> WHITE_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/white");
    public static final ResourceKey<LootTable> LIGHT_GRAY_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/light_gray");
    public static final ResourceKey<LootTable> GRAY_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/gray");
    public static final ResourceKey<LootTable> BLACK_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/black");
    public static final ResourceKey<LootTable> BROWN_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/brown");
    public static final ResourceKey<LootTable> RED_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/red");
    public static final ResourceKey<LootTable> ORANGE_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/orange");
    public static final ResourceKey<LootTable> YELLOW_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/yellow");
    public static final ResourceKey<LootTable> LIME_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/lime");
    public static final ResourceKey<LootTable> GREEN_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/green");
    public static final ResourceKey<LootTable> CYAN_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/cyan");
    public static final ResourceKey<LootTable> LIGHT_BLUE_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/light_blue");
    public static final ResourceKey<LootTable> BLUE_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/blue");
    public static final ResourceKey<LootTable> PURPLE_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/purple");
    public static final ResourceKey<LootTable> MAGENTA_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/magenta");
    public static final ResourceKey<LootTable> PINK_GOLD_PLATED_COLLAR = register("modogs:entities/gold_plated_collars/pink");

    public static final ResourceKey<LootTable> WHITE_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/white");
    public static final ResourceKey<LootTable> LIGHT_GRAY_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/light_gray");
    public static final ResourceKey<LootTable> GRAY_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/gray");
    public static final ResourceKey<LootTable> BLACK_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/black");
    public static final ResourceKey<LootTable> BROWN_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/brown");
    public static final ResourceKey<LootTable> RED_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/red");
    public static final ResourceKey<LootTable> ORANGE_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/orange");
    public static final ResourceKey<LootTable> YELLOW_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/yellow");
    public static final ResourceKey<LootTable> LIME_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/lime");
    public static final ResourceKey<LootTable> GREEN_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/green");
    public static final ResourceKey<LootTable> CYAN_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/cyan");
    public static final ResourceKey<LootTable> LIGHT_BLUE_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/light_blue");
    public static final ResourceKey<LootTable> BLUE_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/blue");
    public static final ResourceKey<LootTable> PURPLE_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/purple");
    public static final ResourceKey<LootTable> MAGENTA_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/magenta");
    public static final ResourceKey<LootTable> PINK_IRON_INFUSED_COLLAR = register("modogs:entities/iron_infused_collars/pink");

    public static final ResourceKey<LootTable> WHITE_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/white");
    public static final ResourceKey<LootTable> LIGHT_GRAY_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/light_gray");
    public static final ResourceKey<LootTable> GRAY_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/gray");
    public static final ResourceKey<LootTable> BLACK_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/black");
    public static final ResourceKey<LootTable> BROWN_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/brown");
    public static final ResourceKey<LootTable> RED_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/red");
    public static final ResourceKey<LootTable> ORANGE_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/orange");
    public static final ResourceKey<LootTable> YELLOW_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/yellow");
    public static final ResourceKey<LootTable> LIME_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/lime");
    public static final ResourceKey<LootTable> GREEN_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/green");
    public static final ResourceKey<LootTable> CYAN_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/cyan");
    public static final ResourceKey<LootTable> LIGHT_BLUE_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/light_blue");
    public static final ResourceKey<LootTable> BLUE_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/blue");
    public static final ResourceKey<LootTable> PURPLE_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/purple");
    public static final ResourceKey<LootTable> MAGENTA_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/magenta");
    public static final ResourceKey<LootTable> PINK_DIAMOND_CRUSTED_COLLAR = register("modogs:entities/diamond_crusted_collars/pink");

    public static final ResourceKey<LootTable> WHITE_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/white");
    public static final ResourceKey<LootTable> LIGHT_GRAY_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/light_gray");
    public static final ResourceKey<LootTable> GRAY_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/gray");
    public static final ResourceKey<LootTable> BLACK_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/black");
    public static final ResourceKey<LootTable> BROWN_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/brown");
    public static final ResourceKey<LootTable> RED_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/red");
    public static final ResourceKey<LootTable> ORANGE_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/orange");
    public static final ResourceKey<LootTable> YELLOW_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/yellow");
    public static final ResourceKey<LootTable> LIME_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/lime");
    public static final ResourceKey<LootTable> GREEN_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/green");
    public static final ResourceKey<LootTable> CYAN_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/cyan");
    public static final ResourceKey<LootTable> LIGHT_BLUE_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/light_blue");
    public static final ResourceKey<LootTable> BLUE_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/blue");
    public static final ResourceKey<LootTable> PURPLE_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/purple");
    public static final ResourceKey<LootTable> MAGENTA_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/magenta");
    public static final ResourceKey<LootTable> PINK_NETHERITE_LACED_COLLAR = register("modogs:entities/netherite_laced_collars/pink");

    private static ResourceKey<LootTable> register(String pId) {
        return register(new ResourceLocation(pId));
    }

    private static ResourceKey<LootTable> register(ResourceLocation pId) {
        if (LOCATIONS.add(pId)) {
            return ResourceKey.create(Registries.LOOT_TABLE, pId);
        } else {
            throw new IllegalArgumentException(pId + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> all() {
        return IMMUTABLE_LOCATIONS;
    }
}
