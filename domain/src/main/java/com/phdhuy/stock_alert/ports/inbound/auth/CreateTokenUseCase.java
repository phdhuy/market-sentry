package com.phdhuy.stock_alert.ports.inbound.auth;


import com.phdhuy.stock_alert.model.Token;

import java.util.UUID;

public interface CreateTokenUseCase {

  Token createToken(UUID userId);

  Token refreshToken(String refreshToken);
}
