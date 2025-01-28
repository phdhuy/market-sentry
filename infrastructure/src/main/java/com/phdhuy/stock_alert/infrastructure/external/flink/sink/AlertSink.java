package com.phdhuy.stock_alert.infrastructure.external.flink.sink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlertSink implements SinkFunction<String> {

  @Override
  public void invoke(String value, Context context) {
    log.info("Sending Alert Notification: {}", value);
  }
}
