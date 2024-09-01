package com.phdhuy.stock_alert.databases.influxdb.adapter;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.phdhuy.stock_alert.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.constant.MessageConstant;
import com.phdhuy.stock_alert.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.databases.postgresql.repository.AssetRepository;
import com.phdhuy.stock_alert.exception.NotFoundException;
import com.phdhuy.stock_alert.mapper.PriceAssetMapper;
import com.phdhuy.stock_alert.model.PriceAsset;
import com.phdhuy.stock_alert.ports.outbound.asset.GetPriceHistoryAssetPort;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class GetPriceHistoryAssetAdapter implements GetPriceHistoryAssetPort {

  private final AssetRepository assetRepository;

  private final InfluxDBClient influxDBClient;

  private final PriceAssetMapper priceAssetMapper;

  @Override
  public List<PriceAsset> getPriceHistoryAsset(UUID assetId, String interval) {
    AssetEntity assetEntity =
        assetRepository
            .findById(assetId)
            .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));

    QueryApi queryApi = influxDBClient.getQueryApi();
    String fluxQuery = buildFluxQuery(assetEntity.getIdentity(), interval);

    List<FluxTable> tables = queryApi.query(fluxQuery);
    List<PriceAsset> priceAssets = new ArrayList<>();
    for (FluxTable table : tables) {
      for (FluxRecord fluxRecord : table.getRecords()) {
        priceAssets.add(
            priceAssetMapper.toPriceAsset(
                (Double) fluxRecord.getValueByKey("_value"), Timestamp.from(Objects.requireNonNull(fluxRecord.getTime()))));
      }
    }

    return priceAssets;
  }

  private String buildFluxQuery(String symbol, String time) {
    return String.format(
        "from(bucket: \"stock-alert\") "
            + "|> range(start: -%s) "
            + "|> filter(fn: (r) => r._measurement == \"price_asset\" and r.symbol == \"%s\")",
        time, symbol);
  }
}
