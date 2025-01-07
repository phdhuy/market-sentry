package com.phdhuy.stock_alert.domain.asset.service;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.model.PriceAsset;
import com.phdhuy.stock_alert.domain.asset.ports.inbound.GetPriceHistoryAssetUseCase;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.PriceAssetRepositoryPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class GetPriceHistoryAssetService implements GetPriceHistoryAssetUseCase {

  private final PriceAssetRepositoryPort priceAssetRepositoryPort;

  private final GetDetailAssetService getDetailAssetService;

  @Override
  public List<PriceAsset> getPriceHistoryAsset(UUID assetId, String interval) {
    Asset asset = getDetailAssetService.getDetailAsset(assetId);
    return priceAssetRepositoryPort.getPriceHistoryAsset(asset, interval);
  }
}
