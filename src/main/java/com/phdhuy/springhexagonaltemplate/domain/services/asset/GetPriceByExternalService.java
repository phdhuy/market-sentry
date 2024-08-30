package com.phdhuy.springhexagonaltemplate.domain.services.asset;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.springhexagonaltemplate.domain.ports.outbound.messagebroker.RabbitMQPort;
import com.phdhuy.springhexagonaltemplate.infrastructure.external.constant.ExternalAPIConstant;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetPriceByExternalService {

  private final RabbitMQPort rabbitMQPort;
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  private static final int RECONNECT_DELAY = 3;
  private volatile boolean isConnected = false;
  private WebSocketClient webSocketClient;

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

    URI uri = new URI(ExternalAPIConstant.PRICE_CRYPTO);
    webSocketClient =
        new WebSocketClient(uri) {
          @Override
          public void onOpen(ServerHandshake handshakedata) {
            log.info("WebSocket connection opened");
            isConnected = true;
            String subscribeMessage =
                "{\"method\": \"SUBSCRIBE\", \"params\": [\"btcusdt@ticker\"], \"id\": 1}";
            send(subscribeMessage);
          }

          @Override
          public void onMessage(String message) {
            try {
              JsonNode node = new ObjectMapper().readTree(message);
              rabbitMQPort.sendMessage(node.toString());
            } catch (JsonProcessingException e) {
              log.error("Error parsing JSON message:", e);
            }
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
    scheduler.schedule(
        () -> {
          log.info("### Reconnecting...");
          try {
            connectWebSocket();
          } catch (URISyntaxException e) {
            log.error("Invalid WebSocket URL", e);
          }
        },
        RECONNECT_DELAY,
        TimeUnit.SECONDS);
  }

  public void closeWebSocket() {
    if (webSocketClient != null) {
      webSocketClient.close();
    }
  }
}
