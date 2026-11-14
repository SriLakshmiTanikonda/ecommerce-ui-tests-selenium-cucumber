package com.qa.automation.steps.saucedemo;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.automation.context.TestContext;
import com.qa.automation.models.User;
import com.qa.automation.pages.saucedemo.SauceLoginPage;
import com.qa.automation.utils.JsonDataReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Duration;
import java.time.Instant;

public class SauceLoginSteps {

    private final TestContext context;

    public SauceLoginSteps(TestContext context) {
        this.context = context;
    }

    private SauceLoginPage loginPage() {
        return context.sauceLoginPage();
    }

    @Given("I am on the store login page")
    public void iAmOnTheStoreLoginPage() {
        loginPage().open();
    }

    /** Shortcut used as a Background step by features that start logged in. */
    @Given("I am logged in to the store as the {string} user")
    public void iAmLoggedInAs(String userKey) {
        loginPage().open();
        loginPage().login(JsonDataReader.user(userKey));
        assertThat(context.inventoryPage().title()).isEqualTo("Products");
    }

    @When("I log in to the store as the {string} user")
    public void iLogInAs(String userKey) {
        User user = JsonDataReader.user(userKey);
        Instant started = Instant.now();
        loginPage().login(user);
        context.put("loginStarted", started);
    }

    @When("I log in to the store with username {string} and password {string}")
    public void iLogInWith(String username, String password) {
        loginPage().login(username, password);
    }

    @Then("I should see the products page")
    public void iShouldSeeTheProductsPage() {
        assertThat(context.inventoryPage().title()).isEqualTo("Products");
    }

    @Then("the products page should load within {int} seconds")
    public void productsPageWithin(int seconds) {
        // Waits (up to the explicit timeout) for the page, then checks the elapsed time.
        assertThat(context.inventoryPage().title()).isEqualTo("Products");
        Duration elapsed = Duration.between(context.get("loginStarted", Instant.class), Instant.now());
        assertThat(elapsed).isLessThan(Duration.ofSeconds(seconds));
    }

    @Then("I should see the login error {string}")
    public void iShouldSeeTheLoginError(String expected) {
        assertThat(loginPage().error()).isEqualTo(expected);
    }

    @When("I close the error message")
    public void iCloseTheErrorMessage() {
        loginPage().dismissError();
    }

    @Then("no login error should be shown")
    public void noLoginError() {
        assertThat(loginPage().isErrorShown()).isFalse();
    }

    @When("I choose {string} from the menu")
    public void iChooseFromTheMenu(String item) {
        context.inventoryPage().openMenuItem(item);
    }

    @Then("I should be back on the store login page")
    public void iShouldBeBackOnTheLoginPage() {
        assertThat(loginPage().isShown()).isTrue();
    }

    @When("I try to open the products page directly")
    public void iTryToOpenTheProductsPageDirectly() {
        context.inventoryPage().open();
    }
}
