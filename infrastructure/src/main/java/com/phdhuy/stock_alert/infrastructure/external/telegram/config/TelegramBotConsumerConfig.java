package com.phdhuy.stock_alert.infrastructure.external.telegram.config;

import com.phdhuy.stock_alert.infrastructure.external.telegram.bot.StockAlertBotService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class TelegramBotConsumerConfig {

  @Value("${telegram.token}")
  private String botToken;

  private final StockAlertBotService stockAlertBotService;

  @PostConstruct
  public void configTelegramBotConsumer() throws TelegramApiException {
    TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
    botsApplication.registerBot(botToken, stockAlertBotService);
  }
}
