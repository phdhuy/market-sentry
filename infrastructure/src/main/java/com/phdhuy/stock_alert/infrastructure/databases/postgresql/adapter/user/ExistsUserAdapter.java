package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.user;

import com.phdhuy.stock_alert.domain.user.ports.outbound.ExistsUserPort;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class ExistsUserAdapter implements ExistsUserPort {

  private final UserRepository userRepository;

  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}
