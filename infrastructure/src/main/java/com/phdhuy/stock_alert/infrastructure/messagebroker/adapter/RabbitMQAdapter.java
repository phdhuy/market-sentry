package com.phdhuy.stock_alert.infrastructure.messagebroker.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQAdapter {

  private final RabbitTemplate rabbitTemplate;

  public void sendMessage(String message) {
    String exchangeName = "websocket.fanout.exchange";
    rabbitTemplate.convertAndSend(exchangeName, "", message);
  }
}
