package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum ItalianGreyhoundVariant {
    BLUE(0),
    FAWN(1),
    BLACK(2),
    WHITE_BLUE(3),
    WHITE_FAWN(4),
    WHITE_BLACK(5);

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
