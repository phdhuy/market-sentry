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
    WebDriver webDriver = webDriverConfig.getWebDriver();
    webDriver.get(CommonConstant.PRICE_STOCK);
    ZoneId zoneId = ZoneId.of("Asia/Bangkok");
    LocalTime marketOpen = LocalTime.of(9, 0);
    LocalTime marketClose = LocalTime.of(16, 0);
    while (true) {
      WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(7));
      Map<String, String> priceMap = new HashMap<>();
      List<WebElement> priceElements = new ArrayList<>();
      try {
        priceElements =
            wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath(CommonConstant.LAST_PRICE_VALUE_XPATH)));
      } catch (TimeoutException e) {
        log.error("Element not found within the specified timeout.");
      }
      for (WebElement stock : priceElements) {
        String id = stock.getAttribute("id").substring(0, 3);
        String price = stock.getText();
        priceMap.put(id, price);
      }
      LocalTime currentTimeInUTC7 = ZonedDateTime.now(zoneId).toLocalTime();
      if (currentTimeInUTC7.isAfter(marketOpen) && currentTimeInUTC7.isBefore(marketClose)) {
        log.info("time {}", currentTimeInUTC7);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(priceMap);
        rabbitMQPort.sendMessage(jsonString);
      }
      Thread.sleep(4000);
    }
  }
}
