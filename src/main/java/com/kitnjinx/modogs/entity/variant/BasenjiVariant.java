package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BasenjiVariant {
    RED(0),
    BLACK(1),
    TRICOLOR(2);

    private static final BasenjiVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BasenjiVariant::getId)).toArray(BasenjiVariant[]::new);
    private final int id;

    BasenjiVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BasenjiVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
