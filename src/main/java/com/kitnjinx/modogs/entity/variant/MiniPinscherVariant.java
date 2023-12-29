package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum MiniPinscherVariant {
    BLACK_TAN(0),
    RED(1),
    CHOCOLATE_TAN(2);

    private static final MiniPinscherVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(MiniPinscherVariant::getId)).toArray(MiniPinscherVariant[]::new);
    private final int id;

    MiniPinscherVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static MiniPinscherVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
