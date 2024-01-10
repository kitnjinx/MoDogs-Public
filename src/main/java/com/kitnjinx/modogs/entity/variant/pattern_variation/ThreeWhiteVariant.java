package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum ThreeWhiteVariant {
    WHITE1(0),
    WHITE2(1),
    WHITE3(2);

    private static final ThreeWhiteVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(ThreeWhiteVariant::getId)).toArray(ThreeWhiteVariant[]::new);
    private final int id;

    ThreeWhiteVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ThreeWhiteVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
