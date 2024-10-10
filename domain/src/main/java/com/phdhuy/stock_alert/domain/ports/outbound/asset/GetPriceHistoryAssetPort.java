package com.phdhuy.stock_alert.domain.ports.outbound.asset;

import com.phdhuy.stock_alert.domain.model.PriceAsset;

import java.util.List;
import java.util.UUID;

public interface GetPriceHistoryAssetPort {

  List<PriceAsset> getPriceHistoryAsset(UUID assetId, String interval);
}
