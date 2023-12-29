package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum TreeWalkHoundVariant {
    TRICOLOR(0),
    BLACK(1);

    private static final TreeWalkHoundVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(TreeWalkHoundVariant::getId)).toArray(TreeWalkHoundVariant[]::new);
    private final int id;

    TreeWalkHoundVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static TreeWalkHoundVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
