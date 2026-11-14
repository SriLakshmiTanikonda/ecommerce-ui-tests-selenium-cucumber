@saucedemo @store-login
Feature: Store login and logout
  As a shopper
  I want to sign in to the store
  So that I can browse products and place orders

  Background:
    Given I am on the store login page

  @smoke
  Scenario: Standard customer logs in
    When I log in to the store as the "standard" user
    Then I should see the products page
    And I should be on the "/inventory.html" page

  @regression
  Scenario Outline: Login is refused with a clear reason (<case>)
    When I log in to the store as the "<user>" user
    Then I should see the login error "<error>"
    And I should be on the "/" page

    Examples:
      | case           | user           | error                                                                     |
      | locked account | locked         | Epic sadface: Sorry, this user has been locked out.                       |
      | wrong password | wrong_password | Epic sadface: Username and password do not match any user in this service |

  @regression
  Scenario Outline: Required fields are checked before submitting (<case>)
    When I log in to the store with username "<username>" and password "<password>"
    Then I should see the login error "<error>"

    Examples:
      | case             | username      | password     | error                              |
      | no username      |               | secret_sauce | Epic sadface: Username is required |
      | no password      | standard_user |              | Epic sadface: Password is required |
      | both fields empty |              |              | Epic sadface: Username is required |

  @regression
  Scenario: Error message can be dismissed
    When I log in to the store as the "locked" user
    And I close the error message
    Then no login error should be shown

  @regression @performance
  Scenario: Slow account still logs in within an acceptable time
    When I log in to the store as the "glitch" user
    Then the products page should load within 10 seconds

  @smoke
  Scenario: Logging out ends the session
    Given I log in to the store as the "standard" user
    When I choose "Logout" from the menu
    Then I should be back on the store login page
    When I try to open the products page directly
    Then I should see the login error "Epic sadface: You can only access '/inventory.html' when you are logged in."
