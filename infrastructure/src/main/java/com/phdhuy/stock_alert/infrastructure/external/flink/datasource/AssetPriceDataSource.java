package com.phdhuy.stock_alert.infrastructure.external.flink.datasource;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AssetPriceDataSource {

  private static final String RABBITMQ_HOST = "rabbitmq";
  private static final int RABBITMQ_PORT = 5672;
  private static final String RABBITMQ_USERNAME = "guest";
  private static final String RABBITMQ_PASSWORD = "guest";
  private static final String RABBITMQ_VIRTUAL_HOST = "/";
  private static final String QUEUE_NAME = "price_flink";
  private static final int PARALLELISM = 1;

  public DataStream<String> getAssetPriceSource(StreamExecutionEnvironment env) {
    RMQConnectionConfig connectionConfig = createRabbitMQConnectionConfig();
    return createRabbitMQDataStream(env, connectionConfig);
  }

  private RMQConnectionConfig createRabbitMQConnectionConfig() {
    return new RMQConnectionConfig.Builder()
        .setHost(RABBITMQ_HOST)
        .setPort(RABBITMQ_PORT)
        .setUserName(RABBITMQ_USERNAME)
        .setPassword(RABBITMQ_PASSWORD)
        .setVirtualHost(RABBITMQ_VIRTUAL_HOST)
        .build();
  }

  private DataStream<String> createRabbitMQDataStream(
      StreamExecutionEnvironment env, RMQConnectionConfig connectionConfig) {
    return env.addSource(
            new RMQSource<>(connectionConfig, QUEUE_NAME, true, new SimpleStringSchema()))
        .setParallelism(PARALLELISM);
  }
}
