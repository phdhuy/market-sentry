package com.phdhuy.stock_alert.databases.postgresql.adapter.user;

import com.phdhuy.stock_alert.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.constant.MessageConstant;
import com.phdhuy.stock_alert.databases.postgresql.entity.UserEntity;
import com.phdhuy.stock_alert.databases.postgresql.repository.UserRepository;
import com.phdhuy.stock_alert.exception.NotFoundException;
import com.phdhuy.stock_alert.mapper.UserMapper;
import com.phdhuy.stock_alert.model.User;
import com.phdhuy.stock_alert.ports.outbound.user.FindUserByIdPort;
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
