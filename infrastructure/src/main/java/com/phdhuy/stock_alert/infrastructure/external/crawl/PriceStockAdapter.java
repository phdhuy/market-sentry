package com.phdhuy.stock_alert.infrastructure.external.crawl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.infrastructure.external.messagebroker.RabbitMQAdapter;
import com.phdhuy.stock_alert.shared.config.WebDriverConfig;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import java.time.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PriceStockAdapter {

  private final RabbitMQAdapter rabbitMQAdapter;
  private final WebDriverConfig webDriverConfig;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @EventListener(ApplicationReadyEvent.class)
  @Order(1)
  public void startStockPriceFetcher() {
    ZoneId zoneId = ZoneId.of(CommonConstant.ZONE_ID);

    scheduler.scheduleAtFixedRate(
        () -> {
          try {
            fetchStockPrice(zoneId);
          } catch (Exception e) {
            log.error("Error fetching stock prices", e);
          }
        },
        0,
        3,
        TimeUnit.SECONDS);
  }

  private void fetchStockPrice(ZoneId zoneId) throws JsonProcessingException {
    LocalTime marketOpen = LocalTime.of(9, 0);
    LocalTime morningClose = LocalTime.of(11, 30);
    LocalTime afternoonOpen = LocalTime.of(13, 0);
    LocalTime marketClose = LocalTime.of(15, 0);

    LocalDate today = LocalDate.now(zoneId);
    DayOfWeek dayOfWeek = today.getDayOfWeek();
    LocalTime currentTime = ZonedDateTime.now(zoneId).toLocalTime();

    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
      webDriverConfig.quitWebDriver();
      return;
    }

    if (currentTime.isAfter(marketClose)) {
      webDriverConfig.quitWebDriver();
      return;
    }

    if (currentTime.isAfter(morningClose) && currentTime.isBefore(afternoonOpen)) {
      webDriverConfig.quitWebDriver();
      return;
    }

    if (currentTime.isBefore(marketOpen)) {
      webDriverConfig.quitWebDriver();
      return;
    }

    WebDriver webDriver = webDriverConfig.getWebDriver();
    try {
      webDriver.get(CommonConstant.PRICE_STOCK);
      WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

      Map<String, String> priceMap = getStockData(wait);
      if (!priceMap.isEmpty()) {
        sendToRabbitMQ(priceMap);
      }
    } finally {
      webDriverConfig.quitWebDriver();
    }
  }

  private Map<String, String> getStockData(WebDriverWait wait) {
    Map<String, String> stockData = new HashMap<>();
    try {
      List<WebElement> rowElements =
          wait.until(
              ExpectedConditions.presenceOfAllElementsLocatedBy(
                  By.cssSelector(CommonConstant.ROW_ELEMENT_VALUE)));

      for (WebElement row : rowElements) {
        WebElement symbolElement =
            row.findElement(By.cssSelector(CommonConstant.SYMBOL_ELEMENT_VALUE));
        String stockSymbol = symbolElement.getText().trim();

        WebElement matchedPriceElement =
            row.findElement(By.cssSelector(CommonConstant.PRICE_ELEMENT_VALUE));
        String matchedPrice = matchedPriceElement.getText().trim();

        stockData.put(stockSymbol, matchedPrice);
      }
    } catch (Exception e) {
      log.error("Failed to scrape stock data: {}", e.getMessage(), e);
    }
    return stockData;
  }

  private void sendToRabbitMQ(Map<String, String> priceMap) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(priceMap);
    rabbitMQAdapter.sendMessage(jsonString);
  }
}
