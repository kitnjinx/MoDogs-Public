package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum CockerSpanielVariant {
    RED(0),
    BROWN(1),
    BUFF(2),
    BLACK(3),
    SILVER(4);

    private static final CockerSpanielVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(CockerSpanielVariant::getId)).toArray(CockerSpanielVariant[]::new);
    private final int id;

    CockerSpanielVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CockerSpanielVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
