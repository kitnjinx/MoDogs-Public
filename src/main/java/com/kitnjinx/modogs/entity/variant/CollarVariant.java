package com.kitnjinx.modogs.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum CollarVariant {
    NONE(0),
    WHITE(1),
    LIGHT_GRAY(2),
    GRAY(3),
    BLACK(4),
    BROWN(5),
    RED(6),
    ORANGE(7),
    YELLOW(8),
    LIME(9),
    GREEN(10),
    CYAN(11),
    LIGHT_BLUE(12),
    BLUE(13),
    PURPLE(14),
    MAGENTA(15),
    PINK(16);

    private static final CollarVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(CollarVariant::getId)).toArray(CollarVariant[]::new);
    private final int id;

    CollarVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CollarVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
