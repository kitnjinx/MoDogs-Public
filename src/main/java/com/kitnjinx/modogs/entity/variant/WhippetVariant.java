package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum WhippetVariant {
    WHITE_RED(0),
    WHITE_BLUE(1),
    WHITE_BLACK(2),
    WHITE(3),
    RED(4),
    BLUE(5),
    BLACK(6);

    private static final WhippetVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(WhippetVariant::getId)).toArray(WhippetVariant[]::new);
    private final int id;

    WhippetVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static WhippetVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
