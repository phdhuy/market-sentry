package com.phdhuy.stock_alert.domain.auth.service;

import com.phdhuy.stock_alert.domain.auth.model.Token;
import com.phdhuy.stock_alert.domain.auth.ports.outbound.TokenUtilsPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import com.phdhuy.stock_alert.domain.auth.ports.inbound.CreateTokenUseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class CreateTokenService implements CreateTokenUseCase {

  private final TokenUtilsPort tokenUtilsPort;

  @Override
  public Token createToken(UUID userId) {
    return tokenUtilsPort.createToken(userId);
  }

  @Override
  public Token refreshToken(String refreshToken) {
    return tokenUtilsPort.refreshToken(refreshToken);
  }
}
