package com.phdhuy.stock_alert.infrastructure.external.telegram.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
@Slf4j
public class TelegramBotPubConfig {

  @Value("${telegram.token}")
  private String botToken;

  @Bean
  public TelegramClient telegramClientConfig() {
    return new OkHttpTelegramClient(botToken);
  }
}
