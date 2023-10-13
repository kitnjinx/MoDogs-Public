package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum PembrokeCorgiVariant {
    RED(0),
    BLACK_TAN(1),
    FAWN(2),
    SABLE(3);

    private static final PembrokeCorgiVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(PembrokeCorgiVariant::getId)).toArray(PembrokeCorgiVariant[]::new);
    private final int id;

    PembrokeCorgiVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PembrokeCorgiVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
