package com.phdhuy.stock_alert.domain.services.asset;

import com.phdhuy.stock_alert.shared.annotation.UseCase;
import com.phdhuy.stock_alert.domain.model.PriceAsset;
import com.phdhuy.stock_alert.domain.ports.inbound.asset.GetPriceHistoryAssetUseCase;
import com.phdhuy.stock_alert.domain.ports.outbound.asset.GetPriceHistoryAssetPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetPriceHistoryAssetService implements GetPriceHistoryAssetUseCase {

  private final GetPriceHistoryAssetPort getPriceHistoryAssetPort;

  @Override
  public List<PriceAsset> getPriceHistoryAsset(UUID assetId, String interval) {
    return getPriceHistoryAssetPort.getPriceHistoryAsset(assetId, interval);
  }
}
