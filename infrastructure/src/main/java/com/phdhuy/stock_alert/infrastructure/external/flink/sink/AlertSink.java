package com.phdhuy.stock_alert.infrastructure.external.flink.sink;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlertSink implements SinkFunction<Alert> {

  @Override
  public void invoke(Alert alert, Context context) {
    log.info("Sending Alert Notification Id: {}, Type: {}", alert.getId(), alert.getAlertType());
  }
}
