package com.phdhuy.stock_alert.domain.auth.ports.inbound;

import com.phdhuy.stock_alert.domain.auth.model.Token;

import java.util.UUID;

public interface CreateTokenUseCase {

  Token createToken(UUID userId);

  Token refreshToken(String refreshToken);
}
