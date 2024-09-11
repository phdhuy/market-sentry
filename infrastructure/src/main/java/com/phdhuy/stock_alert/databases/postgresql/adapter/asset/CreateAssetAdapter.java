package com.phdhuy.stock_alert.databases.postgresql.adapter.asset;

import com.phdhuy.stock_alert.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.constant.MessageConstant;
import com.phdhuy.stock_alert.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.databases.postgresql.entity.enums.AssetType;
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

    this.toCryptoAssetEntity(asset, assetEntity);
  }

  public void updateCrypto(Asset asset) {
    AssetEntity assetEntity =
        assetRepository
            .findByIdentity(asset.getIdentity())
            .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));

    this.toCryptoAssetEntity(asset, assetEntity);
  }

  public void createStock(Asset asset) {
    AssetEntity assetEntity = new AssetEntity();

    this.toStockAssetEntity(asset, assetEntity);
  }

  public void updateStock(Asset asset) {
    AssetEntity assetEntity =
        assetRepository
            .findByIdentity(asset.getIdentity())
            .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));

    this.toStockAssetEntity(asset, assetEntity);
  }

  private void toCryptoAssetEntity(Asset asset, AssetEntity assetEntity) {
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
    assetEntity.setAssetType(AssetType.valueOf(asset.getAssetType()));
    assetRepository.save(assetEntity);
  }

  private void toStockAssetEntity(Asset asset, AssetEntity assetEntity) {
    assetEntity.setIdentity(asset.getIdentity());
    assetEntity.setSymbol(asset.getSymbol());
    assetEntity.setName(asset.getName());
    assetEntity.setNameVn(asset.getNameVn());
    assetEntity.setFloor(asset.getFloor());
    assetEntity.setLogo(asset.getLogo());
    assetEntity.setAssetType(AssetType.valueOf(asset.getAssetType()));
    assetRepository.save(assetEntity);
  }
}
