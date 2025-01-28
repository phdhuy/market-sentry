package com.phdhuy.stock_alert.infrastructure.external.messagebroker;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQAdapter {

  private final RabbitTemplate rabbitTemplate;

  public void sendMessage(String message) {
    String exchangeName = "price_asset.fanout.exchange";
    rabbitTemplate.convertAndSend(exchangeName, "", message);
  }
}
