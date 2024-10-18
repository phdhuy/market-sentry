package com.phdhuy.stock_alert.infrastructure.databases.influxdb.adapter;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.GetLatestPriceAssetPort;
import com.phdhuy.stock_alert.infrastructure.databases.influxdb.utils.FluxQueryUtils;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PersistenceAdapter
@RequiredArgsConstructor
@Slf4j
public class GetLatestPriceAssetAdapter implements GetLatestPriceAssetPort {

  private final InfluxDBClient influxDBClient;

  @Override
  public Double getLatestPriceAsset(String symbol) {
    QueryApi queryApi = influxDBClient.getQueryApi();
    String fluxQuery = FluxQueryUtils.getLatestPriceAsset(symbol);

    List<FluxTable> tables = queryApi.query(fluxQuery);

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

    String query = FluxQueryUtils.getLatestPriceAssets(symbols);
    QueryApi queryApi = influxDBClient.getQueryApi();
    List<FluxTable> tables = queryApi.query(query);

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
}
