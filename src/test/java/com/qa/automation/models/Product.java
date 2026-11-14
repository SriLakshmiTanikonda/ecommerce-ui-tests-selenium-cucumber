package com.qa.automation.models;

import java.math.BigDecimal;

/** A product as shown in the store: name and price. BigDecimal keeps money exact. */
public record Product(String name, BigDecimal price) {

    /** Parses labels like "$29.99". */
    public static BigDecimal money(String label) {
        String digits = label.replaceAll("[^0-9.]", "");
        if (digits.isEmpty()) {
            throw new IllegalArgumentException("No amount in: " + label);
        }
        return new BigDecimal(digits);
    }
}
