package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum DachshundVariant {
    BLACK_TAN(0),
    CHOCOLATE_TAN(1),
    FAWN_TAN(2),
    BLACK_CREAM(3),
    CHOCOLATE_CREAM(4),
    FAWN_CREAM(5);

    private static final DachshundVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(DachshundVariant::getId)).toArray(DachshundVariant[]::new);
    private final int id;

    DachshundVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static DachshundVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
