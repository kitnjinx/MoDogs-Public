package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum GoldenRetrieverVariant {
    LIGHT(0),
    MEDIUM(1),
    DARK(2);

    private static final GoldenRetrieverVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(GoldenRetrieverVariant::getId)).toArray(GoldenRetrieverVariant[]::new);
    private final int id;

    GoldenRetrieverVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GoldenRetrieverVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
