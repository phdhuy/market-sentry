package com.phdhuy.stock_alert.domain.user.service;

import com.phdhuy.stock_alert.domain.user.model.User;
import com.phdhuy.stock_alert.domain.user.ports.inbound.FindUserByIdUseCase;
import com.phdhuy.stock_alert.domain.user.ports.outbound.UserRepositoryPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindUserByIdService implements FindUserByIdUseCase {

  private final UserRepositoryPort userRepositoryPort;

  @Override
  public User findById(UUID id) {
    return userRepositoryPort.findByUserId(id);
  }
}
