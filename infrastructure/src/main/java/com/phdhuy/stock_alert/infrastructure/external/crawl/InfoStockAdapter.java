package com.phdhuy.stock_alert.infrastructure.external.crawl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AssetType;
import com.phdhuy.stock_alert.infrastructure.external.crawl.utils.ProcessAssetUtils;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InfoStockAdapter {

  private final OkHttpClient httpClient;

  private final ProcessAssetUtils processCryptoAssets;

  private final ObjectMapper objectMapper;

  @Scheduled(cron = "0 0 17 * * *")
  public void crawlDataStockAndSaveToDB() {
    try {
      List<Asset> assetList = this.crawlDataStock();
      processCryptoAssets.processCryptoAssets(assetList);
    } catch (Exception e) {
      log.error("Error while crawling crypto data: {}", e.getMessage(), e);
    }
  }

  public List<Asset> crawlDataStock() throws IOException {
    List<Asset> assetList = new ArrayList<>();
    Request request = new Request.Builder().url(CommonConstant.INFO_STOCK_VN).build();
    Response response = httpClient.newCall(request).execute();
    String responseBody = response.body().string();
    JsonNode root = objectMapper.readTree(responseBody);
    JsonNode dataNode = root.path("data");
    for (JsonNode cryptoNode : dataNode) {
      assetList.add(this.convertToStock(cryptoNode));
    }
    return assetList;
  }

  private Asset convertToStock(JsonNode stockNode) {
    return Asset.builder()
        .identity(getSafeText(stockNode, "code"))
        .symbol(getSafeText(stockNode, "code"))
        .name(getSafeText(stockNode, "companyNameEng"))
        .logo(CommonConstant.IMAGE_STOCK_VN + getSafeText(stockNode, "code"))
        .assetType(String.valueOf(AssetType.STOCK))
        .build();
  }

  private String getSafeText(JsonNode node, String key) {
    JsonNode fieldNode = node.get(key);
    return fieldNode != null ? fieldNode.asText() : "";
  }
}
