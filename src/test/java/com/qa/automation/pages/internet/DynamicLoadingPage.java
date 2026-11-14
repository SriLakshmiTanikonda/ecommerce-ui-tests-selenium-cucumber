package com.qa.automation.pages.internet;

import com.qa.automation.config.ConfigReader;
import com.qa.automation.pages.BasePage;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Example 1: the result is in the page but hidden until loading finishes.
 * Example 2: the result is added to the page only after loading finishes.
 * Both need an explicit wait; a fixed sleep would be either too slow or flaky.
 */
public class DynamicLoadingPage extends BasePage {

    private static final By START_BUTTON = By.cssSelector("#start button");
    private static final By LOADING_BAR = By.id("loading");
    private static final By RESULT = By.cssSelector("#finish h4");

    public DynamicLoadingPage(WebDriver driver) {
        super(driver, ConfigReader.internetUrl());
    }

    public void open(int example) {
        open("/dynamic_loading/" + example);
    }

    public void start() {
        click(START_BUTTON);
    }

    /** Waits for the loading bar to disappear, then for the result to be visible. */
    public String waitForResult() {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        longWait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_BAR));
        return longWait.until(ExpectedConditions.visibilityOfElementLocated(RESULT)).getText().trim();
    }
}
