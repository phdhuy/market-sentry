package com.phdhuy.stock_alert.ports.outbound.user;

public interface ExistsUserPort {

  boolean existsByEmail(String email);
}
