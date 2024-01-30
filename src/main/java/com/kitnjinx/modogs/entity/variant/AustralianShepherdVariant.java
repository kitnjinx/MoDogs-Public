package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum AustralianShepherdVariant {
    BLACK(0),
    RED(1);

    private static final AustralianShepherdVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(AustralianShepherdVariant::getId)).toArray(AustralianShepherdVariant[]::new);
    private final int id;

    AustralianShepherdVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static AustralianShepherdVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
