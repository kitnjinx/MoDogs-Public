package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum MudiVariant {
    BLACK(0),
    BROWN(1),
    WHITE(2);

    private static final MudiVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(MudiVariant::getId)).toArray(MudiVariant[]::new);
    private final int id;

    MudiVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static MudiVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
