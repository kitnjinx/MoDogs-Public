package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum ItalianGreyhoundVariant {
    BLUE(0),
    FAWN(1),
    BLACK(2);

    private static final ItalianGreyhoundVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(ItalianGreyhoundVariant::getId)).toArray(ItalianGreyhoundVariant[]::new);
    private final int id;

    ItalianGreyhoundVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ItalianGreyhoundVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
