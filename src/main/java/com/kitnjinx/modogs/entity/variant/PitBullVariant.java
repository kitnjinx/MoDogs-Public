package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum PitBullVariant {
    BROWN(0),
    BLACK(1),
    BLUE(2),
    WHITE(3);

    private static final PitBullVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(PitBullVariant::getId)).toArray(PitBullVariant[]::new);
    private final int id;

    PitBullVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PitBullVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
