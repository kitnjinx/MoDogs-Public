package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum CardiganCorgiVariant {
    RED(0),
    BLACK(1),
    SABLE(2),
    BLUE_MERLE(3);

    private static final CardiganCorgiVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(CardiganCorgiVariant::getId)).toArray(CardiganCorgiVariant[]::new);
    private final int id;

    CardiganCorgiVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CardiganCorgiVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
