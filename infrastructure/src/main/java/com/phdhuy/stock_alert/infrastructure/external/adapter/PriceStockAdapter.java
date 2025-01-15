package com.phdhuy.stock_alert.infrastructure.external.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.infrastructure.messagebroker.adapter.RabbitMQAdapter;
import com.phdhuy.stock_alert.shared.config.WebDriverConfig;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
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

  private final RabbitMQAdapter rabbitMQAdapter;

  private final WebDriverConfig webDriverConfig;

  @EventListener(ApplicationReadyEvent.class)
  public void getStockPrice() throws JsonProcessingException, InterruptedException {
    ZoneId zoneId = ZoneId.of(CommonConstant.ZONE_ID);
    LocalTime marketOpen = LocalTime.of(9, 0);
    LocalTime morningClose = LocalTime.of(11, 30);
    LocalTime afternoonOpen = LocalTime.of(13, 0);
    LocalTime marketClose = LocalTime.of(15, 0);

    while (true) {
      try {
        LocalDate today = LocalDate.now(zoneId);
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
          this.waitUntilNextWeekday(zoneId, marketOpen);
          continue;
        }

        WebDriver webDriver = webDriverConfig.getWebDriver();
        webDriver.get(CommonConstant.PRICE_STOCK);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(7));

        if (isMarketOpen(zoneId, marketOpen, morningClose)) {
          runMarketSession(wait, zoneId, marketOpen, morningClose);
        }

        if (this.isMarketInBreakTime(zoneId, morningClose, afternoonOpen)) {
          this.waitUntilNextMarketOpen(zoneId, afternoonOpen);
        }

        if (isMarketOpen(zoneId, afternoonOpen, marketClose)) {
          runMarketSession(wait, zoneId, afternoonOpen, marketClose);
        }
      } finally {
        webDriverConfig.quitWebDriver();

        if (ZonedDateTime.now(zoneId).toLocalTime().isAfter(marketClose)) {
          this.waitUntilNextMarketOpenOrWeekday(zoneId, marketOpen);
        }
      }
    }
  }

  private void runMarketSession(
      WebDriverWait wait, ZoneId zoneId, LocalTime sessionStart, LocalTime sessionEnd)
      throws JsonProcessingException, InterruptedException {
    while (isMarketOpen(zoneId, sessionStart, sessionEnd)) {
      Map<String, String> priceMap = this.getPriceStock(wait);
      if (!priceMap.isEmpty()) {
        sendToRabbitMQ(priceMap);
      }
      Thread.sleep(4000);
    }
  }

  private void waitUntilNextWeekday(ZoneId zoneId, LocalTime marketOpen)
      throws InterruptedException {
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    ZonedDateTime nextMarketOpen =
        now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).with(marketOpen);

    long sleepTime = Duration.between(now, nextMarketOpen).toMillis();
    log.info("Next weekday market open at {}", nextMarketOpen);
    Thread.sleep(sleepTime);
  }

  private void waitUntilNextMarketOpenOrWeekday(ZoneId zoneId, LocalTime marketOpen)
      throws InterruptedException {
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    DayOfWeek today = now.getDayOfWeek();

    if (today == DayOfWeek.FRIDAY) {
      waitUntilNextWeekday(zoneId, marketOpen);
      return;
    }

    ZonedDateTime nextMarketOpen = now.plusDays(1).with(marketOpen);
    long sleepTime = Duration.between(now, nextMarketOpen).toMillis();
    log.info("Waiting until next market open: {}", nextMarketOpen);
    Thread.sleep(sleepTime);
  }

  private boolean isMarketOpen(ZoneId zoneId, LocalTime sessionStart, LocalTime sessionEnd) {
    LocalTime currentTime = ZonedDateTime.now(zoneId).toLocalTime();
    return currentTime.isAfter(sessionStart) && currentTime.isBefore(sessionEnd);
  }

  private boolean isMarketInBreakTime(
      ZoneId zoneId, LocalTime morningClose, LocalTime afternoonOpen) {
    LocalTime currentTime = ZonedDateTime.now(zoneId).toLocalTime();
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

  private void waitUntilNextMarketOpen(ZoneId zoneId, LocalTime marketOpen)
      throws InterruptedException {
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    ZonedDateTime nextMarketOpen = now.with(marketOpen);

    if (now.toLocalTime().isAfter(marketOpen)) {
      nextMarketOpen = nextMarketOpen.plusDays(1);
    }
    long sleepTime = Duration.between(now, nextMarketOpen).toMillis();
    log.info("Waiting until next market open at {}, sleep time {}", nextMarketOpen, sleepTime);
    Thread.sleep(sleepTime);
  }
}
