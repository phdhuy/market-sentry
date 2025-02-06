package com.phdhuy.stock_alert.infrastructure.databases.influxdb.adapter;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.model.PriceAsset;
import com.phdhuy.stock_alert.domain.asset.port.outbound.PriceAssetRepositoryPort;
import com.phdhuy.stock_alert.infrastructure.databases.influxdb.entity.PriceAssetEntity;
import com.phdhuy.stock_alert.infrastructure.databases.influxdb.repository.PriceAssetRepository;
import com.phdhuy.stock_alert.infrastructure.mapper.PriceAssetMapper;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class PriceAssetRepositoryAdapter implements PriceAssetRepositoryPort {

  private final InfluxDBClient influxDBClient;

  private final PriceAssetRepository priceAssetRepository;

  private final PriceAssetMapper priceAssetMapper;

  @Override
  public Double getLatestPriceAsset(String symbol) {
    List<FluxTable> tables = priceAssetRepository.getLatestPriceAsset(symbol);

    if (tables.isEmpty()) {
      return 0.0;
    }

    FluxTable table = tables.get(0);
    if (table.getRecords().isEmpty()) {
      return 0.0;
    }

    FluxRecord fluxRecord = table.getRecords().get(0);
    return fluxRecord.getValueByKey(CommonConstant.VALUE) != null
        ? ((Number) Objects.requireNonNull(fluxRecord.getValueByKey(CommonConstant.VALUE)))
            .doubleValue()
        : 0.0;
  }

  @Override
  public HashMap<String, Double> getLatestPriceAssets(List<String> symbols) {
    HashMap<String, Double> latestPrices = new HashMap<>();
    List<FluxTable> tables = priceAssetRepository.getLatestPriceAssets(symbols);

    for (FluxTable table : tables) {
      for (FluxRecord fluxRecord : table.getRecords()) {
        String symbol = (String) fluxRecord.getValueByKey("symbol");
        Double price =
            ((Double) Objects.requireNonNull(fluxRecord.getValueByKey(CommonConstant.VALUE)));
        latestPrices.put(symbol, price);
      }
    }

    return latestPrices;
  }

  @Override
  public List<PriceAsset> getPriceHistoryAsset(Asset asset, String interval) {
    List<FluxTable> tables =
        priceAssetRepository.getPriceHistoryAsset(asset.getIdentity(), interval);
    List<PriceAsset> priceAssets = new ArrayList<>();
    for (FluxTable table : tables) {
      for (FluxRecord fluxRecord : table.getRecords()) {
        priceAssets.add(
            priceAssetMapper.toPriceAsset(
                (Double) fluxRecord.getValueByKey(CommonConstant.VALUE),
                Timestamp.from(Objects.requireNonNull(fluxRecord.getTime()))));
      }
    }

    return priceAssets;
  }

  public void createPriceAsset(String name, String symbol, double price) {
    PriceAssetEntity priceAssetEntity = new PriceAssetEntity();
    priceAssetEntity.setName(name);
    priceAssetEntity.setSymbol(symbol);
    priceAssetEntity.setPrice(price);
    priceAssetEntity.setTime(Instant.now());

    influxDBClient.getWriteApiBlocking().writeMeasurement(WritePrecision.MS, priceAssetEntity);
  }
}
