package com.phdhuy.stock_alert.domain.alert.port.outbound;

import com.phdhuy.stock_alert.domain.alert.model.Alert;

public interface AlertRuleEvaluatorPort {

  boolean evaluateAlert(Alert alert, double currentPrice);
}
