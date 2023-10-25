package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum GreatDaneVariant {
    FAWN(0),
    BLACK(1),
    BLUE(2);

    private static final GreatDaneVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(GreatDaneVariant::getId)).toArray(GreatDaneVariant[]::new);
    private final int id;

    GreatDaneVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GreatDaneVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
