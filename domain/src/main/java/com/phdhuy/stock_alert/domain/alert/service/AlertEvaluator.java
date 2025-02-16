package com.phdhuy.stock_alert.domain.alert.service;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.port.outbound.AlertRuleEvaluatorPort;
import com.phdhuy.stock_alert.domain.alert.rule.AlertHandler;
import com.phdhuy.stock_alert.domain.alert.rule.RuleChainFactory;
import org.springframework.stereotype.Service;

@Service
public class AlertEvaluator implements AlertRuleEvaluatorPort {

  private final AlertHandler ruleChain;

  public AlertEvaluator(RuleChainFactory ruleChainFactory) {
    this.ruleChain = ruleChainFactory.createRuleChain();
  }

  @Override
  public boolean evaluateAlert(Alert alert, double currentPrice) {
    return ruleChain.handle(alert, currentPrice);
  }
}