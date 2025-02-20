package com.phdhuy.stock_alert.infrastructure.external.messagebroker;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class RabbitMQConfig implements WebSocketMessageBrokerConfigurer {
  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(jsonMessageConverter());
    return template;
  }

  @Bean
  public FanoutExchange fanoutExchange() {
    return new FanoutExchange("price_asset.fanout.exchange");
  }

  @Bean
  public Queue priceWebsocket() {
    return new Queue("price_websocket");
  }

  @Bean
  public Queue priceFlink() {
    return new Queue("price_flink");
  }

  @Bean
  public Binding binding1(Queue priceWebsocket, FanoutExchange fanoutExchange) {
    return BindingBuilder.bind(priceWebsocket).to(fanoutExchange);
  }

  @Bean
  public Binding binding2(Queue priceFlink, FanoutExchange fanoutExchange) {
    return BindingBuilder.bind(priceFlink).to(fanoutExchange);
  }
}
