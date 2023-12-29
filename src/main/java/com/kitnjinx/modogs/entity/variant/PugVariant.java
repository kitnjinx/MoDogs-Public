package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum PugVariant {
    FAWN(0),
    BLACK(1);

    private static final PugVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(PugVariant::getId)).toArray(PugVariant[]::new);
    private final int id;

    PugVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PugVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
