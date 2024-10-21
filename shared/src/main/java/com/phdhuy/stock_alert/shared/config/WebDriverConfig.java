package com.phdhuy.stock_alert.shared.config;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDriverConfig {

  @Bean
  public WebDriver initializeWebDriver() throws MalformedURLException {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");

    return new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
  }
}
