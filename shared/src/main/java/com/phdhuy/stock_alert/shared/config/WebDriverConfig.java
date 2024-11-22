package com.phdhuy.stock_alert.shared.config;

import jakarta.annotation.PreDestroy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Configuration
public class WebDriverConfig {
  private static final Logger LOGGER = Logger.getLogger(WebDriverConfig.class.getName());
  private static final Duration SESSION_TIMEOUT = Duration.ofHours(24);

  private final AtomicReference<WebDriver> driverRef = new AtomicReference<>();
  private volatile Instant lastInitTime = null;

  private FirefoxOptions createFirefoxOptions() {
    FirefoxOptions options = new FirefoxOptions();

    options.addArguments(
        "--headless",
        "--disable-gpu",
        "--no-sandbox",
        "--width=1920",
        "--height=1080",
        "--disable-infobars",
        "--disable-extensions",
        "--disable-application-cache",
        "--disable-dev-shm-usage");

    options.addArguments(
        "--memory-pressure-off",
        "--disable-features=site-per-process",
        "--disable-logging",
        "--disable-permissions-api",
        "--disable-remote-fonts",
        "--disable-background-networking",
        "--force-renderer-accessibility");

    options.addPreference("browser.cache.disk.enable", false);
    options.addPreference("browser.cache.memory.enable", false);
    options.addPreference("browser.cache.offline.enable", false);
    options.addPreference("network.http.use-cache", false);
    options.addPreference("browser.sessionhistory.max_entries", 5);
    options.addPreference("browser.sessionhistory.max_total_viewers", 5);
    options.addPreference("browser.sessionstore.max_tabs_undo", 0);
    options.addPreference("browser.sessionstore.resume_from_crash", false);
    options.addPreference("browser.tabs.remote.autostart", false);
    options.addPreference("dom.ipc.processCount", 1);
    options.addPreference("memory.free_dirty_pages", true);
    options.addPreference("javascript.options.mem.max", 512 * 1024);

    return options;
  }

  public synchronized WebDriver getWebDriver() throws MalformedURLException {
    WebDriver currentDriver = driverRef.get();

    if (currentDriver == null || shouldRenewSession()) {
      if (currentDriver != null) {
        cleanupDriver(currentDriver);
      }

      FirefoxOptions options = createFirefoxOptions();
      try {
        WebDriver newDriver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);

        newDriver
            .manage()
            .timeouts()
            .pageLoadTimeout(Duration.ofSeconds(30))
            .scriptTimeout(Duration.ofSeconds(30))
            .implicitlyWait(Duration.ofSeconds(10));

        driverRef.set(newDriver);
        lastInitTime = Instant.now();
        LOGGER.info("New WebDriver session initialized");

        return newDriver;
      } catch (Exception e) {
        LOGGER.severe("Failed to initialize WebDriver: " + e.getMessage());
        throw new RuntimeException("WebDriver initialization failed", e);
      }
    }

    return currentDriver;
  }

  private boolean shouldRenewSession() {
    return lastInitTime != null
        && Duration.between(lastInitTime, Instant.now()).compareTo(SESSION_TIMEOUT) > 0;
  }

  @Scheduled(fixedRate = 300000)
  public void performPeriodicCleanup() {
    WebDriver currentDriver = driverRef.get();
    if (currentDriver != null) {
      try {
        currentDriver.manage().deleteAllCookies();

        if (currentDriver instanceof RemoteWebDriver) {
          ((RemoteWebDriver) currentDriver)
              .executeScript(
                  "window.performance && window.performance.memory && window.performance.memory.usedJSHeapSize;");
          ((RemoteWebDriver) currentDriver)
              .executeScript("if(window.localStorage) window.localStorage.clear();");
          ((RemoteWebDriver) currentDriver)
              .executeScript("if(window.sessionStorage) window.sessionStorage.clear();");
        }

        LOGGER.info("Periodic cleanup completed successfully");
      } catch (Exception e) {
        LOGGER.warning("Error during periodic cleanup: " + e.getMessage());
      }
    }
  }

  private void cleanupDriver(WebDriver driver) {
    try {
      driver.manage().deleteAllCookies();
      driver.quit();
    } catch (Exception e) {
      LOGGER.warning("Error during driver cleanup: " + e.getMessage());
    }
  }

  @PreDestroy
  public void quitWebDriver() {
    WebDriver currentDriver = driverRef.get();
    if (currentDriver != null) {
      cleanupDriver(currentDriver);
      driverRef.set(null);
      lastInitTime = null;
    }
  }
}
