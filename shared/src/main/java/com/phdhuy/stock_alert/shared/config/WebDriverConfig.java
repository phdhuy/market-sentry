package com.phdhuy.stock_alert.shared.config;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDriverConfig {

  private static WebDriver driver;

  public WebDriver getWebDriver() throws MalformedURLException {
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

      driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
    }
    return driver;
  }

  public void reinitializeWebDriver() throws MalformedURLException, InterruptedException {
    if (driver != null) {
      driver.quit();
      driver = null;
      Thread.sleep(3000);
      getWebDriver();
    }
  }
}