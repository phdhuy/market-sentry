package com.phdhuy.stock_alert.mapper;

import com.phdhuy.stock_alert.config.MapStructConfig;
import com.phdhuy.stock_alert.databases.postgresql.dto.AssetSummaryDTO;
import com.phdhuy.stock_alert.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.model.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface AssetMapper {

  @Mapping(source = "assetSummaryDTO.rank", target = "rank")
  @Mapping(source = "latestPrice", target = "currentPriceUsd")
  Asset toAssetFromProjection(AssetSummaryDTO assetSummaryDTO, Double latestPrice);

  @Mapping(source = "latestPrice", target = "currentPriceUsd")
  Asset toAssetFromAssetEntity(AssetEntity assetEntity, Double latestPrice);
}
