package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum IrishSetterVariant {
    LIGHT(0),
    MEDIUM(1),
    DARK(2);

    private static final IrishSetterVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(IrishSetterVariant::getId)).toArray(IrishSetterVariant[]::new);
    private final int id;

    IrishSetterVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static IrishSetterVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
