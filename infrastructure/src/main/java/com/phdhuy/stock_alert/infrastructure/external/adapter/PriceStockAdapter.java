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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class PriceStockAdapter extends TextWebSocketHandler {

  private final RabbitMQPort rabbitMQPort;

  private final WebDriverConfig webDriverConfig;

  @Scheduled(fixedRate = 30000)
  public void getStockPrice() throws JsonProcessingException, MalformedURLException {
    if (isWithinMarketHours()) {
      WebDriver webDriver = webDriverConfig.initializeWebDriver();
      webDriver.get(CommonConstant.PRICE_STOCK);
      WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

      Map<String, String> priceMap = new HashMap<>();
      List<WebElement> priceElements =
          wait.until(
              ExpectedConditions.presenceOfAllElementsLocatedBy(
                  By.xpath(CommonConstant.LAST_PRICE_VALUE_XPATH)));

      for (WebElement stock : priceElements) {
        String id = stock.getAttribute("id").substring(0, 3);
        String price = stock.getText();
        priceMap.put(id, price);
      }
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(priceMap);
      rabbitMQPort.sendMessage(jsonString);

      webDriver.quit();
    }
  }

  private boolean isWithinMarketHours() {
    LocalTime now = LocalTime.now(ZoneId.of(CommonConstant.ZONE_ID));
    LocalTime marketOpen = LocalTime.of(9, 0);
    LocalTime marketClose = LocalTime.of(16, 0);
    return now.isAfter(marketOpen) && now.isBefore(marketClose);
  }
}
