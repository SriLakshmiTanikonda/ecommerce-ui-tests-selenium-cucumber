@internet @forms
Feature: Form controls
  Checkboxes and dropdowns keep the value the user chooses.

  @regression
  Scenario: Checkboxes start in their default state and can be toggled
    Given I am on the checkboxes page
    Then checkbox 1 should be unchecked
    And checkbox 2 should be checked
    When I check checkbox 1
    And I uncheck checkbox 2
    Then checkbox 1 should be checked
    And checkbox 2 should be unchecked

  @regression
  Scenario: Dropdown lists its options
    Given I am on the dropdown page
    Then the dropdown should offer these options:
      | Please select an option |
      | Option 1                |
      | Option 2                |

  @smoke
  Scenario Outline: Dropdown keeps the selected option
    Given I am on the dropdown page
    When I select "<option>" from the dropdown
    Then "<option>" should be selected

    Examples:
      | option   |
      | Option 1 |
      | Option 2 |
