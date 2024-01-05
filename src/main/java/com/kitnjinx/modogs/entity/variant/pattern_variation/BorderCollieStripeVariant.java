package com.kitnjinx.modogs.entity.variant.pattern_variation;

import java.util.Arrays;
import java.util.Comparator;

public enum BorderCollieStripeVariant {
    STRIPE1(1),
    STRIPE2(2),
    STRIPE3(3),
    STRIPE4(4);

    private static final BorderCollieStripeVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(BorderCollieStripeVariant::getId)).toArray(BorderCollieStripeVariant[]::new);
    private final int id;

    BorderCollieStripeVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static BorderCollieStripeVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
