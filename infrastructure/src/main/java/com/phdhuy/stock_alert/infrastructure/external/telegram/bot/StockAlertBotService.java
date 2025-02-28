package com.phdhuy.stock_alert.infrastructure.external.telegram.bot;

import com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.UserTelegramAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockAlertBotService implements LongPollingSingleThreadUpdateConsumer {

  private final TelegramClient telegramClient;

  private final UserTelegramAdapter userTelegramAdapter;

  @Override
  public void consume(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String chatId = update.getMessage().getChatId().toString();
      String messageText = update.getMessage().getText();

      if (messageText.startsWith("/start")) {
        sendMessage(
            chatId,
            "Welcome! Please send your email in the format:\nEMAIL: your-email@example.com");
      } else if (messageText.matches("^EMAIL:\\s+.+@.+\\..+$")) {
        try {
          String email = messageText.substring(6).trim();
          sendMessage(chatId, "✅ Email received: " + email + "\nLinking your account...");
          userTelegramAdapter.createUserTelegram(chatId, email);
          sendMessage(chatId, "✅ Linking your account successful!");
        } catch (Exception e) {
          sendMessage(chatId, "❌ Email not found!");
        }
      } else {
        sendMessage(
            chatId,
            "❌ Invalid format! Please send your email like this:\nEMAIL: your-email@example.com");
      }
    }
  }

  public void sendMessage(String chatId, String text) {
    SendMessage message = new SendMessage(chatId, text);
    try {
      telegramClient.execute(message);
    } catch (TelegramApiException e) {
      log.error(e.getMessage());
    }
  }
}
