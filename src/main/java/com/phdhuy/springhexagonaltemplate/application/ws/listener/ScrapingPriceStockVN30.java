package com.phdhuy.springhexagonaltemplate.application.ws.listener;

import com.phdhuy.springhexagonaltemplate.application.ws.handler.PriceStockVN30Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScrapingPriceStockVN30 extends TextWebSocketHandler {

  private final List<String> stockOfVN30 =
      Arrays.asList(
          "ACB", "BCM", "BID", "BVH", "CTG", "FPT", "GAS", "GVR", "HDB", "HPG", "MBB", "MSN", "MWG",
          "PLX", "POW", "SAB", "SHB", "SSB", "SSI", "STB", "TCB", "TPB", "VCB", "VHM", "VIB", "VIC",
          "VJC", "VNM", "VPB", "VRE");

  private final WebDriver webDriver;

  private final PriceStockVN30Handler priceStockVN30Handler;

  @Scheduled(cron = "*/30 * 2-8 * * MON-FRI")
  public List<String> getStockPrice() throws Exception {
    List<String> prices = new ArrayList<>();

    webDriver.get("https://banggia.vps.com.vn/chung-khoan/VN30");

    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    for (String stock : stockOfVN30) {
      WebElement priceElement =
          wait.until(ExpectedConditions.presenceOfElementLocated(By.id(stock + "_lastPrice_value")));

      String price = priceElement.getText();
      prices.add(stock + " " + price);
      priceStockVN30Handler.handleTextMessage(stock + " " + price);
    }
    return prices;
  }
}
