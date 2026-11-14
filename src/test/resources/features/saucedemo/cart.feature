@saucedemo @cart
Feature: Shopping cart
  The cart always matches what the shopper chose, wherever they add or remove items.

  Background:
    Given I am logged in to the store as the "standard" user

  @smoke
  Scenario: Add several products from the catalog
    When I add these products to the cart:
      | Sauce Labs Backpack      |
      | Sauce Labs Bike Light    |
      | Sauce Labs Fleece Jacket |
    Then the cart badge should show 3
    And the button for "Sauce Labs Backpack" should say "Remove"
    When I open the cart
    Then the cart should contain exactly the products I added

  @regression
  Scenario: Remove a product on the catalog page
    When I add these products to the cart:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And I remove "Sauce Labs Backpack" from the products page
    Then the cart badge should show 1
    And the button for "Sauce Labs Backpack" should say "Add to cart"

  @regression
  Scenario: Remove a product on the cart page
    When I add these products to the cart:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And I open the cart
    And I remove "Sauce Labs Backpack" from the cart
    Then the cart should contain:
      | Sauce Labs Bike Light |
    And the cart badge should show 1

  @regression
  Scenario: Add a product from its details page
    When I open the details of "Sauce Labs Onesie"
    And I add the product to the cart from its details page
    Then the details page button should say "Remove"
    And the cart badge should show 1

  @regression
  Scenario: Cart is kept while browsing
    When I add "Sauce Labs Bolt T-Shirt" to the cart
    And I open the details of "Sauce Labs Backpack"
    And I go back to the products page
    And I open the cart
    Then the cart should contain:
      | Sauce Labs Bolt T-Shirt |
    When I continue shopping
    Then I should be on the "/inventory.html" page

  @regression
  Scenario: Reset App State empties the cart
    When I add these products to the cart:
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And I choose "Reset App State" from the menu
    Then the cart badge should be hidden

  @regression
  Scenario: Empty cart
    When I open the cart
    Then the cart should be empty
    And the cart badge should be hidden
