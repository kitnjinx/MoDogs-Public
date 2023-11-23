package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum AiredaleTerrierVariant {
    LIGHT(0),
    MEDIUM(1),
    DARK(2);

    private static final AiredaleTerrierVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(AiredaleTerrierVariant::getId)).toArray(AiredaleTerrierVariant[]::new);
    private final int id;

    AiredaleTerrierVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static AiredaleTerrierVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
