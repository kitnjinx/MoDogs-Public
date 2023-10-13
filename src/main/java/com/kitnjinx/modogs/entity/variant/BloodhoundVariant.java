package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BloodhoundVariant {
    BLACK_TAN(0),
    LIVER_TAN(1),
    RED(2);

    private static final BloodhoundVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BloodhoundVariant::getId)).toArray(BloodhoundVariant[]::new);
    private final int id;

    BloodhoundVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BloodhoundVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
