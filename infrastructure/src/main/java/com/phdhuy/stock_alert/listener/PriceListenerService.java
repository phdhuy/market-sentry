package com.phdhuy.stock_alert.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.databases.influxdb.adapter.CreatePriceAssetAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceListenerService {

  private final CreatePriceAssetAdapter createPriceAssetAdapter;

  private final ObjectMapper objectMapper;

  @RabbitListener(queues = "price.database")
  public void receiveMessage(String message) {
    try {
      JsonNode rootNode = objectMapper.readTree(message);

      rootNode
          .fields()
          .forEachRemaining(
              entry -> {
                String cryptoName = entry.getKey();
                double price = entry.getValue().asDouble();
                createPriceAssetAdapter.createPriceAssetPort(cryptoName, cryptoName, price);
              });

    } catch (Exception e) {
      log.error("Error processing message:", e);
    }
  }
}
