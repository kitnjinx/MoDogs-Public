package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BoxerVariant {
    MEDIUM(0),
    LIGHT(1),
    DARK(2),
    BLACK(3);

    private static final BoxerVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BoxerVariant::getId)).toArray(BoxerVariant[]::new);
    private final int id;

    BoxerVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BoxerVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
