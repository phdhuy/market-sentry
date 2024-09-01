package com.phdhuy.stock_alert.databases.postgresql.adapter.asset;

import com.phdhuy.stock_alert.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.databases.postgresql.repository.AssetRepository;
import com.phdhuy.stock_alert.ports.outbound.asset.ExistsCryptoPort;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class ExistsAssetAdapter implements ExistsCryptoPort {

  private final AssetRepository assetRepository;

  @Override
  public boolean existsByIdentity(String identity) {
    return assetRepository.existsByIdentity(identity);
  }
}
