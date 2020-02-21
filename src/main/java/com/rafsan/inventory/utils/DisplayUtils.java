package com.rafsan.inventory.utils;

import java.text.DecimalFormat;

public final class DisplayUtils {

    private final static DecimalFormat decimalFormat = new DecimalFormat("###");

    public static String getFormattedValue(Number value) {
        return decimalFormat.format(value);
    }
}
