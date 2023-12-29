package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum PitBullVariant {
    BROWN_WHITE(0),
    BLACK_WHITE(1),
    BLUE_WHITE(2),
    BROWN(3),
    BLACK(4),
    BLUE(5),
    WHITE(6);

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
