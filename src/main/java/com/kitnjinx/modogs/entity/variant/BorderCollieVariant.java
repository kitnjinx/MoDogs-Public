package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BorderCollieVariant {
    BLACK (0),
    RED (1),
    LILAC (2),
    BLACK_MERLE (3),
    RED_MERLE (4),
    LILAC_MERLE (5);

    private static final BorderCollieVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BorderCollieVariant::getId)).toArray(BorderCollieVariant[]::new);
    private final int id;

    BorderCollieVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BorderCollieVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
