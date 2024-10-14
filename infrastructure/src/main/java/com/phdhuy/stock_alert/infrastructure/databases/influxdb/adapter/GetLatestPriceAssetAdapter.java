package com.phdhuy.stock_alert.infrastructure.databases.influxdb.adapter;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.GetLatestPriceAssetPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
@Slf4j
public class GetLatestPriceAssetAdapter implements GetLatestPriceAssetPort {

  private final InfluxDBClient influxDBClient;

  public static final String VALUE = "_value";

  @Override
  public Double getLatestPriceAsset(String symbol) {
    QueryApi queryApi = influxDBClient.getQueryApi();
    String fluxQuery = buildFluxQuery(symbol);

    List<FluxTable> tables = queryApi.query(fluxQuery);

    if (tables.isEmpty()) {
      return 0.0;
    }

    FluxTable table = tables.get(0);
    if (table.getRecords().isEmpty()) {
      return 0.0;
    }

    FluxRecord fluxRecord = table.getRecords().get(0);
    return fluxRecord.getValueByKey(VALUE) != null
        ? ((Number) Objects.requireNonNull(fluxRecord.getValueByKey(VALUE))).doubleValue()
        : 0.0;
  }

  @Override
  public HashMap<String, Double> getLatestPriceAssets(List<String> symbols) {
    HashMap<String, Double> latestPrices = new HashMap<>();

    String query = buildFluxQuery(symbols);
    QueryApi queryApi = influxDBClient.getQueryApi();
    List<FluxTable> tables = queryApi.query(query);

    for (FluxTable table : tables) {
      for (FluxRecord fluxRecord : table.getRecords()) {
        String symbol = (String) fluxRecord.getValueByKey("symbol");
        Double price = ((Double) Objects.requireNonNull(fluxRecord.getValueByKey(VALUE)));
        latestPrices.put(symbol, price);
      }
    }

    return latestPrices;
  }

  private String buildFluxQuery(String symbol) {
    return String.format(
        "from(bucket: \"stock-alert\") "
            + "|> range(start: -1y) "
            + "|> filter(fn: (r) => r._measurement == \"price_asset\" and r.symbol == \"%s\") "
            + "|> sort(columns: [\"_time\"], desc: true) "
            + "|> limit(n:1)",
        symbol);
  }

  private String buildFluxQuery(List<String> symbols) {
    String filterCondition =
        symbols.stream()
            .map(symbol -> String.format("r.symbol == \"%s\"", symbol))
            .collect(Collectors.joining(" or "));

    return String.format(
        "from(bucket: \"stock-alert\") "
            + "|> range(start: -1w) "
            + "|> filter(fn: (r) => r._measurement == \"price_asset\" and (%s)) "
            + "|> group(columns: [\"symbol\"]) "
            + "|> last(column: \"_time\")",
        filterCondition);
  }
}
