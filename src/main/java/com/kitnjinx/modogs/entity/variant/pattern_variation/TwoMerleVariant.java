package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum TwoMerleVariant {
    PATTERN1(0),
    PATTERN2(1);

    private static final TwoMerleVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(TwoMerleVariant::getId)).toArray(TwoMerleVariant[]::new);
    private final int id;

    TwoMerleVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static TwoMerleVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
