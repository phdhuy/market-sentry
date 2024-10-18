package com.phdhuy.stock_alert.domain.asset.ports.outbound;

import java.util.HashMap;
import java.util.List;

public interface GetLatestPriceAssetPort {

  Double getLatestPriceAsset(String symbol);

  HashMap<String, Double> getLatestPriceAssets(List<String> symbols);
}
