package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum MastiffVariant {
    FAWN(0),
    APRICOT(1);

    private static final MastiffVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(MastiffVariant::getId)).toArray(MastiffVariant[]::new);
    private final int id;

    MastiffVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static MastiffVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
