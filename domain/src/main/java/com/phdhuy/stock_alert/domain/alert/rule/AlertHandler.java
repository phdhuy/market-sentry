package com.phdhuy.stock_alert.domain.alert.rule;

import com.phdhuy.stock_alert.domain.alert.model.Alert;

public interface AlertHandler {

  void setNext(AlertHandler nextHandler);

  boolean handle(Alert alert, double currentPrice);
}
