package com.phdhuy.stock_alert.domain.ports.outbound.user;

public interface ExistsUserPort {

  boolean existsByEmail(String email);
}
