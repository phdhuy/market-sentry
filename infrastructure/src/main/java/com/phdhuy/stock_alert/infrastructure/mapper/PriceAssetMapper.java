package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import com.phdhuy.stock_alert.domain.asset.model.PriceAsset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;

@Mapper(config = MapStructConfig.class)
public interface PriceAssetMapper {

  @Mapping(source = "price", target = "price")
  @Mapping(source = "time", target = "time")
  PriceAsset toPriceAsset(Double price, Timestamp time);
}
