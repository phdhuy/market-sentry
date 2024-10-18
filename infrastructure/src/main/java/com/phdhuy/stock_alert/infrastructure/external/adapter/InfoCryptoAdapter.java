package com.phdhuy.stock_alert.infrastructure.external.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AssetType;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InfoCryptoAdapter {

  private final OkHttpClient httpClient;

  private final ObjectMapper objectMapper;

  public List<Asset> crawlDataCrypto() {
    List<Asset> assetList = new ArrayList<>();
    Request request = new Request.Builder().url(CommonConstant.INFO_CRYPTO).build();
    try {
      Response response = httpClient.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }
      String responseBody = response.body().string();
      JsonNode root = objectMapper.readTree(responseBody);
      JsonNode dataNode = root.path("data");
      for (JsonNode cryptoNode : dataNode) {
        assetList.add(this.convertToCrypto(cryptoNode));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return assetList;
  }

  private Asset convertToCrypto(JsonNode cryptoNode) {
    return Asset.builder()
        .identity(cryptoNode.get("id").asText())
        .rank(cryptoNode.get("rank").asLong())
        .symbol(cryptoNode.get("symbol").asText())
        .name(cryptoNode.get("name").asText())
        .supply(cryptoNode.get("supply").asDouble())
        .maxSupply(cryptoNode.get("maxSupply").asDouble())
        .marketCapUsd(cryptoNode.get("marketCapUsd").asDouble())
        .volumeUsd24Hr(cryptoNode.get("volumeUsd24Hr").asDouble())
        .changePercent24Hr(cryptoNode.get("changePercent24Hr").asDouble())
        .vwap24Hr(cryptoNode.get("vwap24Hr").asDouble())
        .explorer(cryptoNode.get("explorer").asText())
        .assetType(String.valueOf(AssetType.CRYPTO))
        .build();
  }
}
