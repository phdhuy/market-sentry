package com.phdhuy.stock_alert.infrastructure.external.notification.stragtegy;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationStrategyFactory {

  private final List<NotificationStrategy> strategies;

  public List<NotificationStrategy> getStrategies(List<String> alertMethodTypes) {
    if (alertMethodTypes == null || alertMethodTypes.isEmpty()) {
      throw new IllegalArgumentException("Alert method types must not be null or empty");
    }

    List<NotificationStrategy> matchingStrategies =
        strategies.stream()
            .filter(strategy -> strategy.isContainAlertMethodType(alertMethodTypes))
            .toList();

    if (matchingStrategies.isEmpty()) {
      throw new IllegalArgumentException(
          "No matching strategy found for alert method types: " + alertMethodTypes);
    }

    return matchingStrategies;
  }
}
