package com.phdhuy.stock_alert.domain.asset.service;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.ports.inbound.GetDetailAssetUseCase;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.AssetRepositoryPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetDetailAssetService implements GetDetailAssetUseCase {

  private final AssetRepositoryPort assetRepositoryPort;

  @Override
  public Asset getDetailAsset(UUID asset) {
    return assetRepositoryPort.getDetailAsset(asset);
  }
}
