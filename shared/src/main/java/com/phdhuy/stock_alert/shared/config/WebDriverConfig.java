package com.phdhuy.stock_alert.shared.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class WebDriverConfig {

  private WebDriver driver = null;

  public WebDriver getWebDriver() {
    if (driver == null) {
      FirefoxOptions options = new FirefoxOptions();
      options.addArguments("--headless");
      options.addArguments("--disable-gpu");
      options.addArguments("--no-sandbox");
      options.addArguments("start-maximized");
      options.addArguments("disable-infobars");
      options.addArguments("--disable-extensions");
      options.addArguments("--disable-application-cache");
      options.addArguments("--disable-dev-shm-usage");

      options.addPreference("browser.cache.disk.enable", false);
      options.addPreference("browser.cache.memory.enable", false);
      options.addPreference("browser.cache.offline.enable", false);
      options.addPreference("network.http.use-cache", false);

      driver = new FirefoxDriver(options);
    }
    return driver;
  }

  public void quitWebDriver() {
    if (driver != null) {
      log.info("Quitting web driver");
      driver.manage().deleteAllCookies();
      driver.quit();
      driver = null;
    }
  }

  public boolean isWebDriverAlive() {
    return driver != null;
  }
}
