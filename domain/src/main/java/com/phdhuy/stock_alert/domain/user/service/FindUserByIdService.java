package com.phdhuy.stock_alert.domain.user.service;

import com.phdhuy.stock_alert.domain.user.model.User;
import com.phdhuy.stock_alert.domain.user.ports.inbound.FindUserByIdUseCase;
import com.phdhuy.stock_alert.domain.user.ports.outbound.FindUserByIdPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindUserByIdService implements FindUserByIdUseCase {

  private final FindUserByIdPort findUserByIdPort;

  @Override
  public User findById(UUID id) {
    return findUserByIdPort.findByUserId(id);
  }
}
