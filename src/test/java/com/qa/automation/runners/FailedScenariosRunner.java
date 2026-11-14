package com.qa.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Second pass: reruns only the scenarios listed in target/rerun.txt.
 * A scenario that fails, then passes on rerun, is flaky; it is reported
 * as such in Allure (retries are grouped) instead of failing the build.
 * A scenario that fails twice fails the build.
 */
@CucumberOptions(
        features = "@target/rerun.txt",
        glue = {"com.qa.automation.steps", "com.qa.automation.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-rerun.html",
                "json:target/cucumber-reports/cucumber-rerun.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class FailedScenariosRunner extends AbstractTestNGCucumberTests {
}
