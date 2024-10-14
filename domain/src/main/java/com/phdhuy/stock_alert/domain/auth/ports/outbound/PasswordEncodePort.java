package com.phdhuy.stock_alert.domain.auth.ports.outbound;

public interface PasswordEncodePort {
  String passwordEncoder(String password);
}
