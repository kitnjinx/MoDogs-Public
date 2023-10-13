package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum AlaskanMalamuteVariant {
    GRAY(0),
    SABLE(1),
    SEAL(2),
    BLACK(3),
    RED(4),
    SILVER(5);

    private static final AlaskanMalamuteVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(AlaskanMalamuteVariant::getId)).toArray(AlaskanMalamuteVariant[]::new);
    private final int id;

    AlaskanMalamuteVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static AlaskanMalamuteVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
