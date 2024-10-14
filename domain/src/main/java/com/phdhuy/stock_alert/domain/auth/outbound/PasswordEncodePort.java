package com.phdhuy.stock_alert.domain.auth.outbound;

public interface PasswordEncodePort {
  String passwordEncoder(String password);
}
