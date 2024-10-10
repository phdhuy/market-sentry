package com.phdhuy.stock_alert.infrastructure.databases.influxdb.adapter;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.infrastructure.databases.influxdb.entity.PriceAssetEntity;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreatePriceAssetAdapter {

  private final InfluxDBClient influxDBClient;

  public void createPriceAsset(String name, String symbol, double price) {
    PriceAssetEntity priceAssetEntity = new PriceAssetEntity();

    priceAssetEntity.setName(name);
    priceAssetEntity.setSymbol(symbol);
    priceAssetEntity.setPrice(price);
    priceAssetEntity.setTime(Instant.now());

    influxDBClient.getWriteApiBlocking().writeMeasurement(WritePrecision.MS, priceAssetEntity);
  }
}
