package com.phdhuy.stock_alert.infrastructure.external.adapter;

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
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PriceStockAdapter {

  private final RabbitMQAdapter rabbitMQAdapter;
  private final WebDriverConfig webDriverConfig;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @EventListener(ApplicationReadyEvent.class)
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

    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
      log.info("Market closed on weekends.");
      return;
    }

    LocalTime currentTime = ZonedDateTime.now(zoneId).toLocalTime();

    if (currentTime.isAfter(marketClose)) {
      log.info("Market closed for today.");
      return;
    }

    if ((currentTime.isAfter(morningClose) && currentTime.isBefore(afternoonOpen))) {
      log.info("Market is in break time.");
      return;
    }

    if (currentTime.isBefore(marketOpen)) {
      log.info("Waiting for market to open...");
      return;
    }

    log.info("Fetching stock prices...");
    WebDriver webDriver = webDriverConfig.getWebDriver();
    try {
      webDriver.get(CommonConstant.PRICE_STOCK);
      WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(7));

      Map<String, String> priceMap = getPriceStock(wait);
      if (!priceMap.isEmpty()) {
        sendToRabbitMQ(priceMap);
      }
    } finally {
      webDriverConfig.quitWebDriver();
    }
  }

  private Map<String, String> getPriceStock(WebDriverWait wait) {
    Map<String, String> priceMap = new HashMap<>();
    try {
      List<WebElement> priceElements =
          wait.until(
              ExpectedConditions.visibilityOfAllElementsLocatedBy(
                  By.xpath(CommonConstant.LAST_PRICE_VALUE_XPATH)));
      for (WebElement stock : priceElements) {
        String id = stock.getAttribute("id").substring(0, 3);
        String price = stock.getText();
        priceMap.put(id, price);
      }
    } catch (TimeoutException ignored) {
      // do nothing
    }
    return priceMap;
  }

  private void sendToRabbitMQ(Map<String, String> priceMap) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(priceMap);
    rabbitMQAdapter.sendMessage(jsonString);
  }
}
