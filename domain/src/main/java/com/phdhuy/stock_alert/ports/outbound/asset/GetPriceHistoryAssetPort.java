package com.phdhuy.stock_alert.ports.outbound.asset;

import com.phdhuy.stock_alert.model.PriceAsset;

import java.util.List;
import java.util.UUID;

public interface GetPriceHistoryAssetPort {

  List<PriceAsset> getPriceHistoryAsset(UUID assetId, String interval);
}
