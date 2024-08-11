package com.phdhuy.springhexagonaltemplate.application.ws.handler;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PriceWebSocketHandler extends TextWebSocketHandler {
  private static final List<WebSocketSession> sessions = new ArrayList<>();

  @Override
  public void afterConnectionEstablished(@NotNull WebSocketSession session) {
    log.info("WebSocket connection established with session: {}", session.getId());
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(
      @NotNull WebSocketSession session, @NotNull CloseStatus status) {
    log.info("WebSocket connection closed with session: {}", session.getId());
    sessions.remove(session);
  }

  public void handleTextMessage(String message) throws Exception {
    log.info("Sessions count: {}", sessions.size());
    for (WebSocketSession webSocketSession : sessions) {
      if (webSocketSession.isOpen()) {
        try {
          log.info("Sending message: '{}' to session: {}", message, webSocketSession.getId());
          webSocketSession.sendMessage(new TextMessage(message));
        } catch (IOException e) {
          log.error(
              "Error sending message to session: {}. Error: {}",
              webSocketSession.getId(),
              e.getMessage());
        }
      } else {
        log.warn("Session {} is closed. Skipping...", webSocketSession.getId());
      }
    }
  }
}
