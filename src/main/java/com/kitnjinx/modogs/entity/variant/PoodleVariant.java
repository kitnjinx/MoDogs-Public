package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum PoodleVariant {
    BLACK(0),
    BROWN(1),
    WHITE(2),
    SILVER(3),
    CREAM(4);

    private static final PoodleVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(PoodleVariant::getId)).toArray(PoodleVariant[]::new);
    private final int id;

    PoodleVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PoodleVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
