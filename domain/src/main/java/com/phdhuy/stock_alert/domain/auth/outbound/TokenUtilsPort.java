package com.phdhuy.stock_alert.domain.auth.outbound;


import com.phdhuy.stock_alert.domain.auth.model.Token;

import java.util.UUID;

public interface TokenUtilsPort {

  Token createToken(UUID userId);

  Token refreshToken(String refreshToken);
}
