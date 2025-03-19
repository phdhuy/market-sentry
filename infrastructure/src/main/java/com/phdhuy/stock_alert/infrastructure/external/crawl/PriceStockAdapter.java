package com.phdhuy.stock_alert.infrastructure.external.crawl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.infrastructure.external.messagebroker.RabbitMQAdapter;
import com.phdhuy.stock_alert.shared.config.WebDriverConfig;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import jakarta.annotation.PreDestroy;
import java.time.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceStockAdapter {

  private final RabbitMQAdapter rabbitMQAdapter;
  private final WebDriverConfig webDriverConfig;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  @EventListener(ApplicationReadyEvent.class)
  @Order(1)
  public void startStockPriceFetcher() {
    ZoneId zoneId = ZoneId.of(CommonConstant.ZONE_ID);
    scheduler.scheduleAtFixedRate(() -> {
      try {
        fetchStockPrice(zoneId);
      } catch (Exception e) {
        log.error("Error fetching stock prices", e);
        webDriverConfig.resetDriver();
      }
    }, 0, 3, TimeUnit.SECONDS);
  }

  @PreDestroy
  public void shutdown() {
    log.info("Shutting down PriceStockAdapter scheduler.");
    scheduler.shutdown();
    try {
      if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
        scheduler.shutdownNow();
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      scheduler.shutdownNow();
    }
    webDriverConfig.quitWebDriver();
  }

  private void fetchStockPrice(ZoneId zoneId) {
    if (!isMarketOpen(zoneId)) {
      webDriverConfig.quitWebDriver();
      return;
    }

    WebDriver webDriver = webDriverConfig.getWebDriver();
    try {
      webDriver.get(CommonConstant.PRICE_STOCK_URL);
      WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(2));

      Map<String, String> priceMap = getStockData(wait);
      if (!priceMap.isEmpty()) {
        sendToRabbitMQ(priceMap);
      }
    } catch (Exception e) {
      log.error("Error during stock price fetching: {}", e.getMessage(), e);
      webDriverConfig.resetDriver();
    }
  }

  private boolean isMarketOpen(ZoneId zoneId) {
    LocalTime marketOpen = LocalTime.of(9, 0);
    LocalTime morningClose = LocalTime.of(11, 30);
    LocalTime afternoonOpen = LocalTime.of(13, 0);
    LocalTime marketClose = LocalTime.of(15, 0);

    LocalDate today = LocalDate.now(zoneId);
    DayOfWeek dayOfWeek = today.getDayOfWeek();
    LocalTime currentTime = ZonedDateTime.now(zoneId).toLocalTime();

    return dayOfWeek != DayOfWeek.SATURDAY
            && dayOfWeek != DayOfWeek.SUNDAY
            && ((currentTime.isAfter(marketOpen) && currentTime.isBefore(morningClose))
            || (currentTime.isAfter(afternoonOpen) && currentTime.isBefore(marketClose)));
  }

  private Map<String, String> getStockData(WebDriverWait wait) {
    Map<String, String> stockData = new HashMap<>();
    try {
      List<WebElement> rowElements = wait.until(
              ExpectedConditions.presenceOfAllElementsLocatedBy(
                      By.cssSelector(CommonConstant.ROW_ELEMENT_VALUE)));

      for (WebElement row : rowElements) {
        try {
          WebElement symbolElement = row.findElement(By.cssSelector(CommonConstant.SYMBOL_ELEMENT_VALUE));
          String stockSymbol = symbolElement.getText().trim();
          stockSymbol = stockSymbol.replaceAll("[^A-Za-z]", "");

          WebElement priceElement = row.findElement(By.cssSelector(CommonConstant.PRICE_ELEMENT_VALUE));
          String stockPrice = priceElement.getText().trim();

          stockData.put(stockSymbol, stockPrice);
        } catch (NoSuchElementException e) {
          log.warn("Skipping row due to missing elements: {}", e.getMessage());
        }
      }
    } catch (TimeoutException e) {
      log.warn("Timeout waiting for stock data elements: {}", e.getMessage());
    }
    return stockData;
  }

  private void sendToRabbitMQ(Map<String, String> priceMap) {
    try {
      String jsonString = objectMapper.writeValueAsString(priceMap);
      rabbitMQAdapter.sendMessage(jsonString);
    } catch (JsonProcessingException e) {
      log.error("Error serializing stock data to JSON: {}", e.getMessage(), e);
    }
  }
}