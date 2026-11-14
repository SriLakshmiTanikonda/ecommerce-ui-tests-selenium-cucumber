package com.qa.automation.steps.saucedemo;

import static org.assertj.core.api.Assertions.assertThat;

import com.qa.automation.context.TestContext;
import com.qa.automation.models.Customer;
import com.qa.automation.pages.saucedemo.CheckoutPage;
import com.qa.automation.utils.ExcelReader;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CheckoutSteps {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.08");

    private final TestContext context;

    public CheckoutSteps(TestContext context) {
        this.context = context;
    }

    private CheckoutPage checkout() {
        return context.checkoutPage();
    }

    @When("I check out")
    public void iCheckOut() {
        context.cartPage().checkout();
    }

    /** Customer details come from testdata/customers.xlsx, looked up by the "key" column. */
    @When("I enter the details of customer {string}")
    public void iEnterCustomerDetails(String customerKey) {
        Customer customer = ExcelReader.customer(customerKey);
        context.put("customer", customer);
        checkout().enterCustomer(customer);
    }

    @Then("I should see the error expected for that customer")
    public void iShouldSeeTheExpectedError() {
        Customer customer = context.get("customer", Customer.class);
        assertThat(customer.isValid()).as("customer '%s' should be an invalid row", customer.key()).isFalse();
        assertThat(checkout().error()).isEqualTo(customer.expectedError());
    }

    @Then("the order overview should list the products I added")
    public void overviewListsProducts() {
        @SuppressWarnings("unchecked")
        List<String> added = context.get("cartProducts", List.class);
        assertThat(checkout().overviewItemNames()).containsExactlyElementsOf(added);
    }

    @Then("the item total should equal the sum of the item prices")
    public void itemTotalIsSum() {
        BigDecimal sum = checkout().overviewItemPrices().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(checkout().itemTotal()).isEqualByComparingTo(sum);
    }

    @Then("the tax should be 8% of the item total")
    public void taxIsEightPercent() {
        BigDecimal expected = checkout().itemTotal().multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        assertThat(checkout().tax()).isEqualByComparingTo(expected);
    }

    @Then("the order total should equal the item total plus tax")
    public void totalIsItemTotalPlusTax() {
        assertThat(checkout().total()).isEqualByComparingTo(checkout().itemTotal().add(checkout().tax()));
    }

    @Then("the order total should be {string}")
    public void theOrderTotalShouldBe(String amount) {
        assertThat(checkout().total()).isEqualByComparingTo(new BigDecimal(amount.replace("$", "")));
    }

    @When("I finish the order")
    public void iFinishTheOrder() {
        checkout().finish();
    }

    @When("I cancel the checkout")
    public void iCancelTheCheckout() {
        checkout().cancel();
    }

    @Then("I should see the order confirmation {string}")
    public void iShouldSeeTheConfirmation(String header) {
        assertThat(checkout().confirmationHeader()).isEqualTo(header);
    }

    @When("I go back home")
    public void iGoBackHome() {
        checkout().backHome();
    }
}
