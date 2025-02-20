package com.phdhuy.stock_alert.infrastructure.external.flink.datasource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.port.outbound.AlertRepositoryPort;
import com.phdhuy.stock_alert.infrastructure.external.messagebroker.UserAlertActionMessage;
import com.phdhuy.stock_alert.shared.utils.SpringContext;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserAlertDataSource extends RichSourceFunction<UserAlertActionMessage> {

  private volatile boolean running = true;
  private transient AlertRepositoryPort alertRepositoryPort;
  private transient Connection connection;
  private transient Channel channel;

  private static final String QUEUE_NAME = "user_alert";
  private static final String EXCHANGE_NAME = "alerts-exchange";
  private static final String ROUTING_KEY = "alerts.update";
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void open(Configuration parameters) throws Exception {
    this.alertRepositoryPort = SpringContext.getBean(AlertRepositoryPort.class);

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("rabbitmq");
    factory.setUsername("guest");
    factory.setPassword("guest");
    connection = factory.newConnection();
    channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
  }

  @Override
  public void run(SourceContext<UserAlertActionMessage> ctx) throws Exception {
    List<Alert> activeAlerts = alertRepositoryPort.getListAlertActive();
    log.info("Active alerts: {}", activeAlerts.size());
    synchronized (ctx.getCheckpointLock()) {
      for (Alert alert : activeAlerts) {
        ctx.collect(new UserAlertActionMessage("ADD", alert));
      }
    }

    DeliverCallback deliverCallback =
        (consumerTag, delivery) -> {
          try {
            log.info("Start consume");
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            log.info(message);
            JsonNode jsonNode = objectMapper.readTree(message);
            log.info(jsonNode.toString());

            String action = jsonNode.get("action").asText();
            Alert alert = objectMapper.treeToValue(jsonNode.get("data"), Alert.class);

            synchronized (ctx.getCheckpointLock()) {
              UserAlertActionMessage userAlertMessage =
                  UserAlertActionMessage.builder().action(action).data(alert).build();

              ctx.collect(userAlertMessage);
            }
          } catch (Exception e) {
            log.error("Error processing RabbitMQ message", e);
          }
        };

    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

    while (running) {
      Thread.sleep(1000);
    }
  }

  @Override
  public void cancel() {
    running = false;
    try {
      if (channel != null) channel.close();
      if (connection != null) connection.close();
    } catch (IOException | TimeoutException e) {
      log.error("Error closing RabbitMQ connection", e);
    }
  }
}
