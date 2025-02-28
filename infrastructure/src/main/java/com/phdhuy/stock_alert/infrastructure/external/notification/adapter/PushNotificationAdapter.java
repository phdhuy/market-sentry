package com.phdhuy.stock_alert.infrastructure.external.notification.adapter;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.NotificationRepositoryAdapter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationAdapter {

  private final NotificationRepositoryAdapter notificationRepositoryAdapter;

  private final MailSenderAdapter mailSenderAdapter;

  private final TelegramSenderAdapter telegramSenderAdapter;


  public void pushAlertNotification(Alert alert, Map<String, Object> vars) {
    notificationRepositoryAdapter.createNotification(alert, "Alert Notification");
    mailSenderAdapter.sendEmail(
        alert.getUser().getEmail(), "Stock Alert Notification", "mail/stock-alert", vars);
    telegramSenderAdapter.sendMessage(alert, vars);
  }
}
