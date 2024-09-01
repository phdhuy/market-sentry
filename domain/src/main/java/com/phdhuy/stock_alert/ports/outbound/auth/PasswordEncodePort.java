package com.phdhuy.stock_alert.ports.outbound.auth;

public interface PasswordEncodePort {
  String passwordEncoder(String password);
}
