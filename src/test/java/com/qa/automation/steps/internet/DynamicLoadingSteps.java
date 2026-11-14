package com.qa.automation.steps.internet;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.automation.context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DynamicLoadingSteps {

    private final TestContext context;

    public DynamicLoadingSteps(TestContext context) {
        this.context = context;
    }

    @Given("I open dynamic loading example {int}")
    public void iOpenExample(int example) {
        context.dynamicLoadingPage().open(example);
    }

    @When("I click Start")
    public void iClickStart() {
        context.dynamicLoadingPage().start();
    }

    @Then("the text {string} should appear")
    public void theTextShouldAppear(String expected) {
        assertThat(context.dynamicLoadingPage().waitForResult()).isEqualTo(expected);
    }
}
