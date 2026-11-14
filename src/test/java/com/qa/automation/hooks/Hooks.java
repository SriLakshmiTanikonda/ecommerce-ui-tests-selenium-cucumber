package com.qa.automation.hooks;

import com.qa.automation.config.ConfigReader;
import com.qa.automation.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);
    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before(order = 0)
    public void startBrowser(Scenario scenario) {
        // Every log line from this thread is tagged with the scenario name (see log4j2.xml).
        ThreadContext.put("scenario", scenario.getName());
        LOG.info("START scenario: {} {}", scenario.getName(), scenario.getSourceTagNames());
        context.startBrowser();
    }

    /** With -Dscreenshot.every.step=true, every step gets a screenshot in the report (useful for demos). */
    @AfterStep
    public void screenshotEachStep(Scenario scenario) {
        if (ConfigReader.getBoolean("screenshot.every.step") && context.hasBrowser()) {
            attachScreenshot(scenario, "step");
        }
    }

    /** Attaches a screenshot and the page URL to the report when a scenario fails, then closes the browser. */
    @After(order = 0)
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && context.hasBrowser()) {
                attachScreenshot(scenario, "failure");
                scenario.attach(context.driver().getCurrentUrl(), "text/plain", "URL at failure");
            }
            LOG.info("END scenario: {} -> {}", scenario.getName(), scenario.getStatus());
        } finally {
            context.stopBrowser();
            ThreadContext.clearAll();
        }
    }

    private void attachScreenshot(Scenario scenario, String label) {
        try {
            byte[] screenshot = ((TakesScreenshot) context.driver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", label);
        } catch (RuntimeException e) {
            LOG.warn("Could not take a screenshot: {}", e.getMessage());
        }
    }
}
