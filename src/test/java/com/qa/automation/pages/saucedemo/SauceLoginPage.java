package com.qa.automation.pages.saucedemo;

import com.qa.automation.config.ConfigReader;
import com.qa.automation.models.User;
import com.qa.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SauceLoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("[data-test='error']");
    private static final By ERROR_CLOSE = By.cssSelector(".error-button");

    public SauceLoginPage(WebDriver driver) {
        super(driver, ConfigReader.sauceDemoUrl());
    }

    public void open() {
        open("/");
    }

    public void login(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }

    public void login(User user) {
        log.info("Logging in as {}", user.username());
        login(user.username(), user.password());
    }

    public String error() {
        return text(ERROR);
    }

    public boolean isErrorShown() {
        return isDisplayed(ERROR);
    }

    public void dismissError() {
        click(ERROR_CLOSE);
    }

    public boolean isShown() {
        return isDisplayed(LOGIN_BUTTON);
    }
}
