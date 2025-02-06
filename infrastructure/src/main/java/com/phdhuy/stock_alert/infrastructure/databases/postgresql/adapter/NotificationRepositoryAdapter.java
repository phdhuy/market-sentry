package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.notification.port.outbound.NotificationRepositoryPort;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AlertEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.NotificationEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.NotificationRepository;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepositoryPort {

  private final NotificationRepository notificationRepository;

  private final AlertRepositoryAdapter alertRepositoryAdapter;

  public void createNotification(Alert alert, String content) {
    NotificationEntity notificationEntity = new NotificationEntity();

    AlertEntity alertEntity = alertRepositoryAdapter.findById(alert.getId());

    notificationEntity.setAlertEntity(alertEntity);
    notificationEntity.setUserEntity(alertEntity.getUserEntity());
    notificationEntity.setContent(content);

    notificationRepository.save(notificationEntity);
  }
}
