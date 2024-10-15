package com.phdhuy.stock_alert.infrastructure.external.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.domain.messagebroker.RabbitMQPort;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  private final WebDriver webDriver;

  private final RabbitMQPort rabbitMQPort;

  @EventListener(ApplicationReadyEvent.class)
  public void getStockPrice() throws JsonProcessingException {
    webDriver.get(CommonConstant.PRICE_STOCK);

    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
    while (true) {
      if (!isMarketClosed(wait)) {
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
      }
    }
  }

  public boolean isMarketClosed(WebDriverWait wait) {
    wait.until(
        ExpectedConditions.presenceOfElementLocated(
            By.className(CommonConstant.MARKET_STATUS_CLASS_NAME)));
    List<WebElement> footerDivs =
        webDriver.findElements(By.xpath(CommonConstant.MARKET_STATUS_XPATH));
    for (WebElement div : footerDivs) {
      if (div.getText().contains(CommonConstant.MARKET_STATUS_IS_CLOSED)
          || div.getText().contains(CommonConstant.MARKET_STATUS_IS_BREAK)) {
        return true;
      }
    }
    return false;
  }
}
