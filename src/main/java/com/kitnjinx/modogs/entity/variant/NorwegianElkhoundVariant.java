package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum NorwegianElkhoundVariant {
    LIGHT(0),
    MEDIUM(1),
    DARK(2);

    private static final NorwegianElkhoundVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(NorwegianElkhoundVariant::getId)).toArray(NorwegianElkhoundVariant[]::new);
    private final int id;

    NorwegianElkhoundVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static NorwegianElkhoundVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
