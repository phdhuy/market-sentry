package com.phdhuy.stock_alert.domain.alert.rule;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import org.springframework.stereotype.Component;

@Component
public class PriceConditionCheckHandler implements AlertHandler {

  private AlertHandler nextHandler;

  @Override
  public void setNext(AlertHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public boolean handle(Alert alert, double currentPrice) {
    boolean conditionMet = switch (alert.getAlertConditionType()) {
      case "GREATER_THAN" -> currentPrice > alert.getValue();
      case "LESS_THAN" -> currentPrice < alert.getValue();
      default -> false;
    };

    if (conditionMet) {
      return nextHandler.handle(alert, currentPrice);
    }

    return false;
  }
}
