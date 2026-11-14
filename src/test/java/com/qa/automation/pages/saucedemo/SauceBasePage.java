package com.qa.automation.pages.saucedemo;

import com.qa.automation.config.ConfigReader;
import com.qa.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Header parts shared by every logged-in Sauce Demo page: title, cart badge, side menu. */
public abstract class SauceBasePage extends BasePage {

    private static final By TITLE = By.cssSelector(".title");
    private static final By CART_LINK = By.cssSelector(".shopping_cart_link");
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By MENU_CLOSE = By.id("react-burger-cross-btn");
    private static final By MENU_ALL_ITEMS = By.id("inventory_sidebar_link");
    private static final By MENU_LOGOUT = By.id("logout_sidebar_link");
    private static final By MENU_RESET = By.id("reset_sidebar_link");

    protected SauceBasePage(WebDriver driver) {
        super(driver, ConfigReader.sauceDemoUrl());
    }

    public String title() {
        return text(TITLE);
    }

    public void openCart() {
        click(CART_LINK);
    }

    /** 0 when the badge is hidden, which is how the store shows an empty cart. */
    public int cartCount() {
        return isDisplayed(CART_BADGE) ? Integer.parseInt(text(CART_BADGE)) : 0;
    }

    public void openMenuItem(String item) {
        click(MENU_BUTTON);
        By link = switch (item) {
            case "All Items" -> MENU_ALL_ITEMS;
            case "Logout" -> MENU_LOGOUT;
            case "Reset App State" -> MENU_RESET;
            default -> throw new IllegalArgumentException("Unknown menu item: " + item);
        };
        // The menu slides in; elementToBeClickable waits for the animation to finish.
        click(link);
        if (isDisplayed(MENU_CLOSE)) {
            click(MENU_CLOSE);
        }
    }
}
