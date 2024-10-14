package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.user;

import com.phdhuy.stock_alert.domain.user.ports.outbound.FindUserByIdPort;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.UserRepository;
import com.phdhuy.stock_alert.shared.exception.NotFoundException;
import com.phdhuy.stock_alert.infrastructure.mapper.UserMapper;
import com.phdhuy.stock_alert.domain.user.model.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class FindUserByIdAdapter implements FindUserByIdPort {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  @Override
  public User findByUserId(UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    return userMapper.toUserDomain(userEntity);
  }
}
