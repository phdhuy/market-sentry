package com.phdhuy.stock_alert.domain.auth.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {

  private String accessToken;

  private long expiresIn;

  private String refreshToken;
}
