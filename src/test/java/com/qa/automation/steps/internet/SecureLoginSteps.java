package com.qa.automation.steps.internet;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.automation.context.TestContext;
import com.qa.automation.pages.internet.SecureLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SecureLoginSteps {

    private final TestContext context;

    public SecureLoginSteps(TestContext context) {
        this.context = context;
    }

    private SecureLoginPage loginPage() {
        return context.secureLoginPage();
    }

    @Given("I am on the secure area login page")
    public void iAmOnTheLoginPage() {
        loginPage().open();
    }

    @When("I log in as {string} with password {string}")
    public void iLogInAs(String username, String password) {
        loginPage().loginAs(username, password);
    }

    @When("I log out")
    public void iLogOut() {
        loginPage().logout();
    }

    @Then("I should see the message {string}")
    public void iShouldSeeTheMessage(String expected) {
        assertThat(loginPage().flashMessage()).isEqualTo(expected);
    }
}
