package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum TwoWhiteVariant {
    WHITE1(0),
    WHITE2(1);

    private static final TwoWhiteVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(TwoWhiteVariant::getId)).toArray(TwoWhiteVariant[]::new);
    private final int id;

    TwoWhiteVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static TwoWhiteVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
