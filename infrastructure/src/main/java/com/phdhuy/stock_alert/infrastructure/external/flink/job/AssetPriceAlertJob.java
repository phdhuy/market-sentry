package com.phdhuy.stock_alert.infrastructure.external.flink.job;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.external.flink.datasource.AlertDatabaseSource;
import com.phdhuy.stock_alert.infrastructure.external.flink.datasource.AssetPriceDataSource;
import com.phdhuy.stock_alert.infrastructure.external.flink.function.AlertBroadcastTriggerFunction;
import com.phdhuy.stock_alert.infrastructure.external.flink.function.JsonToCoinPriceMapper;
import com.phdhuy.stock_alert.infrastructure.external.flink.model.AssetPrice;
import com.phdhuy.stock_alert.infrastructure.external.flink.sink.AlertSink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
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

  private final AlertSink alertSink;

  private final AssetPriceDataSource assetPriceDataSource;

  private final AlertBroadcastTriggerFunction alertBroadcastTriggerFunction;

  @EventListener(ApplicationReadyEvent.class)
  public void trigger() {
    try {
      final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

      DataStream<String> assetPriceStream = assetPriceDataSource.getAssetPriceSource(env);

      MapStateDescriptor<String, Alert> alertStateDescriptor =
          new MapStateDescriptor<>(
              "alertsBroadcastState",
              TypeInformation.of(String.class),
              TypeInformation.of(Alert.class));

      BroadcastStream<Alert> broadcastAlerts =
          env.addSource(new AlertDatabaseSource()).broadcast(alertStateDescriptor);

      DataStream<AssetPrice> coinPriceStream = assetPriceStream.map(jsonToCoinPriceMapper);

      DataStream<Alert> alertsStream =
          coinPriceStream.connect(broadcastAlerts).process(alertBroadcastTriggerFunction);

      alertsStream.addSink(alertSink);

      env.execute("Asset Price Alert Job");
    } catch (Exception e) {
      log.error("Error starting Asset price alert job", e);
    }
  }
}
