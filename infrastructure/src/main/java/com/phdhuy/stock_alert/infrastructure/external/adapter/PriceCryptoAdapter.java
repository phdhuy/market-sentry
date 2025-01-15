package com.phdhuy.stock_alert.infrastructure.external.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.infrastructure.messagebroker.adapter.RabbitMQAdapter;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceCryptoAdapter {

  private final RabbitMQAdapter rabbitMQAdapter;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final int INITIAL_RECONNECT_DELAY = 3;
  private static final int MAX_RECONNECT_DELAY = 60;
  private static final int MAX_RETRIES = 10;

  private volatile boolean isConnected = false;
  private WebSocketClient webSocketClient;
  private int reconnectAttempts = 0;

  @PostConstruct
  public void connect() {
    try {
      connectWebSocket();
    } catch (URISyntaxException e) {
      log.error("Invalid WebSocket URL", e);
    }
  }

  private void connectWebSocket() throws URISyntaxException {
    if (isConnected) {
      return;
    }

    URI uri = new URI(CommonConstant.PRICE_CRYPTO);
    webSocketClient =
        new WebSocketClient(uri) {
          @Override
          public void onOpen(ServerHandshake handshakedata) {
            log.info("WebSocket connection opened");
            isConnected = true;
            reconnectAttempts = 0;
            String subscribeMessage =
                "{\"method\": \"SUBSCRIBE\", \"params\": [\"btcusdt@ticker\"], \"id\": 1}";
            send(subscribeMessage);
          }

          @Override
          public void onMessage(String message) {
            scheduler.execute(
                () -> {
                  try {
                    JsonNode node = objectMapper.readTree(message);
                    rabbitMQAdapter.sendMessage(node.toString());
                  } catch (JsonProcessingException e) {
                    log.error("Error parsing JSON message:", e);
                  }
                });
          }

          @Override
          public void onClose(int code, String reason, boolean remote) {
            log.info("WebSocket connection closed: {}", reason);
            isConnected = false;
            scheduleReconnect();
          }

          @Override
          public void onError(Exception ex) {
            log.error("WebSocket connection error", ex);
            isConnected = false;
            scheduleReconnect();
          }
        };

    webSocketClient.connect();
  }

  private void scheduleReconnect() {
    if (reconnectAttempts >= MAX_RETRIES) {
      log.error("Max reconnect attempts reached. Aborting reconnection.");
      return;
    }

    int delay =
        Math.min(
            INITIAL_RECONNECT_DELAY * (int) Math.pow(2, reconnectAttempts), MAX_RECONNECT_DELAY);
    reconnectAttempts++;
    scheduler.schedule(
        () -> {
          log.info("Reconnecting attempt {}...", reconnectAttempts);
          try {
            connectWebSocket();
          } catch (URISyntaxException e) {
            log.error("Invalid WebSocket URL", e);
          }
        },
        delay,
        TimeUnit.SECONDS);
  }

  @PreDestroy
  public void shutdown() {
    log.info("Shutting down WebSocket client and scheduler");
    if (webSocketClient != null && webSocketClient.isOpen()) {
      webSocketClient.close();
    }
    scheduler.shutdown();
  }
}
