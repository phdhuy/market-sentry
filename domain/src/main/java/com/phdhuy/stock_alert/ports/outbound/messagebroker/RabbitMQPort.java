package com.phdhuy.stock_alert.ports.outbound.messagebroker;

public interface RabbitMQPort {

  void sendMessage(String message);
}
