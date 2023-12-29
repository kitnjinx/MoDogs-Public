package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BulldogVariant {
    RED_WHITE(0),
    FAWN_WHITE(1),
    WHITE(2),
    RED(3),
    FAWN(4);

    private static final BulldogVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BulldogVariant::getId)).toArray(BulldogVariant[]::new);
    private final int id;

    BulldogVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BulldogVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
