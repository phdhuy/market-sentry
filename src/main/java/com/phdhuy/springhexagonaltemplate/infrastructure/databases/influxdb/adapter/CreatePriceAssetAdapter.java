package com.phdhuy.springhexagonaltemplate.infrastructure.databases.influxdb.adapter;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.phdhuy.springhexagonaltemplate.infrastructure.databases.influxdb.entity.PriceAssetEntity;
import com.phdhuy.springhexagonaltemplate.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreatePriceAssetAdapter {

  private final InfluxDBClient influxDBClient;

  public void createPriceAssetPort(String name, String symbol, double price) {
    PriceAssetEntity priceAssetEntity = new PriceAssetEntity();

    priceAssetEntity.setName(name);
    priceAssetEntity.setSymbol(symbol);
    priceAssetEntity.setPrice(price);
    priceAssetEntity.setTime(Instant.now());

    influxDBClient.getWriteApiBlocking().writeMeasurement(WritePrecision.MS, priceAssetEntity);
  }
}
