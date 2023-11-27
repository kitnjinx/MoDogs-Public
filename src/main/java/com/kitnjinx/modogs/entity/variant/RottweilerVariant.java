package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum RottweilerVariant {
    TAN(0),
    RUST(1),
    MAHOGANY(2);

    private static final RottweilerVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(RottweilerVariant::getId)).toArray(RottweilerVariant[]::new);
    private final int id;

    RottweilerVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static RottweilerVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
