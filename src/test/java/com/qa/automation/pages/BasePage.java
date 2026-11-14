package com.qa.automation.pages;

import com.qa.automation.config.ConfigReader;
import com.qa.automation.utils.JsUtils;
import java.time.Duration;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Every page goes through these helpers, so each interaction waits for
 * the element to be ready. That removes the main cause of flaky Selenium
 * tests: acting on an element before it is visible or clickable.
 */
public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private final String baseUrl;

    protected BasePage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds")));
    }

    protected void open(String path) {
        log.debug("Opening {}{}", baseUrl, path);
        driver.get(baseUrl + path);
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    protected void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        if (ConfigReader.getBoolean("highlight.elements")) {
            JsUtils.highlight(driver, element);
        }
        log.debug("Click {}", locator);
        element.click();
    }

    protected void type(By locator, String text) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    protected List<WebElement> all(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    protected List<String> texts(By locator) {
        return all(locator).stream().map(element -> element.getText().trim()).toList();
    }

    protected void waitUntilGone(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /** Path after the application's base URL, without query string, e.g. "/cart.html". */
    public String currentPath() {
        String path = driver.getCurrentUrl().replace(baseUrl, "");
        int query = path.indexOf('?');
        return query >= 0 ? path.substring(0, query) : path;
    }
}
