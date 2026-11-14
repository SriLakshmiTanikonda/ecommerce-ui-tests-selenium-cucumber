@internet @tables
Feature: Sortable data table
  Data shown in a table must be complete and sort correctly.

  Background:
    Given I am on the tables page

  @regression
  Scenario: Table shows every customer with the right amount due
    Then the table should contain these rows:
      | Last Name | First Name | Email                 | Due     |
      | Smith     | John       | jsmith@gmail.com      | $50.00  |
      | Bach      | Frank      | fbach@yahoo.com       | $51.00  |
      | Doe       | Jason      | jdoe@hotmail.com      | $100.00 |
      | Conway    | Tim        | tconway@earthlink.net | $50.00  |

  @smoke
  Scenario: Sorting by last name puts customers in alphabetical order
    When I sort the table by "Last Name"
    Then the "Last Name" column should be in ascending order
