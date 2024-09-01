package com.phdhuy.stock_alert.databases.postgresql.adapter.asset;

import com.phdhuy.stock_alert.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.constant.MessageConstant;
import com.phdhuy.stock_alert.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.databases.postgresql.repository.AssetRepository;
import com.phdhuy.stock_alert.exception.NotFoundException;
import com.phdhuy.stock_alert.model.Asset;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateAssetAdapter {

  private final AssetRepository assetRepository;

  public void createCrypto(Asset asset) {
    AssetEntity assetEntity = new AssetEntity();

    this.toAssetEntity(asset, assetEntity);
  }

  public void updateCrypto(Asset asset) {
    AssetEntity assetEntity =
        assetRepository
            .findByIdentity(asset.getIdentity())
            .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));

    this.toAssetEntity(asset, assetEntity);
  }

  private void toAssetEntity(Asset asset, AssetEntity assetEntity) {
    assetEntity.setIdentity(asset.getIdentity());
    assetEntity.setRank(asset.getRank());
    assetEntity.setSymbol(asset.getSymbol());
    assetEntity.setName(asset.getName());
    assetEntity.setSupply(asset.getSupply());
    assetEntity.setMaxSupply(asset.getMaxSupply());
    assetEntity.setMarketCapUsd(asset.getMarketCapUsd());
    assetEntity.setVolumeUsd24Hr(asset.getVolumeUsd24Hr());
    assetEntity.setChangePercent24Hr(asset.getChangePercent24Hr());
    assetEntity.setVwap24Hr(asset.getVwap24Hr());
    assetEntity.setExplorer(asset.getExplorer());
    assetRepository.save(assetEntity);
  }
}
