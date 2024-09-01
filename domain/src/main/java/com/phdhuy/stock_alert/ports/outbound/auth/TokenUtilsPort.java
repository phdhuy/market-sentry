package com.phdhuy.stock_alert.ports.outbound.auth;


import com.phdhuy.stock_alert.model.Token;

import java.util.UUID;

public interface TokenUtilsPort {

  Token createToken(UUID userId);

  Token refreshToken(String refreshToken);
}
