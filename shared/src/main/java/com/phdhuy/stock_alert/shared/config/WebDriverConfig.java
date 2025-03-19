package com.phdhuy.stock_alert.shared.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WebDriverConfig {

  private WebDriver driver;

  public synchronized WebDriver getWebDriver() {
    if (driver == null) {
      driver = createNewDriver();
    }
    return driver;
  }

  private WebDriver createNewDriver() {
    FirefoxOptions options = new FirefoxOptions();
    options.addArguments(
        "--headless",
        "--disable-gpu",
        "--no-sandbox",
        "start-maximized",
        "disable-infobars",
        "--disable-extensions",
        "--disable-application-cache",
        "--disable-dev-shm-usage");
    options.addPreference("browser.cache.disk.enable", false);
    options.addPreference("browser.cache.memory.enable", false);
    options.addPreference("browser.cache.offline.enable", false);
    options.addPreference("network.http.use-cache", false);

    WebDriver newDriver = new FirefoxDriver(options);
    log.info("Initialized new WebDriver instance.");
    return newDriver;
  }

  public synchronized void quitWebDriver() {
    if (driver != null) {
      try {
        log.info("Quitting web driver");
        driver.manage().deleteAllCookies();
        driver.quit();
      } catch (Exception e) {
        log.error("Error while quitting WebDriver: {}", e.getMessage(), e);
      } finally {
        driver = null;
      }
    }
  }

  public synchronized void resetDriver() {
    log.info("Resetting WebDriver instance.");
    quitWebDriver();
    driver = createNewDriver();
  }

  public boolean isWebDriverAlive() {
    return driver != null;
  }
}
