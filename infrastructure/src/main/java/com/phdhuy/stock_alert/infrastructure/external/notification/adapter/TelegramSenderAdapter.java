package com.phdhuy.stock_alert.infrastructure.external.notification.adapter;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.UserTelegramAdapter;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserTelegramEntity;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramSenderAdapter {

  private final TelegramClient telegramClient;

  private final UserTelegramAdapter userTelegramAdapter;

  @Async("asyncExecutor")
  public void sendMessage(Alert alert, Map<String, Object> vars) {
    List<UserTelegramEntity> userTelegramEntities =
        userTelegramAdapter.findByUserId(alert.getUser().getId());

    for (UserTelegramEntity userTelegramEntity : userTelegramEntities) {
      String chatId = String.valueOf(userTelegramEntity.getChatId());
      String condition = vars.get("condition").toString();
      SendMessage message = new SendMessage(chatId, condition);
      try {
        telegramClient.execute(message);
      } catch (TelegramApiException e) {
        log.error(e.getMessage());
      }
    }
  }
}
