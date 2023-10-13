package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum DalmatianVariant {
    BLACK1(0),
    BLACK2(1),
    BLACK3(2),
    BROWN(3);

    private static final DalmatianVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(DalmatianVariant::getId)).toArray(DalmatianVariant[]::new);
    private final int id;

    DalmatianVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static DalmatianVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
