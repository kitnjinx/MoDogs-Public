package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum WhippetVariant {
    RED(0),
    BLUE(1),
    BLACK(2);

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
