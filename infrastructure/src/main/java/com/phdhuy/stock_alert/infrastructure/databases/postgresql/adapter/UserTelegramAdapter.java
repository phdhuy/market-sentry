package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter;

import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserTelegramEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.UserTelegramRepository;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserTelegramAdapter {

  private final UserTelegramRepository userTelegramRepository;

  private final UserRepositoryAdapter userRepositoryAdapter;

  public void createUserTelegram(String chatId, String email) {
    UserEntity userEntity = userRepositoryAdapter.findUserEntityByEmail(email);

    UserTelegramEntity userTelegramEntity = new UserTelegramEntity();

    userTelegramEntity.setUserEntity(userEntity);
    userTelegramEntity.setChatId(chatId);

    userTelegramRepository.save(userTelegramEntity);
  }

  public List<UserTelegramEntity> findByUserId(UUID userId) {
    return userTelegramRepository.findByUserEntityId(userId);
  }
}
