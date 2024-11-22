package com.phdhuy.stock_alert.infrastructure.external.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.domain.messagebroker.RabbitMQPort;
import com.phdhuy.stock_alert.shared.config.WebDriverConfig;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class PriceStockAdapter extends TextWebSocketHandler {

  private final RabbitMQPort rabbitMQPort;

  private final WebDriverConfig webDriverConfig;

  @EventListener(ApplicationReadyEvent.class)
  public void getStockPrice()
      throws JsonProcessingException, InterruptedException, MalformedURLException {
    ZoneId zoneId = ZoneId.of(CommonConstant.ZONE_ID);
    LocalTime morningOpen = LocalTime.of(9, 0);
    LocalTime morningClose = LocalTime.of(11, 30);
    LocalTime afternoonOpen = LocalTime.of(13, 0);
    LocalTime marketClose = LocalTime.of(15, 0);

    while (true) {
      try {
        WebDriver webDriver = webDriverConfig.getWebDriver();
        webDriver.get(CommonConstant.PRICE_STOCK);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(7));

        while (isMarketOpen(zoneId, morningOpen, morningClose)
            || isMarketOpen(zoneId, afternoonOpen, marketClose)) {
          Map<String, String> priceMap = this.getPriceStock(wait);
          if (!priceMap.isEmpty()) {
            sendToRabbitMQ(priceMap);
          }
          Thread.sleep(4000);
        }
      }
      finally {
        webDriverConfig.quitWebDriver();
        if (this.isMarketInBreakTime(zoneId, morningClose, afternoonOpen)) {
          this.waitUntilNextMarketOpen(zoneId, afternoonOpen);
        } else {
          this.waitUntilNextMarketOpen(zoneId, morningOpen);
        }
      }
    }
  }

  private boolean isMarketOpen(ZoneId zoneId, LocalTime marketOpen, LocalTime marketClose) {
    LocalTime currentTime = ZonedDateTime.now(zoneId).toLocalTime();
    return currentTime.isAfter(marketOpen) && currentTime.isBefore(marketClose);
  }

  private boolean isMarketInBreakTime(
      ZoneId zoneId, LocalTime morningClose, LocalTime afternoonOpen) {
    LocalTime currentTime = ZonedDateTime.now(zoneId).toLocalTime();
    log.info("Current time (break time): {}", currentTime);
    return currentTime.isAfter(morningClose) && currentTime.isBefore(afternoonOpen);
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
    } catch (TimeoutException e) {
      log.error("Element not found within the specified timeout.");
    }
    return priceMap;
  }

  private void sendToRabbitMQ(Map<String, String> priceMap) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(priceMap);
    rabbitMQPort.sendMessage(jsonString);
  }

  private void waitUntilNextMarketOpen(ZoneId zoneId, LocalTime marketOpen)
      throws InterruptedException {
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    ZonedDateTime nextMarketOpen = now.with(marketOpen);

    if (now.toLocalTime().isAfter(marketOpen)) {
      nextMarketOpen = nextMarketOpen.plusDays(1);
    }
    log.info("Next market open at {}", nextMarketOpen);
    long sleepTime = Duration.between(now, nextMarketOpen).toMillis();
    log.info("Sleep time {}", sleepTime);
    Thread.sleep(sleepTime);
    log.info("Waiting until next market open at {}, sleep time {}", nextMarketOpen, sleepTime);
  }
}
