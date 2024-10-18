package com.phdhuy.stock_alert.domain.asset.ports.outbound;

public interface ExistsCryptoPort {

  boolean existsByIdentity(String identity);
}
