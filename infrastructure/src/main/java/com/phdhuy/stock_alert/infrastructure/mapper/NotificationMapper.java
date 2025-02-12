package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.domain.notification.model.Notification;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AlertEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.NotificationEntity;
import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface NotificationMapper {

  Notification toNotification(NotificationEntity notificationEntity);

  @Mapping(source = "notificationEntity.id", target = "id")
  @Mapping(source = "notificationEntity.createdAt", target = "createdAt")
  @Mapping(source = "alertEntity", target = "alert")
  Notification toNotification(NotificationEntity notificationEntity, AlertEntity alertEntity);
}
