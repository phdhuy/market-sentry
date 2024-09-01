package com.phdhuy.stock_alert.ports.outbound.asset;

public interface ExistsCryptoPort {

  boolean existsByIdentity(String identity);
}
