package com.qa.automation.steps.saucedemo;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.automation.context.TestContext;
import com.qa.automation.models.Product;
import com.qa.automation.pages.saucedemo.InventoryPage;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProductSteps {

    private final TestContext context;

    public ProductSteps(TestContext context) {
        this.context = context;
    }

    private InventoryPage inventory() {
        return context.inventoryPage();
    }

    /** Converts DataTable rows with "name" and "price" columns into Product objects. */
    @DataTableType
    public Product productEntry(Map<String, String> row) {
        return new Product(row.get("name"), Product.money(row.get("price")));
    }

    @Then("I should see {int} products")
    public void iShouldSeeProducts(int count) {
        assertThat(inventory().productCount()).isEqualTo(count);
    }

    @Then("the catalog should list these products:")
    public void theCatalogShouldList(List<Product> expected) {
        assertThat(inventory().products()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @When("I sort products by {string}")
    public void iSortProductsBy(String option) {
        inventory().sortBy(option);
    }

    @Then("products should be sorted by {word} in {word} order")
    public void productsShouldBeSorted(String field, String direction) {
        boolean ascending = direction.equals("ascending");
        if (field.equals("name")) {
            List<String> names = inventory().names();
            List<String> expected = new ArrayList<>(names);
            Comparator<String> byName = String.CASE_INSENSITIVE_ORDER;
            expected.sort(ascending ? byName : byName.reversed());
            assertThat(names).isEqualTo(expected);
        } else if (field.equals("price")) {
            List<BigDecimal> prices = inventory().prices();
            List<BigDecimal> expected = new ArrayList<>(prices);
            expected.sort(ascending ? Comparator.naturalOrder() : Comparator.reverseOrder());
            assertThat(prices).isEqualTo(expected);
        } else {
            throw new IllegalArgumentException("Unknown sort field: " + field);
        }
    }

    @When("I open the details of {string}")
    public void iOpenTheDetailsOf(String productName) {
        BigDecimal listPrice = inventory().products().stream()
                .filter(product -> product.name().equals(productName))
                .findFirst()
                .orElseThrow()
                .price();
        context.put("listPrice", listPrice);
        inventory().openDetails(productName);
    }

    @Then("the details page should show {string} at the same price as the list")
    public void detailsMatchList(String productName) {
        assertThat(context.productDetailsPage().name()).isEqualTo(productName);
        assertThat(context.productDetailsPage().price()).isEqualByComparingTo(context.get("listPrice", BigDecimal.class));
        assertThat(context.productDetailsPage().description()).isNotBlank();
    }

    @When("I go back to the products page")
    public void iGoBackToTheProductsPage() {
        context.productDetailsPage().backToProducts();
    }

    @Then("every product should have its own image")
    public void everyProductShouldHaveItsOwnImage() {
        List<String> sources = inventory().imageSources();
        assertThat(sources).doesNotHaveDuplicates();
    }
}
