package com.phdhuy.stock_alert.infrastructure.external.notification.stragtegy.impl;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.external.notification.adapter.TelegramSenderAdapter;
import com.phdhuy.stock_alert.infrastructure.external.notification.stragtegy.NotificationStrategy;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationStrategy implements NotificationStrategy {

  private final TelegramSenderAdapter telegramSenderAdapter;

  private static final String TYPE = "TELEGRAM";

  @Override
  public boolean isContainAlertMethodType(List<String> alertMethodTypes) {
    return alertMethodTypes.contains(TYPE);
  }

  @Override
  public void sendAlertNotification(Alert alert, Map<String, Object> vars) {
    telegramSenderAdapter.sendMessage(alert, vars);
  }
}
