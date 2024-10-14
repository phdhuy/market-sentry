package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.asset;

import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.AssetRepository;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.ExistsCryptoPort;
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
