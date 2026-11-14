package com.qa.automation.pages.saucedemo;

import com.qa.automation.models.Product;
import java.math.BigDecimal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** A single product (/inventory-item.html?id=N). */
public class ProductDetailsPage extends SauceBasePage {

    private static final By NAME = By.cssSelector(".inventory_details_name");
    private static final By PRICE = By.cssSelector(".inventory_details_price");
    private static final By DESCRIPTION = By.cssSelector(".inventory_details_desc");
    private static final By ADD_OR_REMOVE = By.cssSelector(".inventory_details_desc_container button");
    private static final By BACK = By.id("back-to-products");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String name() {
        return text(NAME);
    }

    public BigDecimal price() {
        return Product.money(text(PRICE));
    }

    public String description() {
        return text(DESCRIPTION);
    }

    public void addToCart() {
        click(ADD_OR_REMOVE);
    }

    public String buttonLabel() {
        return text(ADD_OR_REMOVE);
    }

    public void backToProducts() {
        click(BACK);
    }
}
