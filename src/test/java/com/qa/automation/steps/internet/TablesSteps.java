package com.qa.automation.steps.internet;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.automation.context.TestContext;
import com.qa.automation.pages.internet.TablesPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TablesSteps {

    private final TestContext context;

    public TablesSteps(TestContext context) {
        this.context = context;
    }

    private TablesPage page() {
        return context.tablesPage();
    }

    @Given("I am on the tables page")
    public void iAmOnTheTablesPage() {
        page().open();
    }

    @When("I sort the table by {string}")
    public void iSortTheTableBy(String column) {
        page().sortBy(column);
    }

    /** Compares only the columns named in the feature file, in any row order. */
    @Then("the table should contain these rows:")
    public void theTableShouldContain(List<Map<String, String>> expected) {
        List<Map<String, String>> actual = new ArrayList<>();
        for (Map<String, String> row : page().rows()) {
            Map<String, String> subset = new LinkedHashMap<>();
            for (String column : expected.get(0).keySet()) {
                subset.put(column, row.get(column));
            }
            actual.add(subset);
        }
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Then("the {string} column should be in ascending order")
    public void theColumnShouldBeAscending(String column) {
        List<String> values = page().column(column);
        List<String> sorted = new ArrayList<>(values);
        sorted.sort(String.CASE_INSENSITIVE_ORDER);
        assertThat(values).isEqualTo(sorted);
    }
}
