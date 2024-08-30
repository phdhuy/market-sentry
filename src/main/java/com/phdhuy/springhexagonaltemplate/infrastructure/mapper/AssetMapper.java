package com.phdhuy.springhexagonaltemplate.infrastructure.mapper;

import com.phdhuy.springhexagonaltemplate.domain.model.Asset;
import com.phdhuy.springhexagonaltemplate.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.springhexagonaltemplate.infrastructure.databases.postgresql.dto.AssetSummaryDTO;
import com.phdhuy.springhexagonaltemplate.shared.config.MapStructConfig;
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
