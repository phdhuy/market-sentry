package com.phdhuy.stock_alert.domain.notification.port.outbound;

import com.phdhuy.stock_alert.domain.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationRepositoryPort {

  int countUnreadNotification(UUID userId);

  void markReadNotification(UUID notificationId, UUID userId);

  Page<Notification> getMyNotification(Pageable pageable, UUID userId);

  Notification getDetailNotification(UUID notificationId);
}
