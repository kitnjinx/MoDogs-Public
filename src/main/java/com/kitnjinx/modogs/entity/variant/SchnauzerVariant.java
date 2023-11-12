package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum SchnauzerVariant {
    BLACK(0),
    PEPPER_SALT(1);

    private static final SchnauzerVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(SchnauzerVariant::getId)).toArray(SchnauzerVariant[]::new);
    private final int id;

    SchnauzerVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static SchnauzerVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
