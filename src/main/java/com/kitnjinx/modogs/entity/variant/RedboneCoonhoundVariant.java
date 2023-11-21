package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum RedboneCoonhoundVariant {
    BROWN(0),
    RED(1),
    DARK_RED(2);

    private static final RedboneCoonhoundVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(RedboneCoonhoundVariant::getId)).toArray(RedboneCoonhoundVariant[]::new);
    private final int id;

    RedboneCoonhoundVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static RedboneCoonhoundVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
