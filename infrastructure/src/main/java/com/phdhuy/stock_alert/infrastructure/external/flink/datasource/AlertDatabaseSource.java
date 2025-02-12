package com.phdhuy.stock_alert.infrastructure.external.flink.datasource;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.port.outbound.AlertRepositoryPort;
import com.phdhuy.stock_alert.shared.utils.SpringContext;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlertDatabaseSource extends RichSourceFunction<Alert> {

  private transient AlertRepositoryPort alertRepositoryPort;
  private volatile boolean running = true;

  @Override
  public void open(Configuration parameters) {
    this.alertRepositoryPort = SpringContext.getBean(AlertRepositoryPort.class);
  }

  @Override
  public void run(SourceContext<Alert> ctx) throws Exception {
    while (running) {
      List<Alert> activeAlerts = alertRepositoryPort.getListAlertActive();
      log.info("Broadcasting {} active alerts", activeAlerts.size());

      for (Alert alert : activeAlerts) {
        ctx.collect(alert);
      }

      Thread.sleep(30000);
    }
  }

  @Override
  public void cancel() {
    running = false;
  }
}

