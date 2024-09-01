package com.phdhuy.stock_alert.ports.outbound.asset;

import java.util.HashMap;
import java.util.List;

public interface GetLatestPriceAssetPort {

  Double getLatestPriceAsset(String symbol);

  HashMap<String, Double> getLatestPriceAssets(List<String> symbols);
}
