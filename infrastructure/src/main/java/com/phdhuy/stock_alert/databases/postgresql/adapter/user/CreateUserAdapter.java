package com.phdhuy.stock_alert.databases.postgresql.adapter.user;

import com.phdhuy.stock_alert.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.databases.postgresql.repository.UserRepository;
import com.phdhuy.stock_alert.mapper.UserMapper;
import com.phdhuy.stock_alert.model.User;
import com.phdhuy.stock_alert.ports.outbound.user.CreateUserPort;
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
