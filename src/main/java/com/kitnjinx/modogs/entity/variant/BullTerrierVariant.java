package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BullTerrierVariant {
    BLACK(0),
    RED(1);

    private static final BullTerrierVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BullTerrierVariant::getId)).toArray(BullTerrierVariant[]::new);
    private final int id;

    BullTerrierVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BullTerrierVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
