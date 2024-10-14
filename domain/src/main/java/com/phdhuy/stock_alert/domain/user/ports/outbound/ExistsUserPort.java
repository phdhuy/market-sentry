package com.phdhuy.stock_alert.domain.user.ports.outbound;

public interface ExistsUserPort {

  boolean existsByEmail(String email);
}
