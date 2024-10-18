package com.phdhuy.stock_alert.domain.asset.ports.outbound;

import com.phdhuy.stock_alert.domain.asset.model.PriceAsset;
import java.util.List;
import java.util.UUID;

public interface GetPriceHistoryAssetPort {

  List<PriceAsset> getPriceHistoryAsset(UUID assetId, String interval);
}
