package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum MiniSchnauzerVariant {
    PEPPER_SALT(0),
    BLACK_SILVER(1),
    BLACK(2);

    private static final MiniSchnauzerVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(MiniSchnauzerVariant::getId)).toArray(MiniSchnauzerVariant[]::new);
    private final int id;

    MiniSchnauzerVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static MiniSchnauzerVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
