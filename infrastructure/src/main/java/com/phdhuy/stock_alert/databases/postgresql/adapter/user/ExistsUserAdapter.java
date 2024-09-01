package com.phdhuy.stock_alert.databases.postgresql.adapter.user;

import com.phdhuy.stock_alert.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.databases.postgresql.repository.UserRepository;
import com.phdhuy.stock_alert.ports.outbound.user.ExistsUserPort;
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
