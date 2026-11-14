package com.qa.automation.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** JavaScript helpers for things WebDriver can't do directly. */
public final class JsUtils {

    private JsUtils() {
    }

    public static void scrollIntoView(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    /** Outlines the element briefly, so a headed run or a recording shows what is being clicked. */
    public static void highlight(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "const el = arguments[0]; const old = el.style.outline;"
                        + "el.style.outline = '3px solid #e91e63';"
                        + "setTimeout(() => el.style.outline = old, 400);", element);
    }

    public static String localStorageItem(WebDriver driver, String key) {
        Object value = ((JavascriptExecutor) driver).executeScript("return window.localStorage.getItem(arguments[0]);", key);
        return value == null ? null : value.toString();
    }

    public static void clearStorage(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear(); window.sessionStorage.clear();");
    }
}
