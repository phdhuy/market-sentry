package com.phdhuy.stock_alert.shared.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class PriceWebSocketHandler extends TextWebSocketHandler {
  private static final List<WebSocketSession> sessions = new ArrayList<>();

  @Override
  public void afterConnectionEstablished(@NotNull WebSocketSession session) {
    sessions.add(session);
  }

  @Override
  public void afterConnectionClosed(
      @NotNull WebSocketSession session, @NotNull CloseStatus status) {
    sessions.remove(session);
  }

  public void handleTextMessage(String message) {
    for (WebSocketSession webSocketSession : sessions) {
      if (webSocketSession.isOpen()) {
        try {
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
