package com.phdhuy.stock_alert.job;

import com.phdhuy.stock_alert.constant.MessageConstant;
import com.phdhuy.stock_alert.databases.postgresql.entity.StockEntity;
import com.phdhuy.stock_alert.databases.postgresql.repository.StockRepository;
import com.phdhuy.stock_alert.exception.NotFoundException;
import com.phdhuy.stock_alert.external.constant.ExternalAPIConstant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockInfoSyncJob {

  private final WebDriver webDriver;

  private final StockRepository stockRepository;

  public void scheduledStockInfoProcessing() {
    webDriver.get(ExternalAPIConstant.PRICE_STOCK);

    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

    List<WebElement> priceElements =
            wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.xpath("//*[contains(@id, '_lastPrice_value')]")));
    List<String> symbolList = new ArrayList<>();
    for(WebElement webElement : priceElements) {
      String symbol = webElement.getAttribute("id").substring(0, 3);
      symbolList.add(symbol);
    }
    
    for (String symbol : symbolList) {
      this.scrapingStockInfo(symbol);
    }
  }

  private void scrapingStockInfo(String symbol) {
    webDriver.get(ExternalAPIConstant.INFO_STOCK_VN + symbol + ExternalAPIConstant.LANGUAGE);
    WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

    WebElement h1Element =
        webDriverWait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1.h1-title")));
    WebElement bElement = h1Element.findElement(By.tagName("b"));
    String title = bElement.getText().trim();

    WebElement sectorDiv =
        webDriverWait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.sector-level")));

    List<WebElement> industryElements =
        sectorDiv.findElements(By.cssSelector("a.title-link.text-bold"));

    String openPrice =
        webDriverWait
            .until(ExpectedConditions.presenceOfElementLocated(By.id("openprice")))
            .getText();
    String lowestPrice =
        webDriverWait
            .until(ExpectedConditions.presenceOfElementLocated(By.id("lowestprice")))
            .getText();
    String highestPrice =
        webDriverWait
            .until(ExpectedConditions.presenceOfElementLocated(By.id("highestprice")))
            .getText();
    String totalVol =
        webDriverWait
            .until(ExpectedConditions.presenceOfElementLocated(By.id("totalvol")))
            .getText();
    String tradingStatus =
        webDriverWait
            .until(ExpectedConditions.presenceOfElementLocated(By.id("tradingstatus")))
            .getText();
    this.saveInfoStockToDB(
        title,
        symbol,
        industryElements.get(0).getText(),
        tradingStatus,
        openPrice,
        lowestPrice,
        highestPrice,
        totalVol);
  }

  private void saveInfoStockToDB(
      String title,
      String symbol,
      String industry,
      String tradingStatus,
      String openPrice,
      String lowestPrice,
      String highestPrice,
      String totalVol) {
    if (!stockRepository.existsBySymbol(symbol)) {
      StockEntity stockEntity = new StockEntity();
      save(
          title,
          symbol,
          industry,
          tradingStatus,
          openPrice,
          lowestPrice,
          highestPrice,
          totalVol,
          stockEntity);
    } else {
      StockEntity stockEntity =
          stockRepository
              .findBySymbol(symbol)
              .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));
      save(
          title,
          symbol,
          industry,
          tradingStatus,
          openPrice,
          lowestPrice,
          highestPrice,
          totalVol,
          stockEntity);
    }
  }

  private void save(
      String title,
      String symbol,
      String industry,
      String tradingStatus,
      String openPrice,
      String lowestPrice,
      String highestPrice,
      String totalVol,
      StockEntity stockEntity) {
    stockEntity.setTitle(title);
    stockEntity.setSymbol(symbol);
    stockEntity.setIndustry(industry);
    stockEntity.setHighestPrice(highestPrice);
    stockEntity.setLowestPrice(lowestPrice);
    stockEntity.setOpenPrice(openPrice);
    stockEntity.setVolume(totalVol);
    stockEntity.setTradingStatus(tradingStatus);
    stockRepository.save(stockEntity);
  }
}
