package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AlertEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserEntity;
import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface AlertMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "updatedAt", target = "updatedAt")
  Alert toAlert(AlertEntity alertEntity);

  @Mapping(source = "assetEntity", target = "asset")
  @Mapping(source = "alertEntity.id", target = "id")
  @Mapping(source = "alertEntity.createdAt", target = "createdAt")
  @Mapping(source = "alertEntity.updatedAt", target = "updatedAt")
  Alert toAlert(AlertEntity alertEntity, AssetEntity assetEntity);

  @Mapping(source = "assetEntity", target = "asset")
  @Mapping(source = "userEntity", target = "user")
  @Mapping(source = "alertEntity.id", target = "id")
  @Mapping(source = "alertEntity.createdAt", target = "createdAt")
  @Mapping(source = "alertEntity.updatedAt", target = "updatedAt")
  Alert toAlert(AlertEntity alertEntity, AssetEntity assetEntity, UserEntity userEntity);
}
