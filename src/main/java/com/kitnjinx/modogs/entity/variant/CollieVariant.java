package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum CollieVariant {
    SABLE(0),
    BLACK_TAN(1);

    private static final CollieVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(CollieVariant::getId)).toArray(CollieVariant[]::new);
    private final int id;

    CollieVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CollieVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
