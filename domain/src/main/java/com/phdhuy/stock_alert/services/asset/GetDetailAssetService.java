package com.phdhuy.stock_alert.services.asset;

import com.phdhuy.stock_alert.annotation.UseCase;
import com.phdhuy.stock_alert.model.Asset;
import com.phdhuy.stock_alert.ports.inbound.asset.GetDetailAssetUseCase;
import com.phdhuy.stock_alert.ports.outbound.asset.GetDetailAssetPort;
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
