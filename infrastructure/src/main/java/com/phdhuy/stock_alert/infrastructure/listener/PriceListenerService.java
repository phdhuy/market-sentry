package com.phdhuy.stock_alert.infrastructure.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.infrastructure.databases.influxdb.adapter.PriceAssetRepositoryAdapter;
import com.phdhuy.stock_alert.shared.handler.PriceWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceListenerService {

  private final PriceAssetRepositoryAdapter priceAssetRepositoryAdapter;

  private final PriceWebSocketHandler priceWebSocketHandler;

  private final ObjectMapper objectMapper;

  @RabbitListener(queues = "price_websocket")
  public void receiveMessage(String message) {
    try {
      JsonNode rootNode = objectMapper.readTree(message);
      priceWebSocketHandler.handleTextMessage(rootNode.toString());
      rootNode
          .fields()
          .forEachRemaining(
              entry -> {
                String assetSymbol = entry.getKey();
                double assetPrice = entry.getValue().asDouble();
                priceAssetRepositoryAdapter.createPriceAsset(assetSymbol, assetSymbol, assetPrice);
              });
    } catch (Exception e) {
      log.error("Error processing message:", e);
    }
  }
}
