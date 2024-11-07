package com.phdhuy.stock_alert.infrastructure.security.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncodeAdapter {

  private final PasswordEncoder passwordEncoder;

  public String passwordEncoder(String password) {
    return passwordEncoder.encode(password);
  }
}
