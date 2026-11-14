package com.qa.automation.pages.internet;

import com.qa.automation.config.ConfigReader;
import com.qa.automation.pages.BasePage;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/** Checkboxes (/checkboxes) and the dropdown (/dropdown). */
public class FormElementsPage extends BasePage {

    private static final By CHECKBOXES = By.cssSelector("#checkboxes input[type='checkbox']");
    private static final By DROPDOWN = By.id("dropdown");

    public FormElementsPage(WebDriver driver) {
        super(driver, ConfigReader.internetUrl());
    }

    public void openCheckboxes() {
        open("/checkboxes");
    }

    public void openDropdown() {
        open("/dropdown");
    }

    /** Checkbox numbers start at 1, matching how the feature files describe them. */
    private WebElement checkbox(int number) {
        List<WebElement> boxes = all(CHECKBOXES);
        return boxes.get(number - 1);
    }

    public boolean isChecked(int number) {
        return checkbox(number).isSelected();
    }

    public void setChecked(int number, boolean checked) {
        WebElement box = checkbox(number);
        if (box.isSelected() != checked) {
            box.click();
        }
    }

    public void selectOption(String visibleText) {
        new Select(visible(DROPDOWN)).selectByVisibleText(visibleText);
    }

    public String selectedOption() {
        return new Select(visible(DROPDOWN)).getFirstSelectedOption().getText().trim();
    }

    public List<String> dropdownOptions() {
        return new Select(visible(DROPDOWN)).getOptions().stream()
                .map(option -> option.getText().trim())
                .toList();
    }
}
