package com.phdhuy.stock_alert.infrastructure.external.flink.function;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.port.outbound.AlertRuleEvaluatorPort;
import com.phdhuy.stock_alert.infrastructure.external.flink.dto.AlertTriggerMessage;
import com.phdhuy.stock_alert.infrastructure.external.flink.dto.UserAlertActionMessage;
import com.phdhuy.stock_alert.infrastructure.external.flink.model.AssetPrice;
import com.phdhuy.stock_alert.shared.utils.SpringContext;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlertBroadcastTriggerFunction
    extends BroadcastProcessFunction<AssetPrice, UserAlertActionMessage, AlertTriggerMessage> {

  private final MapStateDescriptor<UUID, Alert> alertStateDescriptor =
      new MapStateDescriptor<>("alertsBroadcastState", UUID.class, Alert.class);

  private transient AlertRuleEvaluatorPort alertRuleEvaluatorPort;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    this.alertRuleEvaluatorPort = SpringContext.getBean(AlertRuleEvaluatorPort.class);
  }

  @Override
  public void processElement(
      AssetPrice price, ReadOnlyContext ctx, Collector<AlertTriggerMessage> out) throws Exception {
    ReadOnlyBroadcastState<UUID, Alert> alerts = ctx.getBroadcastState(alertStateDescriptor);
    log.debug("Alerts in state: {}", alerts.toString());

    Map<String, Double> prices = price.getPricesAsMap();

    for (Map.Entry<String, Double> entry : prices.entrySet()) {
      String assetId = entry.getKey();
      double priceValue = entry.getValue();

      for (Map.Entry<UUID, Alert> alertEntry : alerts.immutableEntries()) {
        Alert alert = alertEntry.getValue();

        if (alert.getAsset().getIdentity().equals(assetId)) {
          boolean isConditionMet = alertRuleEvaluatorPort.evaluateAlert(alert, priceValue);

          if (isConditionMet) {
            String conditionMessage = buildConditionMessage(alert, priceValue);
            out.collect(new AlertTriggerMessage(priceValue, conditionMessage, alert));
          }
        }
      }
    }
  }

  @Override
  public void processBroadcastElement(
      UserAlertActionMessage alertAction, Context ctx, Collector<AlertTriggerMessage> out)
      throws Exception {
    var broadcastState = ctx.getBroadcastState(alertStateDescriptor);
    String action = alertAction.getAction();

    Alert alert = alertAction.getData();

    switch (action) {
      case "ADD", "UPDATE":
        log.info("Updating broadcast state with alert: {}", alert.getId());
        broadcastState.put(alert.getId(), alert);
        break;
      case "DELETE":
        log.info("Removing alert from state: {}", alert.getId());
        broadcastState.remove(alert.getId());
        break;
      default:
        log.warn("Unknown action received: {}", action);
    }
  }

  private String buildConditionMessage(Alert alert, double priceValue) {
    String conditionType = alert.getAlertConditionType();
    double targetPrice = alert.getValue();

    return switch (conditionType) {
      case "GREATER_THAN" ->
              String.format("Alert triggered: %s price is %.2f, which is above your threshold of %.2f.",
                      alert.getAsset().getName(), priceValue, targetPrice);
      case "LESS_THAN" ->
              String.format("Alert triggered: %s price is %.2f, which is below your threshold of %.2f.",
                      alert.getAsset().getName(), priceValue, targetPrice);
      default -> "Alert triggered due to an unknown condition.";
    };
  }
}
