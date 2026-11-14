package com.qa.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Main run. Tags are chosen with -Dcucumber.filter.tags (default in the pom:
 * everything except @wip). Failed scenarios are
 * written to target/rerun.txt for FailedScenariosRunner.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.qa.automation.steps", "com.qa.automation.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml",
                "rerun:target/rerun.txt",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /** Runs scenarios in parallel; the thread count comes from the pom (parallel.threads). */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
