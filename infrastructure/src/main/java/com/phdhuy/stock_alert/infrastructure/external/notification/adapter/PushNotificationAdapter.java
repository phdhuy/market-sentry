package com.phdhuy.stock_alert.infrastructure.external.notification.adapter;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.NotificationRepositoryAdapter;
import com.phdhuy.stock_alert.infrastructure.external.notification.stragtegy.NotificationStrategy;
import com.phdhuy.stock_alert.infrastructure.external.notification.stragtegy.NotificationStrategyFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PushNotificationAdapter {

  private final NotificationRepositoryAdapter notificationRepositoryAdapter;
  private final NotificationStrategyFactory notificationStrategyFactory;

  public void pushAlertNotification(Alert alert, Map<String, Object> vars) {
    notificationRepositoryAdapter.createNotification(alert, vars.get("condition").toString());

    List<NotificationStrategy> strategies = notificationStrategyFactory.getStrategies(alert.getAlertMethodTypes());

    for (NotificationStrategy strategy : strategies) {
      strategy.sendAlertNotification(alert, vars);
    }
  }
}
