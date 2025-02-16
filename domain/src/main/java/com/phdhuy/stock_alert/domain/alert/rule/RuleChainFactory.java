package com.phdhuy.stock_alert.domain.alert.rule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RuleChainFactory {

  private final AlertStatusCheckHandler statusCheck;
  private final PriceConditionCheckHandler priceCheck;
  private final TriggerAlertHandler triggerAlert;

  public AlertHandler createRuleChain() {
    statusCheck.setNext(priceCheck);
    priceCheck.setNext(triggerAlert);
    return statusCheck;
  }
}

