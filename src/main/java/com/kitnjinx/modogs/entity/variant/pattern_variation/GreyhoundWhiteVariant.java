package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum GreyhoundWhiteVariant {
    WHITE1(0),
    WHITE2(1),
    WHITE3(2);

    private static final GreyhoundWhiteVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(GreyhoundWhiteVariant::getId)).toArray(GreyhoundWhiteVariant[]::new);
    private final int id;

    GreyhoundWhiteVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GreyhoundWhiteVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
