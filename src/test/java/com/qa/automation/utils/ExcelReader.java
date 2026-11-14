package com.qa.automation.utils;

import com.qa.automation.models.Customer;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Reads data-driven test input from Excel, the format business testers often
 * maintain. The first row is the header; each later row becomes a map of
 * header to cell text, so column order in the sheet doesn't matter.
 */
public final class ExcelReader {

    private static final Map<String, List<Map<String, String>>> CACHE = new HashMap<>();

    private ExcelReader() {
    }

    public static synchronized List<Map<String, String>> rows(String resource, String sheetName) {
        return CACHE.computeIfAbsent(resource + "#" + sheetName, key -> load(resource, sheetName));
    }

    private static List<Map<String, String>> load(String resource, String sheetName) {
        try (InputStream in = ExcelReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Missing Excel file: " + resource);
            }
            try (Workbook workbook = WorkbookFactory.create(in)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("No sheet '" + sheetName + "' in " + resource);
                }
                DataFormatter formatter = new DataFormatter();
                Row header = sheet.getRow(0);
                List<String> columns = new ArrayList<>();
                for (int c = 0; c < header.getLastCellNum(); c++) {
                    columns.add(formatter.formatCellValue(header.getCell(c)).trim());
                }
                List<Map<String, String>> rows = new ArrayList<>();
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    Map<String, String> values = new LinkedHashMap<>();
                    for (int c = 0; c < columns.size(); c++) {
                        // DataFormatter returns "" for empty cells and keeps "27601" as text, not 27601.0.
                        values.put(columns.get(c), formatter.formatCellValue(row.getCell(c)).trim());
                    }
                    rows.add(values);
                }
                return rows;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + resource, e);
        }
    }

    public static Customer customer(String key) {
        return rows("testdata/customers.xlsx", "customers").stream()
                .filter(row -> key.equals(row.get("key")))
                .findFirst()
                .map(row -> new Customer(row.get("key"), row.get("firstName"), row.get("lastName"),
                        row.get("postalCode"), row.get("expectedError")))
                .orElseThrow(() -> new IllegalArgumentException("No customer '" + key + "' in customers.xlsx"));
    }
}
