package com.qa.automation.steps.common;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.automation.context.TestContext;
import io.cucumber.java.en.Then;
import java.net.URI;

/** Steps that apply to any page in any application. */
public class CommonSteps {

    private final TestContext context;

    public CommonSteps(TestContext context) {
        this.context = context;
    }

    @Then("I should be on the {string} page")
    public void iShouldBeOnThePage(String expectedPath) {
        String path = URI.create(context.driver().getCurrentUrl()).getPath();
        assertThat(path).isEqualTo(expectedPath);
    }
}
