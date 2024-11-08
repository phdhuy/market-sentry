package com.phdhuy.stock_alert.infrastructure.databases.influxdb.adapter;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.phdhuy.stock_alert.domain.asset.model.PriceAsset;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.GetPriceHistoryAssetPort;
import com.phdhuy.stock_alert.infrastructure.databases.influxdb.repository.PriceAssetRepository;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.AssetRepository;
import com.phdhuy.stock_alert.infrastructure.mapper.PriceAssetMapper;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.exception.NotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class GetPriceHistoryAssetAdapter implements GetPriceHistoryAssetPort {

  private final AssetRepository assetRepository;

  private final PriceAssetRepository priceAssetRepository;

  private final PriceAssetMapper priceAssetMapper;

  @Override
  public List<PriceAsset> getPriceHistoryAsset(UUID assetId, String interval) {
    AssetEntity assetEntity =
        assetRepository
            .findById(assetId)
            .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));

    List<FluxTable> tables =
        priceAssetRepository.getPriceHistoryAsset(assetEntity.getIdentity(), interval);
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
}
