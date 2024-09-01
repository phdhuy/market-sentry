package com.phdhuy.stock_alert.services.asset;

import com.phdhuy.stock_alert.annotation.UseCase;
import com.phdhuy.stock_alert.model.PriceAsset;
import com.phdhuy.stock_alert.ports.inbound.asset.GetPriceHistoryAssetUseCase;
import com.phdhuy.stock_alert.ports.outbound.asset.GetPriceHistoryAssetPort;
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
