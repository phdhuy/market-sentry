package com.phdhuy.stock_alert.infrastructure.external.flink.job;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.external.flink.model.AssetPrice;
import com.phdhuy.stock_alert.infrastructure.external.flink.datasource.AssetPriceDataSource;
import com.phdhuy.stock_alert.infrastructure.external.flink.function.AlertTriggerFunction;
import com.phdhuy.stock_alert.infrastructure.external.flink.function.JsonToCoinPriceMapper;
import com.phdhuy.stock_alert.infrastructure.external.flink.sink.AlertSink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AssetPriceAlertJob {

  private final JsonToCoinPriceMapper jsonToCoinPriceMapper;

  private final AlertTriggerFunction alertTriggerFunction;

  private final AlertSink alertSink;

  private final AssetPriceDataSource assetPriceDataSource;

  @EventListener(ApplicationReadyEvent.class)
  public void trigger() {
    try {
      final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

      DataStream<String> assetPriceStream = assetPriceDataSource.getAssetPriceSource(env);

      DataStream<AssetPrice> coinPriceStream = assetPriceStream.map(jsonToCoinPriceMapper);
      DataStream<Alert> alertsStream = coinPriceStream.flatMap(alertTriggerFunction);
      alertsStream.addSink(alertSink);

      env.execute("Asset Price Alert Job");
    } catch (Exception e) {
      log.error("Error starting Asset price alert job", e);
    }
  }
}
