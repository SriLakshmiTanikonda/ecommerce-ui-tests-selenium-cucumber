@internet @alerts
Feature: Browser alerts and new windows
  The framework must handle JavaScript pop-ups and links that open new windows.

  @smoke
  Scenario: Accept a simple alert
    Given I am on the JavaScript alerts page
    When I click "Click for JS Alert" and accept the alert
    Then the result should be "You successfully clicked an alert"

  @regression
  Scenario Outline: Answer a confirm dialog
    Given I am on the JavaScript alerts page
    When I click "Click for JS Confirm" and <action> the alert
    Then the result should be "<result>"

    Examples:
      | action  | result              |
      | accept  | You clicked: Ok     |
      | dismiss | You clicked: Cancel |

  @regression
  Scenario: Type an answer into a prompt
    Given I am on the JavaScript alerts page
    When I click "Click for JS Prompt" and type "Hello from Selenium"
    Then the result should be "You entered: Hello from Selenium"

  @regression @windows
  Scenario: Work in a new window and come back
    Given I am on the multiple windows page
    When I open the new window
    Then 2 windows should be open
    And the new window heading should be "New Window"
    When I close it and return to the original window
    Then the page heading should be "Opening a new window"
