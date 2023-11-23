package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum AmericanFoxhoundVariant {
    LIGHT(0),
    MEDIUM(1),
    DARK(2);

    private static final AmericanFoxhoundVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(AmericanFoxhoundVariant::getId)).toArray(AmericanFoxhoundVariant[]::new);
    private final int id;

    AmericanFoxhoundVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static AmericanFoxhoundVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
