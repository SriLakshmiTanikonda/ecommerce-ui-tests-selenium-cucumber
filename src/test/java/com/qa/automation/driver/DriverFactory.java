package com.qa.automation.driver;

import com.qa.automation.config.ConfigReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Creates a browser from configuration, either locally or on a Selenium Grid.
 * Locally, Selenium Manager (built into Selenium 4.6+) downloads the matching
 * driver, so no driver binaries or WebDriverManager are needed.
 */
public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver create() {
        String browser = ConfigReader.get("browser").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");
        Capabilities options = options(browser, headless);
        Optional<String> gridUrl = ConfigReader.find("grid.url");

        WebDriver driver;
        if (gridUrl.isPresent()) {
            LOG.info("Starting {} on Selenium Grid at {}", browser, gridUrl.get());
            driver = remote(gridUrl.get(), options);
        } else {
            LOG.info("Starting local {} (headless={})", browser, headless);
            driver = local(browser, options);
        }

        // Explicit waits only: implicit wait stays at 0 so the two never mix.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout.seconds")));
        return driver;
    }

    private static WebDriver local(String browser, Capabilities options) {
        return switch (browser) {
            case "chrome" -> new ChromeDriver((ChromeOptions) options);
            case "firefox" -> new FirefoxDriver((FirefoxOptions) options);
            case "edge" -> new EdgeDriver((EdgeOptions) options);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static WebDriver remote(String gridUrl, Capabilities options) {
        try {
            return new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid grid.url: " + gridUrl, e);
        }
    }

    private static Capabilities options(String browser, boolean headless) {
        return switch (browser) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1920,1080", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
                disablePasswordPopups(options);
                yield options;
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                options.addArguments("--width=1920", "--height=1080");
                yield options;
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1920,1080");
                disablePasswordPopups(options);
                yield options;
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    /**
     * Chromium browsers show a "password found in a data breach" dialog after a
     * login with a well-known password (such as the demo sites' passwords). The
     * dialog sits over the page and swallows clicks, so turn the password
     * manager and its leak check off.
     */
    private static void disablePasswordPopups(ChromiumOptions<?> options) {
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        options.addArguments("--disable-features=PasswordLeakDetection,PasswordCheck");
    }
}
