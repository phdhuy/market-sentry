package com.phdhuy.stock_alert.domain.asset.ports.outbound;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.model.PriceAsset;
import java.util.HashMap;
import java.util.List;

public interface PriceAssetRepositoryPort {

  Double getLatestPriceAsset(String symbol);

  HashMap<String, Double> getLatestPriceAssets(List<String> symbols);

  List<PriceAsset> getPriceHistoryAsset(Asset asset, String interval);
}
