package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum BorderCollieMerleVariant {
    PATTERN1(0),
    PATTERN2(1),
    PATTERN3(2);

    private static final BorderCollieMerleVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BorderCollieMerleVariant::getId)).toArray(BorderCollieMerleVariant[]::new);
    private final int id;

    BorderCollieMerleVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BorderCollieMerleVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
