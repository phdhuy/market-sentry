package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface AssetMapper {

  @Mapping(source = "latestPrice", target = "currentPriceUsd")
  Asset toAssetFromAssetEntity(AssetEntity assetEntity, Double latestPrice);
}
