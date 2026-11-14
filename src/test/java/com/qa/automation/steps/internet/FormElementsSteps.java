package com.qa.automation.steps.internet;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.automation.context.TestContext;
import com.qa.automation.pages.internet.FormElementsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;

public class FormElementsSteps {

    private final TestContext context;

    public FormElementsSteps(TestContext context) {
        this.context = context;
    }

    private FormElementsPage page() {
        return context.formElementsPage();
    }

    @Given("I am on the checkboxes page")
    public void iAmOnTheCheckboxesPage() {
        page().openCheckboxes();
    }

    @Given("I am on the dropdown page")
    public void iAmOnTheDropdownPage() {
        page().openDropdown();
    }

    @When("I check checkbox {int}")
    public void iCheckCheckbox(int number) {
        page().setChecked(number, true);
    }

    @When("I uncheck checkbox {int}")
    public void iUncheckCheckbox(int number) {
        page().setChecked(number, false);
    }

    @Then("checkbox {int} should be checked")
    public void checkboxShouldBeChecked(int number) {
        assertThat(page().isChecked(number)).as("checkbox %d checked", number).isTrue();
    }

    @Then("checkbox {int} should be unchecked")
    public void checkboxShouldBeUnchecked(int number) {
        assertThat(page().isChecked(number)).as("checkbox %d checked", number).isFalse();
    }

    @When("I select {string} from the dropdown")
    public void iSelectFromTheDropdown(String option) {
        page().selectOption(option);
    }

    @Then("{string} should be selected")
    public void shouldBeSelected(String option) {
        assertThat(page().selectedOption()).isEqualTo(option);
    }

    @Then("the dropdown should offer these options:")
    public void theDropdownShouldOffer(List<String> expected) {
        assertThat(page().dropdownOptions()).containsExactlyElementsOf(expected);
    }
}
