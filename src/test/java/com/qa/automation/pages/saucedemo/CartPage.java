package com.qa.automation.pages.saucedemo;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** The cart (/cart.html). */
public class CartPage extends SauceBasePage {

    private static final By CART_ITEMS = By.cssSelector(".cart_item");
    private static final By ITEM_NAME = By.cssSelector(".inventory_item_name");
    private static final By ITEM_QUANTITY = By.cssSelector(".cart_quantity");
    private static final By CHECKOUT = By.id("checkout");
    private static final By CONTINUE_SHOPPING = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        open("/cart.html");
    }

    /** Empty list for an empty cart (no waiting for items that will never appear). */
    public List<String> itemNames() {
        return driver.findElements(CART_ITEMS).stream()
                .map(item -> item.findElement(ITEM_NAME).getText().trim())
                .toList();
    }

    public List<Integer> quantities() {
        return driver.findElements(ITEM_QUANTITY).stream()
                .map(quantity -> Integer.parseInt(quantity.getText().trim()))
                .toList();
    }

    public void remove(String productName) {
        WebElement item = driver.findElements(CART_ITEMS).stream()
                .filter(row -> row.findElement(ITEM_NAME).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(productName + " is not in the cart"));
        item.findElement(By.tagName("button")).click();
    }

    public void checkout() {
        click(CHECKOUT);
    }

    public void continueShopping() {
        click(CONTINUE_SHOPPING);
    }
}
