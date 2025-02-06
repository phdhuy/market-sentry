package com.phdhuy.stock_alert.domain.user.service;

import com.phdhuy.stock_alert.domain.user.model.User;
import com.phdhuy.stock_alert.domain.user.port.inbound.FindUserByIdUseCase;
import com.phdhuy.stock_alert.domain.user.port.outbound.UserRepositoryPort;
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
