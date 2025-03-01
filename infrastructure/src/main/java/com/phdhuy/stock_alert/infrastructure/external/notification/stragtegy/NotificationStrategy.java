package com.phdhuy.stock_alert.infrastructure.external.notification.stragtegy;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import java.util.List;
import java.util.Map;

public interface NotificationStrategy {

  void sendAlertNotification(Alert alert, Map<String, Object> vars);

  boolean isContainAlertMethodType(List<String> alertMethodTypes);
}
