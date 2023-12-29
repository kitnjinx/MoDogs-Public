package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum GreyhoundVariant {
    WHITE_BLACK(0),
    WHITE_RED(1),
    WHITE_BLUE(2),
    WHITE(3),
    BLACK(4),
    RED(5),
    BLUE(6);

    private static final GreyhoundVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(GreyhoundVariant::getId)).toArray(GreyhoundVariant[]::new);
    private final int id;

    GreyhoundVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GreyhoundVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
