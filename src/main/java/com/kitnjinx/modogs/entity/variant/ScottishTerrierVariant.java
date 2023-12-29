package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum ScottishTerrierVariant {
    BLACK(0),
    WHEATEN(1);

    private static final ScottishTerrierVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(ScottishTerrierVariant::getId)).toArray(ScottishTerrierVariant[]::new);
    private final int id;

    ScottishTerrierVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ScottishTerrierVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
