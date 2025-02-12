package com.phdhuy.stock_alert.infrastructure.external.flink.function;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.external.flink.model.AssetPrice;
import java.util.Map;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.springframework.stereotype.Component;

@Component
public class AlertBroadcastFunction extends BroadcastProcessFunction<AssetPrice, Alert, Alert> {

  private final MapStateDescriptor<String, Alert> alertStateDescriptor =
      new MapStateDescriptor<>("alertsBroadcastState", String.class, Alert.class);

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

        boolean isConditionMet =
            ("GREATER_THAN".equals(alert.getAlertConditionType()) && priceValue > alert.getValue())
                || ("LESS_THAN".equals(alert.getAlertConditionType())
                    && priceValue < alert.getValue());

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
