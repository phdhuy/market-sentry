package com.phdhuy.stock_alert.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
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
    return new FanoutExchange("websocket.fanout.exchange");
  }

  @Bean
  public Queue queue1() {
    return new Queue("price.database");
  }

  @Bean
  public Binding binding1(Queue queue1, FanoutExchange fanoutExchange) {
    return BindingBuilder.bind(queue1).to(fanoutExchange);
  }
}
