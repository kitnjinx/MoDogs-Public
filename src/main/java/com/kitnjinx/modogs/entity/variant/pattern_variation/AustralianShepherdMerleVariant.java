package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum AustralianShepherdMerleVariant {
    PATTERN1(0),
    PATTERN2(1);

    private static final AustralianShepherdMerleVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(AustralianShepherdMerleVariant::getId)).toArray(AustralianShepherdMerleVariant[]::new);
    private final int id;

    AustralianShepherdMerleVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static AustralianShepherdMerleVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
