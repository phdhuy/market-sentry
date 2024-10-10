package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.user;

import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.UserRepository;
import com.phdhuy.stock_alert.infrastructure.mapper.UserMapper;
import com.phdhuy.stock_alert.domain.model.User;
import com.phdhuy.stock_alert.domain.ports.outbound.user.CreateUserPort;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateUserAdapter implements CreateUserPort {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  @Override
  public User createUser(User user) {
    return userMapper.toUserDomain(userRepository.save(userMapper.fromUserDomain(user)));
  }
}
