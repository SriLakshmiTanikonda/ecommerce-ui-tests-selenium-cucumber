package com.qa.automation.steps.saucedemo;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.automation.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;

public class CartSteps {

    private final TestContext context;

    public CartSteps(TestContext context) {
        this.context = context;
    }

    @When("I add these products to the cart:")
    public void iAddTheseProducts(List<String> products) {
        for (String product : products) {
            context.inventoryPage().addToCart(product);
        }
        context.put("cartProducts", List.copyOf(products));
    }

    @When("I add {string} to the cart")
    public void iAddToTheCart(String product) {
        context.inventoryPage().addToCart(product);
    }

    @When("I remove {string} from the products page")
    public void iRemoveFromProductsPage(String product) {
        context.inventoryPage().removeFromCart(product);
    }

    @When("I add the product to the cart from its details page")
    public void iAddFromDetailsPage() {
        context.productDetailsPage().addToCart();
    }

    @Then("the details page button should say {string}")
    public void detailsButtonShouldSay(String label) {
        assertThat(context.productDetailsPage().buttonLabel()).isEqualTo(label);
    }

    @Then("the button for {string} should say {string}")
    public void theButtonShouldSay(String product, String label) {
        assertThat(context.inventoryPage().buttonLabel(product)).isEqualTo(label);
    }

    @Then("the cart badge should show {int}")
    public void theCartBadgeShouldShow(int count) {
        assertThat(context.inventoryPage().cartCount()).isEqualTo(count);
    }

    @Then("the cart badge should be hidden")
    public void theCartBadgeShouldBeHidden() {
        assertThat(context.inventoryPage().cartCount()).isZero();
    }

    @When("I open the cart")
    public void iOpenTheCart() {
        context.inventoryPage().openCart();
    }

    @Then("the cart should contain exactly the products I added")
    public void cartContainsAdded() {
        @SuppressWarnings("unchecked")
        List<String> added = context.get("cartProducts", List.class);
        assertThat(context.cartPage().itemNames()).containsExactlyElementsOf(added);
        assertThat(context.cartPage().quantities()).allMatch(quantity -> quantity == 1);
    }

    @Then("the cart should contain:")
    public void theCartShouldContain(List<String> expected) {
        assertThat(context.cartPage().itemNames()).containsExactlyElementsOf(expected);
    }

    @Then("the cart should be empty")
    public void theCartShouldBeEmpty() {
        assertThat(context.cartPage().itemNames()).isEmpty();
    }

    @When("I remove {string} from the cart")
    public void iRemoveFromTheCart(String product) {
        context.cartPage().remove(product);
    }

    @When("I continue shopping")
    public void iContinueShopping() {
        context.cartPage().continueShopping();
    }
}
