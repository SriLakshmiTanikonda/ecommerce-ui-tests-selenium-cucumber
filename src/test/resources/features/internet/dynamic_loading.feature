@internet @dynamic
Feature: Content that loads after a delay
  Pages often show content only after a background request finishes.
  These scenarios prove the framework waits for it instead of sleeping.

  @smoke
  Scenario Outline: Result appears once loading finishes
    Given I open dynamic loading example <example>
    When I click Start
    Then the text "Hello World!" should appear

    Examples:
      | example | note                                 |
      | 1       | element is hidden, then shown        |
      | 2       | element is added to the page later   |
