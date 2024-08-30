package com.phdhuy.springhexagonaltemplate.shared.config;

import com.phdhuy.springhexagonaltemplate.application.ws.handler.PriceStockVN30Handler;
import com.phdhuy.springhexagonaltemplate.application.ws.handler.PriceWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new PriceWebSocketHandler(), "/prices").setAllowedOrigins("*");
    registry.addHandler(new PriceStockVN30Handler(), "/prices/vn30").setAllowedOrigins("*");
  }
}
