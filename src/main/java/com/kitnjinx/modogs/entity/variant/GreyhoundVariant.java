package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum GreyhoundVariant {
    WHITE(0),
    BLACK(1),
    RED(2),
    BLUE(3);

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
