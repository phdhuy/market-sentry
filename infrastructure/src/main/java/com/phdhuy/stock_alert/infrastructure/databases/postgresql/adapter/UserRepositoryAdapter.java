package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter;

import com.phdhuy.stock_alert.domain.user.model.User;
import com.phdhuy.stock_alert.domain.user.ports.outbound.UserRepositoryPort;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.UserRepository;
import com.phdhuy.stock_alert.infrastructure.mapper.UserMapper;
import com.phdhuy.stock_alert.infrastructure.security.adapters.PasswordEncodeAdapter;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.enums.Role;
import com.phdhuy.stock_alert.shared.exception.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

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

  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public User findByUserId(UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
    return userMapper.toUserDomain(userEntity);
  }
}
