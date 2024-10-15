package com.phdhuy.stock_alert.domain.asset.service;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.ports.inbound.GetDetailAssetUseCase;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.GetDetailAssetPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetDetailAssetService implements GetDetailAssetUseCase {

  private final GetDetailAssetPort getDetailAssetPort;

  @Override
  public Asset getDetailAsset(UUID asset) {
    return getDetailAssetPort.getDetailAsset(asset);
  }
}
