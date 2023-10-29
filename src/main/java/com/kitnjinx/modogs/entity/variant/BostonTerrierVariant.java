package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BostonTerrierVariant {
    BLACK(0),
    SEAL(1);

    private static final BostonTerrierVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BostonTerrierVariant::getId)).toArray(BostonTerrierVariant[]::new);
    private final int id;

    BostonTerrierVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BostonTerrierVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
