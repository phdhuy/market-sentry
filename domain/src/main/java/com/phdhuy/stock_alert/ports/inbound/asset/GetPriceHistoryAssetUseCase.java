package com.phdhuy.stock_alert.ports.inbound.asset;

import com.phdhuy.stock_alert.model.PriceAsset;

import java.util.List;
import java.util.UUID;

public interface GetPriceHistoryAssetUseCase {

  List<PriceAsset> getPriceHistoryAsset(UUID assetId, String interval);
}
