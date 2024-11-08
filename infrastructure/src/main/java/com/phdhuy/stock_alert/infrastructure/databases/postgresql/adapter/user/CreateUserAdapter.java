package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.user;

import com.phdhuy.stock_alert.domain.user.ports.outbound.CreateUserPort;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserEntity;
import com.phdhuy.stock_alert.infrastructure.security.adapters.PasswordEncodeAdapter;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.UserRepository;
import com.phdhuy.stock_alert.infrastructure.mapper.UserMapper;
import com.phdhuy.stock_alert.domain.user.model.User;
import com.phdhuy.stock_alert.shared.enums.Role;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateUserAdapter implements CreateUserPort {

  private final UserRepository userRepository;

  private final PasswordEncodeAdapter passwordEncodeAdapter;

  private final UserMapper userMapper;

  @Override
  public User createUser(String email, String password) {
    UserEntity userEntity = new UserEntity();

    userEntity.setEmail(email);
    userEntity.setIsConfirmed(true);
    userEntity.setRole(Role.ROLE_USER);
    userEntity.setPassword(passwordEncodeAdapter.passwordEncoder(password));

    return userMapper.toUserDomain(userRepository.save(userEntity));
  }
}
