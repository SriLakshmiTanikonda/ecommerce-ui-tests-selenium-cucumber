# Selenium + Java + Cucumber BDD Framework

[![Selenium Cucumber Tests](https://github.com/SriLakshmiTanikonda/ecommerce-ui-tests-selenium-cucumber/actions/workflows/tests.yml/badge.svg)](https://github.com/SriLakshmiTanikonda/ecommerce-ui-tests-selenium-cucumber/actions/workflows/tests.yml)

A behavior-driven UI test framework built with Selenium WebDriver 4, Cucumber 7 and TestNG. It covers two applications:

| Application | What it's used for |
|---|---|
| [Sauce Demo](https://www.saucedemo.com) (e-commerce store) | Business flows: login, catalog, product details, cart, checkout, order totals |
| [The Internet](https://the-internet.herokuapp.com) (practice site) | Hard browser-automation cases: dynamic loading, alerts, multiple windows, form controls, tables |

It includes Allure reporting, Log4j2 logging, JSON and Excel test data, and an automatic retry of failed scenarios.

## Test coverage

### Sauce Demo (e-commerce)

| Feature | Scenarios |
|---|---|
| `store_login.feature` | Successful login, locked account, wrong password, required-field checks, dismissing errors, slow-account login time limit, logout ends the session |
| `products.feature` | Full catalog with prices (DataTable), 4 sort orders (Scenario Outline), details page matches catalog, unique product images |
| `cart.feature` | Add several products, remove from catalog or cart page, add from details page, cart kept while browsing, Reset App State, empty cart |
| `checkout.feature` | End-to-end purchase; item total, 8% tax and order total checked; per-product totals; valid and invalid customer details from Excel; cancel keeps the cart |

### The Internet (practice site)

| Feature | Scenarios |
|---|---|
| `secure_login.feature` | Login and logout, invalid credentials |
| `dynamic_loading.feature` | Explicit waits for hidden and late-rendered elements |
| `form_elements.feature` | Checkboxes and dropdowns |
| `alerts_and_windows.feature` | Alert, confirm, prompt and new-window handling |
| `tables.feature` | Whole-table data comparison and sort order |

## Framework design

```
src/test/java/com/qa/automation/
  config/       ConfigReader: properties file, overridable by -D flags or environment variables
  driver/       DriverFactory: Chrome, Firefox or Edge, local or Selenium Grid
  pages/        BasePage with explicit-wait helpers and logging
    saucedemo/  Login, Inventory, ProductDetails, Cart, Checkout pages + shared header/menu
    internet/   Practice-site pages
  context/      TestContext: one browser, lazily created pages and shared data per scenario
  hooks/        Browser start/stop, screenshot and URL on failure, per-scenario log tagging
  steps/        Step definitions: saucedemo/, internet/, common/
  models/       User, Customer, Product (BigDecimal money)
  utils/        JsonDataReader, ExcelReader (Apache POI), JsUtils
  runners/      TestRunner (parallel) and FailedScenariosRunner (retry pass)
src/test/resources/
  features/     saucedemo/, internet/
  testdata/     users.json, customers.xlsx
  config.properties, log4j2.xml, allure.properties
Jenkinsfile     Parameterized pipeline: browser, tags, threads, retry
```

Choices and why:

- **Explicit waits only.** Implicit wait is zero, and every interaction goes through `BasePage` helpers that wait for the element to be visible or clickable. Mixing the two wait types is a common cause of slow, flaky suites.
- **PicoContainer dependency injection.** Step classes get a shared `TestContext` through their constructor, not static fields. Each scenario has its own browser, so scenarios run safely in parallel.
- **Test data outside the code.** Users live in JSON. Checkout customers, including the exact error each invalid row should produce, live in an Excel sheet a business tester can edit without touching code.
- **Money as `BigDecimal`.** Totals and 8% tax are compared exactly, not as floating-point numbers that drift by a cent.
- **Retry pass, not hidden retries.** Failed scenarios are written to `target/rerun.txt` and run once more. A scenario that passes on retry shows as flaky in Allure; a scenario that fails twice fails the build.
- **Logs per scenario.** Log4j2 tags every line with the scenario name, so parallel runs can be told apart in `target/logs/test-run.log`.

## Run it

Requires Java 17 or later, Maven 3.9 or later, and Chrome or Firefox.

```bash
mvn test                                              # all scenarios, headless Chrome
mvn test -Dcucumber.filter.tags="@smoke"              # smoke set
mvn test -Dcucumber.filter.tags="@checkout"           # one feature area
mvn test -Dbrowser=firefox -Dheadless=false           # watch it in Firefox
mvn test -Dparallel.threads=1 -Drerun=false           # one at a time, no retry
mvn test -Dgrid.url=http://localhost:4444             # run on a Selenium Grid

mvn allure:serve                                      # open the Allure report
```

Other reports: `target/cucumber-reports/cucumber.html` and the log at `target/logs/test-run.log`.

## CI/CD

- **GitHub Actions** runs on every push and pull request, plus nightly, on Chrome and Firefox in parallel. It includes the retry pass, publishes Allure and Cucumber reports and logs, and shows a JUnit summary on the run page. A manual run can take any tag expression.
- **Jenkins** uses the `Jenkinsfile`, with parameters for browser, tags, thread count and retry. It publishes JUnit results and the Allure report.

## Tech stack

Java 17 · Selenium WebDriver 4 · Cucumber 7 · TestNG · PicoContainer · AssertJ · Allure · Log4j2 · Jackson · Apache POI · Maven · GitHub Actions · Jenkins
