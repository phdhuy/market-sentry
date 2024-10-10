package com.phdhuy.stock_alert.domain.ports.outbound.auth;

public interface PasswordEncodePort {
  String passwordEncoder(String password);
}
