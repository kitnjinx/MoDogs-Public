package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum BerneseMountainDogVariant {
    RUST(0),
    TAN(1);

    private static final BerneseMountainDogVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BerneseMountainDogVariant::getId)).toArray(BerneseMountainDogVariant[]::new);
    private final int id;

    BerneseMountainDogVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BerneseMountainDogVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
