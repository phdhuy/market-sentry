package com.phdhuy.stock_alert.domain.messagebroker;

public interface RabbitMQPort {

  void sendMessage(String message);
}
