package com.phdhuy.springhexagonaltemplate.shared.config;

import com.phdhuy.springhexagonaltemplate.shared.handler.PriceWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new PriceWebSocketHandler(), "/assets/prices").setAllowedOrigins("*");
  }
}
