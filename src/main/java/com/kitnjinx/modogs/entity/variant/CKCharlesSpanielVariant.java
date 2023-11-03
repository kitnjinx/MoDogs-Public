package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum CKCharlesSpanielVariant {
    BLENHEIM(0),
    TRICOLOR(1),
    RUBY(2),
    BLACK_TAN(3);

    private static final CKCharlesSpanielVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(CKCharlesSpanielVariant::getId)).toArray(CKCharlesSpanielVariant[]::new);
    private final int id;

    CKCharlesSpanielVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CKCharlesSpanielVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
