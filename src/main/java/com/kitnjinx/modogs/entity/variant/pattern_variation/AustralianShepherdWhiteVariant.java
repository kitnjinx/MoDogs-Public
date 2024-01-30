package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum AustralianShepherdWhiteVariant {
    WHITE1(0),
    WHITE2(1),
    WHITE3(2),
    WHITE4(3);

    private static final AustralianShepherdWhiteVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(AustralianShepherdWhiteVariant::getId)).toArray(AustralianShepherdWhiteVariant[]::new);
    private final int id;

    AustralianShepherdWhiteVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static AustralianShepherdWhiteVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
