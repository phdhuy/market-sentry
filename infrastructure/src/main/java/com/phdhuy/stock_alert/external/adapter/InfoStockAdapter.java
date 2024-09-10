package com.phdhuy.stock_alert.external.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.databases.postgresql.entity.enums.AssetType;
import com.phdhuy.stock_alert.external.constant.ExternalAPIConstant;
import com.phdhuy.stock_alert.model.Asset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InfoStockAdapter {

  private final OkHttpClient httpClient;

  private final ObjectMapper objectMapper;

  public List<Asset> crawlDataStock() throws IOException {
    List<Asset> assetList = new ArrayList<>();
    Request request = new Request.Builder().url(ExternalAPIConstant.INFO_STOCK_VN).build();
    Response response = httpClient.newCall(request).execute();
    String responseBody = response.body().string();
    JsonNode root = objectMapper.readTree(responseBody);
    JsonNode dataNode = root.path("data");
    log.info("dataNode: {}", dataNode);
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
        .nameVn(getSafeText(stockNode, "companyName"))
        .floor(getSafeText(stockNode, "floor"))
        .logo(ExternalAPIConstant.IMAGE_STOCK_VN + getSafeText(stockNode, "code"))
        .assetType(String.valueOf(AssetType.STOCK))
        .build();
  }
  private String getSafeText(JsonNode node, String key) {
    JsonNode fieldNode = node.get(key);
    return fieldNode != null ? fieldNode.asText() : "";
  }
}
