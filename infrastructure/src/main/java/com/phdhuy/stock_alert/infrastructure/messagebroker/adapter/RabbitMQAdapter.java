package com.phdhuy.stock_alert.infrastructure.messagebroker.adapter;

import com.phdhuy.stock_alert.domain.ports.outbound.messagebroker.RabbitMQPort;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQAdapter implements RabbitMQPort {

  private final RabbitTemplate rabbitTemplate;

  @Override
  public void sendMessage(String message) {
    String exchangeName = "websocket.fanout.exchange";
    rabbitTemplate.convertAndSend(exchangeName, "", message);
  }
}
