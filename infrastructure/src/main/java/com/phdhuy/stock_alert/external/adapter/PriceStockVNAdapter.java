package com.phdhuy.stock_alert.external.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.external.constant.ExternalAPIConstant;
import com.phdhuy.stock_alert.ports.outbound.messagebroker.RabbitMQPort;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class PriceStockVNAdapter extends TextWebSocketHandler {

  private final WebDriver webDriver;

  private final RabbitMQPort rabbitMQPort;

  @EventListener(ApplicationReadyEvent.class)
  public void getStockPrice() throws JsonProcessingException {
    webDriver.get(ExternalAPIConstant.PRICE_STOCK);

    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
    while (true) {
      Map<String, String> priceMap = new HashMap<>();
      List<WebElement> priceElements =
          wait.until(
              ExpectedConditions.presenceOfAllElementsLocatedBy(
                  By.xpath("//*[contains(@id, '_lastPrice_value')]")));

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
