package com.phdhuy.stock_alert.domain.services.asset;

import com.phdhuy.stock_alert.shared.annotation.UseCase;
import com.phdhuy.stock_alert.domain.model.Asset;
import com.phdhuy.stock_alert.domain.ports.inbound.asset.GetDetailAssetUseCase;
import com.phdhuy.stock_alert.domain.ports.outbound.asset.GetDetailAssetPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetDetailAssetService implements GetDetailAssetUseCase {

  private final GetDetailAssetPort getDetailAssetPort;

  @Override
  public Asset getDetailAsset(UUID asset) {
    return getDetailAssetPort.getDetailAsset(asset);
  }
}
