package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum ShetlandSheepdogVariant {
    SABLE (0),
    BLACK (1),
    BLACK_TAN (2);

    private static final ShetlandSheepdogVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(ShetlandSheepdogVariant::getId)).toArray(ShetlandSheepdogVariant[]::new);
    private final int id;

    ShetlandSheepdogVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ShetlandSheepdogVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
