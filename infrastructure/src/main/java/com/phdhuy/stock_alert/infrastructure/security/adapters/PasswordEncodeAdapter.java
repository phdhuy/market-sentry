package com.phdhuy.stock_alert.infrastructure.security.adapters;

import com.phdhuy.stock_alert.domain.auth.outbound.PasswordEncodePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncodeAdapter implements PasswordEncodePort {

  private final PasswordEncoder passwordEncoder;

  @Override
  public String passwordEncoder(String password) {
    return passwordEncoder.encode(password);
  }
}
