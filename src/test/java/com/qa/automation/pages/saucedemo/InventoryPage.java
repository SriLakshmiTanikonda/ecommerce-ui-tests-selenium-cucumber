package com.qa.automation.pages.saucedemo;

import com.qa.automation.models.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/** The product list (/inventory.html). */
public class InventoryPage extends SauceBasePage {

    private static final By ITEMS = By.cssSelector(".inventory_item");
    private static final By ITEM_NAME = By.cssSelector(".inventory_item_name");
    private static final By ITEM_PRICE = By.cssSelector(".inventory_item_price");
    private static final By ITEM_IMAGE = By.cssSelector(".inventory_item_img img");
    private static final By ITEM_BUTTON = By.cssSelector("button");
    private static final By SORT = By.cssSelector("select.product_sort_container");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        open("/inventory.html");
    }

    /** The product card whose name matches exactly. */
    private WebElement card(String productName) {
        return all(ITEMS).stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No product named " + productName));
    }

    public void addToCart(String productName) {
        WebElement button = card(productName).findElement(ITEM_BUTTON);
        if (!button.getText().equals("Add to cart")) {
            throw new IllegalStateException(productName + " is already in the cart");
        }
        button.click();
    }

    public void removeFromCart(String productName) {
        WebElement button = card(productName).findElement(ITEM_BUTTON);
        if (!button.getText().equals("Remove")) {
            throw new IllegalStateException(productName + " is not in the cart");
        }
        button.click();
    }

    public String buttonLabel(String productName) {
        return card(productName).findElement(ITEM_BUTTON).getText().trim();
    }

    public void openDetails(String productName) {
        card(productName).findElement(ITEM_NAME).click();
    }

    public void sortBy(String visibleOption) {
        new Select(visible(SORT)).selectByVisibleText(visibleOption);
    }

    public List<String> names() {
        return texts(ITEM_NAME);
    }

    public List<BigDecimal> prices() {
        return texts(ITEM_PRICE).stream().map(Product::money).toList();
    }

    public List<Product> products() {
        List<Product> products = new ArrayList<>();
        for (WebElement item : all(ITEMS)) {
            products.add(new Product(
                    item.findElement(ITEM_NAME).getText().trim(),
                    Product.money(item.findElement(ITEM_PRICE).getText())));
        }
        return products;
    }

    public List<String> imageSources() {
        return all(ITEM_IMAGE).stream().map(img -> img.getAttribute("src")).toList();
    }

    public int productCount() {
        return all(ITEMS).size();
    }
}
