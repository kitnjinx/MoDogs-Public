package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum RussellTerrierWhiteVariant {
    WHITE1(0),
    WHITE2(1),
    WHITE3(2),
    WHITE4(3),
    WHITE5(4),
    WHITE6(5);

    private static final RussellTerrierWhiteVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(RussellTerrierWhiteVariant::getId)).toArray(RussellTerrierWhiteVariant[]::new);
    private final int id;

    RussellTerrierWhiteVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static RussellTerrierWhiteVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
