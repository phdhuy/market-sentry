package com.phdhuy.stock_alert.infrastructure.external.notification.stragtegy.impl;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.external.notification.adapter.MailSenderAdapter;
import com.phdhuy.stock_alert.infrastructure.external.notification.stragtegy.NotificationStrategy;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationStrategy implements NotificationStrategy {

  private final MailSenderAdapter mailSenderAdapter;

  @Override
  public void sendAlertNotification(Alert alert, Map<String, Object> vars) {
    mailSenderAdapter.sendEmail(
        alert.getUser().getEmail(), "Stock Alert Notification", "mail/stock-alert", vars);
  }
}
