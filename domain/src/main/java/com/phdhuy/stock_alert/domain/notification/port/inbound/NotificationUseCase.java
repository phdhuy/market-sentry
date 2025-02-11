package com.phdhuy.stock_alert.domain.notification.port.inbound;

import com.phdhuy.stock_alert.domain.notification.model.Notification;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationUseCase {

  Page<Notification> getMyNotification(Pageable pageable, UUID userId);

  int countUnreadNotification(UUID userId);

  void markReadNotification(UUID notificationId, UUID userId);

  Notification getDetailNotification(UUID notificationId, UUID userId);
}
