package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BeagleVariant {
    BLACK_TAN(0),
    TAN(1),
    DARK_EARS(2);

    private static final BeagleVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BeagleVariant::getId)).toArray(BeagleVariant[]::new);
    private final int id;

    BeagleVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BeagleVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
