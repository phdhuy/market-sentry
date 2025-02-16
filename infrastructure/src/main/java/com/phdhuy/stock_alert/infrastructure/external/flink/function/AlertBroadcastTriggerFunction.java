package com.phdhuy.stock_alert.infrastructure.external.flink.function;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.port.outbound.AlertRuleEvaluatorPort;
import com.phdhuy.stock_alert.infrastructure.external.flink.model.AssetPrice;
import com.phdhuy.stock_alert.shared.utils.SpringContext;
import java.util.Map;

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
    extends BroadcastProcessFunction<AssetPrice, Alert, Alert> {

  private final MapStateDescriptor<String, Alert> alertStateDescriptor =
      new MapStateDescriptor<>("alertsBroadcastState", String.class, Alert.class);

  private transient AlertRuleEvaluatorPort alertRuleEvaluatorPort;

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    this.alertRuleEvaluatorPort = SpringContext.getBean(AlertRuleEvaluatorPort.class);
  }

  @Override
  public void processElement(AssetPrice price, ReadOnlyContext ctx, Collector<Alert> out)
      throws Exception {
    ReadOnlyBroadcastState<String, Alert> alerts = ctx.getBroadcastState(alertStateDescriptor);
    Map<String, Double> prices = price.getPricesAsMap();

    for (Map.Entry<String, Double> entry : prices.entrySet()) {
      String assetId = entry.getKey();
      double priceValue = entry.getValue();

      if (alerts.contains(assetId)) {
        Alert alert = alerts.get(assetId);

        boolean isConditionMet = alertRuleEvaluatorPort.evaluateAlert(alert, priceValue);

        if (isConditionMet) {
          out.collect(alert);
        }
      }
    }
  }

  @Override
  public void processBroadcastElement(Alert alert, Context ctx, Collector<Alert> out)
      throws Exception {
    ctx.getBroadcastState(alertStateDescriptor).put(alert.getAsset().getIdentity(), alert);
  }
}
