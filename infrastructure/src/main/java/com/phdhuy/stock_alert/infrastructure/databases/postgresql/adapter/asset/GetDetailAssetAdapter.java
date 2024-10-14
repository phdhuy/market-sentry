package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.asset;

import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.AssetRepository;
import com.phdhuy.stock_alert.shared.exception.NotFoundException;
import com.phdhuy.stock_alert.infrastructure.mapper.AssetMapper;
import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.GetDetailAssetPort;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.GetLatestPriceAssetPort;
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
