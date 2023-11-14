package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum DobermanVariant {
    BLACK(0),
    RED(1),
    BLUE(2),
    FAWN(3);

    private static final DobermanVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(DobermanVariant::getId)).toArray(DobermanVariant[]::new);
    private final int id;

    DobermanVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static DobermanVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
