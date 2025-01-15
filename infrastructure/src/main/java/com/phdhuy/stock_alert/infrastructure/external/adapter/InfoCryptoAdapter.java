package com.phdhuy.stock_alert.infrastructure.external.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AssetType;
import com.phdhuy.stock_alert.infrastructure.external.utils.ProcessAssetUtils;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfoCryptoAdapter {

  private final OkHttpClient httpClient;

  private final ObjectMapper objectMapper;

  private final ProcessAssetUtils processCryptoAssets;

  @Scheduled(cron = "0 0 17 * * *")
  public void crawlDataCryptoAndSaveToDB() {
    try {
      List<Asset> assetList = this.crawlDataCrypto();
      processCryptoAssets.processCryptoAssets(assetList);
    } catch (Exception e) {
      log.error("Error while crawling crypto data: {}", e.getMessage(), e);
    }
  }

  private List<Asset> crawlDataCrypto() {
    List<Asset> assetList = new ArrayList<>();
    Request request = new Request.Builder().url(CommonConstant.INFO_CRYPTO).build();
    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected HTTP response: " + response);
      }
      String responseBody = response.body().string();
      JsonNode root = objectMapper.readTree(responseBody);
      JsonNode dataNode = root.path("data");
      for (JsonNode cryptoNode : dataNode) {
        assetList.add(convertToAsset(cryptoNode));
      }
    } catch (IOException e) {
      log.error("Error during HTTP request or data parsing: {}", e.getMessage(), e);
    }
    return assetList;
  }

  private Asset convertToAsset(JsonNode cryptoNode) {
    return Asset.builder()
        .identity(cryptoNode.get("id").asText())
        .symbol(cryptoNode.get("symbol").asText())
        .name(cryptoNode.get("name").asText())
        .explorer(cryptoNode.get("explorer").asText())
        .assetType(String.valueOf(AssetType.CRYPTO))
        .build();
  }
}
