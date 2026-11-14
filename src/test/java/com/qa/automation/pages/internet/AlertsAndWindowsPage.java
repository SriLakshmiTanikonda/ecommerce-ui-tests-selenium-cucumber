package com.qa.automation.pages.internet;

import com.qa.automation.config.ConfigReader;
import com.qa.automation.pages.BasePage;

import java.util.Set;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** JavaScript alerts (/javascript_alerts) and multiple windows (/windows). */
public class AlertsAndWindowsPage extends BasePage {

    private static final By RESULT = By.id("result");
    private static final By OPEN_WINDOW_LINK = By.linkText("Click Here");
    private static final By HEADING = By.tagName("h3");

    private String originalWindow;

    public AlertsAndWindowsPage(WebDriver driver) {
        super(driver, ConfigReader.internetUrl());
    }

    public void openAlerts() {
        open("/javascript_alerts");
    }

    public void openWindows() {
        open("/windows");
    }

    /** Clicks one of the three buttons by its label, e.g. "Click for JS Confirm". */
    public void clickButton(String label) {
        click(By.xpath("//button[normalize-space()='" + label + "']"));
    }

    public Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    public String result() {
        return text(RESULT);
    }

    public void openNewWindow() {
        originalWindow = driver.getWindowHandle();
        click(OPEN_WINDOW_LINK);
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
    }

    public void switchToNewWindow() {
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                return;
            }
        }
        throw new IllegalStateException("No new window was opened");
    }

    public void closeAndReturnToOriginal() {
        driver.close();
        driver.switchTo().window(originalWindow);
    }

    public String heading() {
        return text(HEADING);
    }

    public int windowCount() {
        return driver.getWindowHandles().size();
    }
}
