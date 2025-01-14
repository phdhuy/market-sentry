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
        assetList.add(this.convertToAsset(cryptoNode));
      }
    } catch (IOException e) {
      e.printStackTrace();
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
