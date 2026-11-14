package com.qa.automation.steps.internet;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.automation.context.TestContext;
import com.qa.automation.pages.internet.AlertsAndWindowsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Alert;

public class AlertsAndWindowsSteps {

    private final TestContext context;

    public AlertsAndWindowsSteps(TestContext context) {
        this.context = context;
    }

    private AlertsAndWindowsPage page() {
        return context.alertsAndWindowsPage();
    }

    @Given("I am on the JavaScript alerts page")
    public void iAmOnTheAlertsPage() {
        page().openAlerts();
    }

    @Given("I am on the multiple windows page")
    public void iAmOnTheWindowsPage() {
        page().openWindows();
    }

    @When("I click {string} and {word} the alert")
    public void iClickAndHandleTheAlert(String button, String action) {
        page().clickButton(button);
        Alert alert = page().waitForAlert();
        switch (action) {
            case "accept" -> alert.accept();
            case "dismiss" -> alert.dismiss();
            default -> throw new IllegalArgumentException("Unknown alert action: " + action);
        }
    }

    @When("I click {string} and type {string}")
    public void iClickAndType(String button, String text) {
        page().clickButton(button);
        Alert prompt = page().waitForAlert();
        prompt.sendKeys(text);
        prompt.accept();
    }

    @Then("the result should be {string}")
    public void theResultShouldBe(String expected) {
        assertThat(page().result()).isEqualTo(expected);
    }

    @When("I open the new window")
    public void iOpenTheNewWindow() {
        page().openNewWindow();
    }

    @Then("{int} windows should be open")
    public void windowsShouldBeOpen(int count) {
        assertThat(page().windowCount()).isEqualTo(count);
    }

    @Then("the new window heading should be {string}")
    public void theNewWindowHeadingShouldBe(String expected) {
        page().switchToNewWindow();
        assertThat(page().heading()).isEqualTo(expected);
    }

    @When("I close it and return to the original window")
    public void iCloseItAndReturn() {
        page().closeAndReturnToOriginal();
    }

    @Then("the page heading should be {string}")
    public void thePageHeadingShouldBe(String expected) {
        assertThat(page().heading()).isEqualTo(expected);
    }
}
