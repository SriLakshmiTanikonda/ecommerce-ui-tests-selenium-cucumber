package com.qa.automation.models;

/**
 * A row from testdata/customers.xlsx. `expectedError` is empty for valid
 * customers and holds the exact message the checkout form should show otherwise.
 */
public record Customer(String key, String firstName, String lastName, String postalCode, String expectedError) {

    public boolean isValid() {
        return expectedError == null || expectedError.isBlank();
    }
}
