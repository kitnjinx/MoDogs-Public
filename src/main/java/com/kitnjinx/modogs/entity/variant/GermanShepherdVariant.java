package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum GermanShepherdVariant {
    STANDARD(0),
    BROWN_POINTS(1),
    BLACK(2),
    WHITE(3);

    private static final GermanShepherdVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(GermanShepherdVariant::getId)).toArray(GermanShepherdVariant[]::new);
    private final int id;

    GermanShepherdVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GermanShepherdVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
