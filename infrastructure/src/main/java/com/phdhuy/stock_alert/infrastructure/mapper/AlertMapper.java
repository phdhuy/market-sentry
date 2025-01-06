package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AlertEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface AlertMapper {

  @Mapping(source = "assetEntity", target = "asset")
  @Mapping(source = "alertEntity.id", target = "id")
  Alert toAlertFromAlertEntity(AlertEntity alertEntity, AssetEntity assetEntity);
}
