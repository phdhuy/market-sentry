package com.phdhuy.stock_alert.infrastructure.external.flink.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

@Data
public class AssetPrice implements Serializable {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private JsonNode prices;

  public AssetPrice() {}

  public JsonNode getPrices() {
    return prices;
  }

  public void setPrices(JsonNode prices) {
    this.prices = prices;
  }

  public Map<String, Double> getPricesAsMap() {
    if (prices == null || prices.isEmpty()) {
      return Collections.emptyMap();
    }
    try {
      return OBJECT_MAPPER.convertValue(prices, new TypeReference<Map<String, Double>>() {});
    } catch (Exception e) {
      return Collections.emptyMap();
    }
  }
}
