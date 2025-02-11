package com.phdhuy.stock_alert.domain.notification.service;

import com.phdhuy.stock_alert.domain.notification.model.Notification;
import com.phdhuy.stock_alert.domain.notification.port.inbound.NotificationUseCase;
import com.phdhuy.stock_alert.domain.notification.port.outbound.NotificationRepositoryPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@UseCase
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {

  private final NotificationRepositoryPort notificationRepositoryPort;

  @Override
  public Page<Notification> getMyNotification(Pageable pageable, UUID userId) {
    return notificationRepositoryPort.getMyNotification(pageable, userId);
  }

  @Override
  public int countUnreadNotification(UUID userId) {
    return notificationRepositoryPort.countUnreadNotification(userId);
  }

  @Override
  public void markReadNotification(UUID notificationId, UUID userId) {
    notificationRepositoryPort.markReadNotification(notificationId, userId);
  }

  @Override
  public Notification getDetailNotification(UUID notificationId, UUID userId) {
    return null;
  }
}
