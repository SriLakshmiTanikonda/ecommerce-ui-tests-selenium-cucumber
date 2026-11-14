package com.qa.automation.pages.internet;

import com.qa.automation.config.ConfigReader;
import com.qa.automation.pages.BasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SecureLoginPage extends BasePage {

    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By FLASH_MESSAGE = By.id("flash");
    private static final By SECURE_AREA_HEADING = By.cssSelector("div.example h2");
    private static final By LOGOUT_BUTTON = By.cssSelector("a[href='/logout']");

    public SecureLoginPage(WebDriver driver) {
        super(driver, ConfigReader.internetUrl());
    }

    public void open() {
        open("/login");
    }

    public void loginAs(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }

    /** The flash banner ends with a close "×" character, which is stripped here. */
    public String flashMessage() {
        return text(FLASH_MESSAGE).replace("×", "").trim();
    }

    public String secureAreaHeading() {
        return text(SECURE_AREA_HEADING);
    }

    public void logout() {
        click(LOGOUT_BUTTON);
    }
}
