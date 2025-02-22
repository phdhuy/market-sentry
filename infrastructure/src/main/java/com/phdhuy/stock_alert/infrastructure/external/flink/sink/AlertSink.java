package com.phdhuy.stock_alert.infrastructure.external.flink.sink;

import com.phdhuy.stock_alert.infrastructure.external.flink.dto.AlertTriggerMessage;
import com.phdhuy.stock_alert.infrastructure.external.notification.adapter.PushNotificationAdapter;
import com.phdhuy.stock_alert.shared.utils.SpringContext;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlertSink extends RichSinkFunction<AlertTriggerMessage> {

  private transient PushNotificationAdapter pushNotificationAdapter;

  @Override
  public void open(Configuration parameters) {
    this.pushNotificationAdapter = SpringContext.getBean(PushNotificationAdapter.class);
  }

  @Override
  public void invoke(AlertTriggerMessage alertTriggerMessage, Context context) {
    log.info(
        "Sending Alert Notification Id: {}, Type: {}",
        alertTriggerMessage.getAlert().getId(),
        alertTriggerMessage.getAlert().getAlertType());
    Map<String, Object> vars = new HashMap<>();

    vars.put("email", alertTriggerMessage.getAlert().getUser().getEmail());
    vars.put("stockSymbol", alertTriggerMessage.getAlert().getAsset().getSymbol());
    vars.put("price", alertTriggerMessage.getPriceTrigger());
    vars.put("currency", "USD");
    vars.put("dateTime", alertTriggerMessage.getAlert().getUpdatedAt());
    vars.put("condition", "Above $149.00");

    pushNotificationAdapter.pushAlertNotification(alertTriggerMessage.getAlert(), vars);
  }
}
