package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.notification.model.Notification;
import com.phdhuy.stock_alert.domain.notification.port.outbound.NotificationRepositoryPort;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AlertEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.NotificationEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.NotificationRepository;
import com.phdhuy.stock_alert.infrastructure.mapper.NotificationMapper;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.exception.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@PersistenceAdapter
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepositoryPort {

  private final NotificationRepository notificationRepository;

  private final AlertRepositoryAdapter alertRepositoryAdapter;

  private final NotificationMapper notificationMapper;

  public void createNotification(Alert alert, String content) {
    NotificationEntity notificationEntity = new NotificationEntity();

    AlertEntity alertEntity = alertRepositoryAdapter.findById(alert.getId());

    notificationEntity.setAlertEntity(alertEntity);
    notificationEntity.setUserEntity(alertEntity.getUserEntity());
    notificationEntity.setContent(content);

    notificationRepository.save(notificationEntity);
  }

  @Override
  public int countUnreadNotification(UUID userId) {
    return notificationRepository.countUnreadNotification(userId);
  }

  @Override
  public void markReadNotification(UUID notificationId, UUID userId) {
    notificationRepository.markReadNotification(notificationId, userId);
  }

  @Override
  public Page<Notification> getMyNotification(Pageable pageable, UUID userId) {
    Page<NotificationEntity> notificationEntities =
        notificationRepository.getMyNotification(pageable, userId);

    return notificationEntities.map(notificationMapper::toNotification);
  }

  @Override
  public Notification getDetailNotification(UUID notificationId) {
    NotificationEntity notificationEntity = this.findById(notificationId);
    return notificationMapper.toNotification(
        notificationEntity, notificationEntity.getAlertEntity());
  }

  public NotificationEntity findById(UUID notificationId) {
    return notificationRepository
        .findById(notificationId)
        .orElseThrow(() -> new NotFoundException(MessageConstant.NOTIFICATION_NOT_FOUND));
  }
}
