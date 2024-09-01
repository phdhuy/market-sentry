package com.phdhuy.stock_alert.databases.postgresql.adapter.asset;

import com.phdhuy.stock_alert.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.constant.MessageConstant;
import com.phdhuy.stock_alert.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.databases.postgresql.repository.AssetRepository;
import com.phdhuy.stock_alert.exception.NotFoundException;
import com.phdhuy.stock_alert.mapper.AssetMapper;
import com.phdhuy.stock_alert.model.Asset;
import com.phdhuy.stock_alert.ports.outbound.asset.GetDetailAssetPort;
import com.phdhuy.stock_alert.ports.outbound.asset.GetLatestPriceAssetPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class GetDetailAssetAdapter implements GetDetailAssetPort {

  private final AssetRepository assetRepository;

  private final GetLatestPriceAssetPort getLatestPriceAssetPort;

  private final AssetMapper assetMapper;

  @Override
  public Asset getDetailAsset(UUID id) {
    AssetEntity assetEntity =
        assetRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));
    return assetMapper.toAssetFromAssetEntity(
        assetEntity, getLatestPriceAssetPort.getLatestPriceAsset(assetEntity.getIdentity()));
  }
}
