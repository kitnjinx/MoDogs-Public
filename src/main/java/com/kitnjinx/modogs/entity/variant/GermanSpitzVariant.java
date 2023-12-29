package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum GermanSpitzVariant {
    WHITE(0),
    RED(1),
    BLACK(2),
    BROWN(3);

    private static final GermanSpitzVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(GermanSpitzVariant::getId)).toArray(GermanSpitzVariant[]::new);
    private final int id;

    GermanSpitzVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GermanSpitzVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
