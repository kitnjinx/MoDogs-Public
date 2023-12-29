package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum RussellTerrierVariant {
    BROWN(0),
    BLACK(1),
    TAN(2),
    CREAM(3),
    TRI_BROWN(4),
    TRI_TAN(5);

    private static final RussellTerrierVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(RussellTerrierVariant::getId)).toArray(RussellTerrierVariant[]::new);
    private final int id;

    RussellTerrierVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static RussellTerrierVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
