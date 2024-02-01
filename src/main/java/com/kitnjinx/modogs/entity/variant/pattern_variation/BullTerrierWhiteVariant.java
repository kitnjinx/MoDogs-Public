package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum BullTerrierWhiteVariant {
    PATTERN1(0),
    PATTERN2(1);

    private static final BullTerrierWhiteVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BullTerrierWhiteVariant::getId)).toArray(BullTerrierWhiteVariant[]::new);
    private final int id;

    BullTerrierWhiteVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BullTerrierWhiteVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
