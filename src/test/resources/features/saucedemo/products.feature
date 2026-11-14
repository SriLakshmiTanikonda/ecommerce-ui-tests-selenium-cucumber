@saucedemo @products
Feature: Product catalog
  Shoppers see every product with the right price, can sort the list,
  and can open any product for more detail.

  Background:
    Given I am logged in to the store as the "standard" user

  @smoke
  Scenario: Catalog shows every product at the right price
    Then I should see 6 products
    And the catalog should list these products:
      | name                              | price  |
      | Sauce Labs Backpack               | $29.99 |
      | Sauce Labs Bike Light             | $9.99  |
      | Sauce Labs Bolt T-Shirt           | $15.99 |
      | Sauce Labs Fleece Jacket          | $49.99 |
      | Sauce Labs Onesie                 | $7.99  |
      | Test.allTheThings() T-Shirt (Red) | $15.99 |

  @regression
  Scenario Outline: Sort products by <option>
    When I sort products by "<option>"
    Then products should be sorted by <field> in <direction> order

    Examples:
      | option              | field | direction  |
      | Name (A to Z)       | name  | ascending  |
      | Name (Z to A)       | name  | descending |
      | Price (low to high) | price | ascending  |
      | Price (high to low) | price | descending |

  @regression
  Scenario Outline: Details page matches the catalog for <product>
    When I open the details of "<product>"
    Then the details page should show "<product>" at the same price as the list
    When I go back to the products page
    Then I should be on the "/inventory.html" page

    Examples:
      | product                  |
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
      | Sauce Labs Onesie        |

  @regression
  Scenario: Every product has its own image
    Then every product should have its own image
