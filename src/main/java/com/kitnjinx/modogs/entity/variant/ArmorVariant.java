package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum ArmorVariant {
    NONE(0),
    REINFORCED(1),
    GOLD_PLATED(2),
    IRON_INFUSED(3),
    DIAMOND_CRUSTED(4),
    NETHERITE_LACED(5);

    private static final ArmorVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(ArmorVariant::getId)).toArray(ArmorVariant[]::new);
    private final int id;

    ArmorVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ArmorVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
