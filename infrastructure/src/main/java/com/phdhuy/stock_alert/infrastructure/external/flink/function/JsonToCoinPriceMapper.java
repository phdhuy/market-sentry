package com.phdhuy.stock_alert.infrastructure.external.flink.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.infrastructure.external.flink.model.AssetPrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.MapFunction;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonToCoinPriceMapper implements MapFunction<String, AssetPrice> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public AssetPrice map(String json) {
    try {
      JsonNode rawNode = objectMapper.readTree(json);

      if (rawNode.isTextual()) {
        rawNode = objectMapper.readTree(rawNode.asText());
      }

      AssetPrice assetPrice = new AssetPrice();
      assetPrice.setPrices(rawNode);
      return assetPrice;
    } catch (Exception e) {
      log.error("Error parsing JSON: {}", json, e);
      return null;
    }
  }
}
