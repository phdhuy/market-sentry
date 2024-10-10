package com.phdhuy.stock_alert.domain.ports.outbound.asset;

public interface ExistsCryptoPort {

  boolean existsByIdentity(String identity);
}
