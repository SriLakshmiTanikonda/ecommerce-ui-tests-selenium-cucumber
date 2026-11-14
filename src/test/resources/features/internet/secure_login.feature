@internet @secure-login
Feature: Login to the secure area
  As a registered user
  I want to log in with my username and password
  So that I can reach pages only members can see

  Background:
    Given I am on the secure area login page

  @smoke
  Scenario: Successful login and logout
    When I log in as "tomsmith" with password "SuperSecretPassword!"
    Then I should see the message "You logged into a secure area!"
    And I should be on the "/secure" page
    When I log out
    Then I should see the message "You logged out of the secure area!"
    And I should be on the "/login" page

  @regression
  Scenario Outline: Invalid credentials are rejected
    When I log in as "<username>" with password "<password>"
    Then I should see the message "<message>"
    And I should be on the "/login" page

    Examples:
      | username  | password             | message                   |
      | wronguser | SuperSecretPassword! | Your username is invalid! |
      | tomsmith  | wrongpassword        | Your password is invalid! |
      |           |                      | Your username is invalid! |
