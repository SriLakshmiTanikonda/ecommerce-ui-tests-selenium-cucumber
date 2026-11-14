package com.qa.automation.pages.internet;

import com.qa.automation.config.ConfigReader;
import com.qa.automation.pages.BasePage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** The sortable data table at /tables (first table, id "table1"). */
public class TablesPage extends BasePage {

    private static final By HEADERS = By.cssSelector("#table1 thead th");
    private static final By ROWS = By.cssSelector("#table1 tbody tr");

    public TablesPage(WebDriver driver) {
        super(driver, ConfigReader.internetUrl());
    }

    public void open() {
        open("/tables");
    }

    public List<String> headers() {
        return all(HEADERS).stream().map(h -> h.getText().trim()).toList();
    }

    public void sortBy(String header) {
        int index = headers().indexOf(header);
        if (index < 0) {
            throw new IllegalArgumentException("No column named " + header);
        }
        all(HEADERS).get(index).click();
    }

    /** Reads the table into a list of rows keyed by column header. */
    public List<Map<String, String>> rows() {
        List<String> headers = headers();
        List<Map<String, String>> rows = new ArrayList<>();
        for (WebElement row : all(ROWS)) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            Map<String, String> values = new LinkedHashMap<>();
            for (int i = 0; i < headers.size() && i < cells.size(); i++) {
                values.put(headers.get(i), cells.get(i).getText().trim());
            }
            rows.add(values);
        }
        return rows;
    }

    public List<String> column(String header) {
        return rows().stream().map(row -> row.get(header)).toList();
    }
}
