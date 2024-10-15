package com.phdhuy.stock_alert.infrastructure.external.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AssetType;
import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
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
    Request request = new Request.Builder().url(CommonConstant.INFO_STOCK_VN).build();
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
        .logo(CommonConstant.IMAGE_STOCK_VN + getSafeText(stockNode, "code"))
        .assetType(String.valueOf(AssetType.STOCK))
        .build();
  }

  private String getSafeText(JsonNode node, String key) {
    JsonNode fieldNode = node.get(key);
    return fieldNode != null ? fieldNode.asText() : "";
  }
}
