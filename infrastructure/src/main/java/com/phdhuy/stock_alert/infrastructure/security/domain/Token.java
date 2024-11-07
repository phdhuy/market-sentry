package com.phdhuy.stock_alert.infrastructure.security.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {

  private String accessToken;

  private long expiresIn;

  private String refreshToken;
}
