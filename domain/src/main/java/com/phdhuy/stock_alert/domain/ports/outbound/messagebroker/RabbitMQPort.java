package com.phdhuy.stock_alert.domain.ports.outbound.messagebroker;

public interface RabbitMQPort {

  void sendMessage(String message);
}
