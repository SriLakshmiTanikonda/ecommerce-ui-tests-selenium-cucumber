package com.qa.automation.pages.saucedemo;

import com.qa.automation.models.Customer;
import com.qa.automation.models.Product;
import java.math.BigDecimal;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * The three checkout steps: your information (/checkout-step-one.html),
 * overview (/checkout-step-two.html) and confirmation (/checkout-complete.html).
 */
public class CheckoutPage extends SauceBasePage {

    // Step one
    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.id("continue");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    // Step two
    private static final By ITEM_NAMES = By.cssSelector(".cart_item .inventory_item_name");
    private static final By ITEM_PRICES = By.cssSelector(".cart_item .inventory_item_price");
    private static final By SUBTOTAL = By.cssSelector(".summary_subtotal_label");
    private static final By TAX = By.cssSelector(".summary_tax_label");
    private static final By TOTAL = By.cssSelector(".summary_total_label");
    private static final By FINISH = By.id("finish");
    private static final By CANCEL = By.id("cancel");

    // Confirmation
    private static final By COMPLETE_HEADER = By.cssSelector(".complete-header");
    private static final By BACK_HOME = By.id("back-to-products");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void enterCustomer(Customer customer) {
        log.info("Checkout as customer '{}'", customer.key());
        type(FIRST_NAME, customer.firstName());
        type(LAST_NAME, customer.lastName());
        type(POSTAL_CODE, customer.postalCode());
        click(CONTINUE);
    }

    public String error() {
        return text(ERROR);
    }

    public List<String> overviewItemNames() {
        return texts(ITEM_NAMES);
    }

    public List<BigDecimal> overviewItemPrices() {
        return texts(ITEM_PRICES).stream().map(Product::money).toList();
    }

    public BigDecimal itemTotal() {
        return Product.money(text(SUBTOTAL));
    }

    public BigDecimal tax() {
        return Product.money(text(TAX));
    }

    public BigDecimal total() {
        return Product.money(text(TOTAL));
    }

    public void finish() {
        click(FINISH);
    }

    public void cancel() {
        click(CANCEL);
    }

    public String confirmationHeader() {
        return text(COMPLETE_HEADER);
    }

    public void backHome() {
        click(BACK_HOME);
    }
}
