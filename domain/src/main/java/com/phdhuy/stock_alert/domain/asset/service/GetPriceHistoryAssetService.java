package com.phdhuy.stock_alert.domain.asset.service;

import com.phdhuy.stock_alert.domain.asset.model.PriceAsset;
import com.phdhuy.stock_alert.domain.asset.ports.inbound.GetPriceHistoryAssetUseCase;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.GetPriceHistoryAssetPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
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
