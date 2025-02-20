package com.phdhuy.stock_alert.infrastructure.external.flink.sink;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.external.notification.adapter.PushNotificationAdapter;
import com.phdhuy.stock_alert.shared.utils.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AlertSink extends RichSinkFunction<Alert> {

  private transient PushNotificationAdapter pushNotificationAdapter;

  @Override
  public void open(Configuration parameters) {
    this.pushNotificationAdapter = SpringContext.getBean(PushNotificationAdapter.class);
  }

  @Override
  public void invoke(Alert alert, Context context) {
    log.info("Sending Alert Notification Id: {}, Type: {}", alert.getId(), alert.getAlertType());
    Map<String, Object> vars = new HashMap<>();

    vars.put("userName", "John Doe");
    vars.put("stockSymbol", alert.getAsset().getSymbol());
    vars.put("price", "$150.00");
    vars.put("currency", "USD");
    vars.put("dateTime", alert.getUpdatedAt());
    vars.put("condition", "Above $149.00");

    pushNotificationAdapter.pushAlertNotification(alert, vars);
  }
}
