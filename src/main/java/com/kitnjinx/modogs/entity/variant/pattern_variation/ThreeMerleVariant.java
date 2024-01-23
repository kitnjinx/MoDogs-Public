package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum ThreeMerleVariant {
    PATTERN1(0),
    PATTERN2(1),
    PATTERN3(2);

    private static final ThreeMerleVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(ThreeMerleVariant::getId)).toArray(ThreeMerleVariant[]::new);
    private final int id;

    ThreeMerleVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ThreeMerleVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
