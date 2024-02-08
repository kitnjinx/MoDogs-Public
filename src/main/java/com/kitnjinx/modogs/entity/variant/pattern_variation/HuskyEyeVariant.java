package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum HuskyEyeVariant {
    BLUE1(0),
    BLUE2(1),
    BROWN1(2),
    BROWN2(3),
    HETERO1(4),
    HETERO2(5);

    private static final HuskyEyeVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(HuskyEyeVariant::getId)).toArray(HuskyEyeVariant[]::new);
    private final int id;

    HuskyEyeVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static HuskyEyeVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
