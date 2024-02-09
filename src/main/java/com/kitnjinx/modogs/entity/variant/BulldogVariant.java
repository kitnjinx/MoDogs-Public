package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BulldogVariant {
    RED(0),
    FAWN(1);

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
