package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum ShibaInuVariant {
    RED(0),
    BLACK_TAN(1),
    CREAM(2),
    DARK_CREAM(3);

    private static final ShibaInuVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(ShibaInuVariant::getId)).toArray(ShibaInuVariant[]::new);
    private final int id;

    ShibaInuVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ShibaInuVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
