package com.phdhuy.stock_alert.infrastructure.databases.influxdb.repository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PriceAssetRepository {

  private final InfluxDBClient influxDBClient;

  public List<FluxTable> getLatestPriceAsset(String symbol) {
    QueryApi queryApi = influxDBClient.getQueryApi();

    return queryApi.query(
        String.format(
            "from(bucket: \"stock-alert\") "
                + "|> range(start: -1y) "
                + "|> filter(fn: (r) => r._measurement == \"price_asset\" and r.symbol == \"%s\") "
                + "|> sort(columns: [\"_time\"], desc: true) "
                + "|> limit(n:1)",
            symbol));
  }

  public List<FluxTable> getLatestPriceAssets(List<String> symbols) {
    QueryApi queryApi = influxDBClient.getQueryApi();
    String filterCondition =
        symbols.stream()
            .map(symbol -> String.format("r.symbol == \"%s\"", symbol))
            .collect(Collectors.joining(" or "));

    return queryApi.query(
        String.format(
            "from(bucket: \"stock-alert\") "
                + "|> range(start: -1w) "
                + "|> filter(fn: (r) => r._measurement == \"price_asset\" and (%s)) "
                + "|> group(columns: [\"symbol\"]) "
                + "|> last(column: \"_time\")",
            filterCondition));
  }

  public List<FluxTable> getPriceHistoryAsset(String symbol, String time) {
    QueryApi queryApi = influxDBClient.getQueryApi();

    return queryApi.query(
        String.format(
            "from(bucket: \"stock-alert\") "
                + "|> range(start: -%s) "
                + "|> filter(fn: (r) => r._measurement == \"price_asset\" and r.symbol == \"%s\")",
            time, symbol));
  }
}
