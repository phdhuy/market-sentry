package com.phdhuy.stock_alert.domain.services.auth;

import com.phdhuy.stock_alert.shared.annotation.UseCase;
import com.phdhuy.stock_alert.domain.model.Token;
import com.phdhuy.stock_alert.domain.ports.inbound.auth.CreateTokenUseCase;
import com.phdhuy.stock_alert.domain.ports.outbound.auth.TokenUtilsPort;
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
