package com.phdhuy.stock_alert.infrastructure.external.flink.function;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.ports.outbound.AlertRepositoryPort;
import com.phdhuy.stock_alert.infrastructure.external.flink.model.AssetPrice;
import java.util.List;
import java.util.Map;

import com.phdhuy.stock_alert.shared.utils.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlertTriggerFunction extends RichFlatMapFunction<AssetPrice, Alert> {

  private transient AlertRepositoryPort alertRepositoryPort;

  @Override
  public void open(Configuration parameters) {
    this.alertRepositoryPort = SpringContext.getBean(AlertRepositoryPort.class);
  }

  @Override
  public void flatMap(AssetPrice price, Collector<Alert> out) {
    Map<String, Double> prices = price.getPricesAsMap();

    if (prices.isEmpty()) {
      return;
    }

    List<Alert> userAlerts = alertRepositoryPort.getListAlertActive();

    for (Alert alert : userAlerts) {
      String identity = alert.getAsset().getIdentity();

      if (prices.containsKey(identity)) {
        double coinPrice = prices.get(identity);
        double threshold = alert.getValue();
        String alertConditionType = alert.getAlertConditionType();

        boolean alertTriggered =
                ("GREATER_THAN".equals(alertConditionType) && coinPrice > threshold)
                        || ("LESS_THAN".equals(alertConditionType) && coinPrice < threshold);

        if (alertTriggered) {
          String notification =
                  String.format(
                          "Alert: %s price is now %.2f, crossing %s %.2f",
                          identity, coinPrice, alertConditionType, threshold);
          log.info(notification);
          out.collect(alert);
        }
      }
    }
  }
}
