package com.phdhuy.stock_alert.domain.alert.rule;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import org.springframework.stereotype.Component;

@Component
public class AlertStatusCheckHandler implements AlertHandler {

  private AlertHandler nextHandler;

  @Override
  public void setNext(AlertHandler nextHandler) {
    this.nextHandler = nextHandler;
  }

  @Override
  public boolean handle(Alert alert, double currentPrice) {
    if (alert.getAlertStatus().equals("ACTIVE")) {
      return nextHandler.handle(alert, currentPrice);
    }
    return false;
  }
}
