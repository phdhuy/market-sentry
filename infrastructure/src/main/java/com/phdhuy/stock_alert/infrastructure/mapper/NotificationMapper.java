package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.domain.notification.model.Notification;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.NotificationEntity;
import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface NotificationMapper {

    Notification toNotification(NotificationEntity notificationEntity);
}
