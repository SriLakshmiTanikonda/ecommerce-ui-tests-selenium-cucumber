package com.qa.automation.context;

import com.qa.automation.driver.DriverFactory;
import com.qa.automation.pages.BasePage;
import com.qa.automation.pages.internet.AlertsAndWindowsPage;
import com.qa.automation.pages.internet.DynamicLoadingPage;
import com.qa.automation.pages.internet.FormElementsPage;
import com.qa.automation.pages.internet.SecureLoginPage;
import com.qa.automation.pages.internet.TablesPage;
import com.qa.automation.pages.saucedemo.CartPage;
import com.qa.automation.pages.saucedemo.CheckoutPage;
import com.qa.automation.pages.saucedemo.InventoryPage;
import com.qa.automation.pages.saucedemo.ProductDetailsPage;
import com.qa.automation.pages.saucedemo.SauceLoginPage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.openqa.selenium.WebDriver;

/**
 * One instance per scenario, injected by PicoContainer into hooks and every
 * step class. Because each scenario gets its own context and browser,
 * scenarios can run in parallel without sharing state.
 */
public class TestContext {

    private WebDriver driver;
    private final Map<Class<? extends BasePage>, BasePage> pages = new HashMap<>();
    private final Map<String, Object> scenarioData = new HashMap<>();

    public void startBrowser() {
        driver = DriverFactory.create();
    }

    public void stopBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public WebDriver driver() {
        if (driver == null) {
            throw new IllegalStateException("Browser not started. Is the @Before hook running?");
        }
        return driver;
    }

    public boolean hasBrowser() {
        return driver != null;
    }

    /** Page objects are created on first use and reused for the rest of the scenario. */
    private <T extends BasePage> T page(Class<T> type, Function<WebDriver, T> constructor) {
        return type.cast(pages.computeIfAbsent(type, key -> constructor.apply(driver())));
    }

    /** Lets one step store a value (e.g. the products added) for a later step to check. */
    public void put(String key, Object value) {
        scenarioData.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        Object value = scenarioData.get(key);
        if (value == null) {
            throw new IllegalStateException("Nothing stored under '" + key + "' in this scenario");
        }
        return type.cast(value);
    }

    // The Internet (practice site)
    public SecureLoginPage secureLoginPage() {
        return page(SecureLoginPage.class, SecureLoginPage::new);
    }

    public DynamicLoadingPage dynamicLoadingPage() {
        return page(DynamicLoadingPage.class, DynamicLoadingPage::new);
    }

    public FormElementsPage formElementsPage() {
        return page(FormElementsPage.class, FormElementsPage::new);
    }

    public AlertsAndWindowsPage alertsAndWindowsPage() {
        return page(AlertsAndWindowsPage.class, AlertsAndWindowsPage::new);
    }

    public TablesPage tablesPage() {
        return page(TablesPage.class, TablesPage::new);
    }

    // Sauce Demo (e-commerce)
    public SauceLoginPage sauceLoginPage() {
        return page(SauceLoginPage.class, SauceLoginPage::new);
    }

    public InventoryPage inventoryPage() {
        return page(InventoryPage.class, InventoryPage::new);
    }

    public ProductDetailsPage productDetailsPage() {
        return page(ProductDetailsPage.class, ProductDetailsPage::new);
    }

    public CartPage cartPage() {
        return page(CartPage.class, CartPage::new);
    }

    public CheckoutPage checkoutPage() {
        return page(CheckoutPage.class, CheckoutPage::new);
    }
}
