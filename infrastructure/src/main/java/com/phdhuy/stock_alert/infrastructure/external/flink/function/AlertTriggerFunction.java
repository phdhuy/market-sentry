package com.phdhuy.stock_alert.infrastructure.external.flink.function;

import com.phdhuy.stock_alert.infrastructure.external.flink.model.AssetPrice;
import java.util.List;
import java.util.Map;

import com.phdhuy.stock_alert.infrastructure.external.flink.model.UserAlert;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.springframework.stereotype.Component;

@Component
public class AlertTriggerFunction implements FlatMapFunction<AssetPrice, String> {
  @Override
  public void flatMap(AssetPrice price, Collector<String> out) {
    Map<String, Double> prices = price.getPricesAsMap();

    if (prices.isEmpty()) {
      return;
    }

    List<UserAlert> userAlerts =
            List.of(new UserAlert("bitcoin", 100456, "above"), new UserAlert("proton", 0.005, "below"));

    for (UserAlert alert : userAlerts) {
      String coin = alert.getCoin();

      if (prices.containsKey(coin)) {
        double coinPrice = prices.get(coin);
        double threshold = alert.getThreshold();
        String condition = alert.getCondition();

        boolean alertTriggered =
                ("above".equals(condition) && coinPrice > threshold)
                        || ("below".equals(condition) && coinPrice < threshold);

        if (alertTriggered) {
          String notification =
                  String.format(
                          "Alert: %s price is now %.2f, crossing %s %.2f",
                          coin, coinPrice, condition, threshold);
          out.collect(notification);
        }
      }
    }
  }
}
