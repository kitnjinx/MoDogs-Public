package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum LabRetrieverVariant {
    BLACK(0),
    CHOCOLATE(1),
    YELLOW(2);

    private static final LabRetrieverVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(LabRetrieverVariant::getId)).toArray(LabRetrieverVariant[]::new);
    private final int id;

    LabRetrieverVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static LabRetrieverVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
