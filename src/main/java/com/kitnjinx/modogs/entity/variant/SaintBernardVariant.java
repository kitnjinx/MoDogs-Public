package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum SaintBernardVariant {
    BROWN(0),
    RED(1),
    ORANGE(2),
    YELLOW_BROWN(3),
    MAHOGANY(4);

    private static final SaintBernardVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(SaintBernardVariant::getId)).toArray(SaintBernardVariant[]::new);
    private final int id;

    SaintBernardVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static SaintBernardVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
