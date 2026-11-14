@saucedemo @checkout
Feature: Checkout
  Shoppers can buy what is in their cart, with correct totals,
  and are stopped with a clear message when details are missing.
  Customer details are read from testdata/customers.xlsx.

  Background:
    Given I am logged in to the store as the "standard" user

  @smoke @e2e
  Scenario: Complete purchase from catalog to confirmation
    When I add these products to the cart:
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
    And I open the cart
    And I check out
    And I enter the details of customer "valid_us"
    Then I should be on the "/checkout-step-two.html" page
    And the order overview should list the products I added
    And the item total should equal the sum of the item prices
    And the tax should be 8% of the item total
    And the order total should equal the item total plus tax
    When I finish the order
    Then I should see the order confirmation "Thank you for your order!"
    And the cart badge should be hidden
    When I go back home
    Then I should be on the "/inventory.html" page

  @regression
  Scenario Outline: Order total for a single item (<product>)
    When I add these products to the cart:
      | <product> |
    And I open the cart
    And I check out
    And I enter the details of customer "valid_us"
    Then the order total should be "<total>"

    # Total = price + 8% tax, rounded to the cent.
    Examples:
      | product                  | total  |
      | Sauce Labs Onesie        | $8.63  |
      | Sauce Labs Backpack      | $32.39 |
      | Sauce Labs Fleece Jacket | $53.99 |

  @regression
  Scenario Outline: Valid customer details are accepted (<customer>)
    When I add these products to the cart:
      | Sauce Labs Bike Light |
    And I open the cart
    And I check out
    And I enter the details of customer "<customer>"
    Then I should be on the "/checkout-step-two.html" page

    Examples:
      | customer        |
      | valid_us        |
      | valid_long_name |
      | valid_zip_plus4 |

  @regression
  Scenario Outline: Missing details are reported (<customer>)
    When I add these products to the cart:
      | Sauce Labs Bike Light |
    And I open the cart
    And I check out
    And I enter the details of customer "<customer>"
    Then I should see the error expected for that customer
    And I should be on the "/checkout-step-one.html" page

    Examples:
      | customer            |
      | missing_first_name  |
      | missing_last_name   |
      | missing_postal_code |
      | all_empty           |

  @regression
  Scenario: Cancelling the overview keeps the cart
    When I add these products to the cart:
      | Sauce Labs Backpack |
    And I open the cart
    And I check out
    And I enter the details of customer "valid_us"
    And I cancel the checkout
    Then I should be on the "/inventory.html" page
    And the cart badge should show 1
