package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum HuskyVariant {
    BLACK(0),
    GRAY(1),
    RED(2),
    SABLE(3),
    WHITE(4);

    private static final HuskyVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(HuskyVariant::getId)).toArray(HuskyVariant[]::new);
    private final int id;

    HuskyVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static HuskyVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
