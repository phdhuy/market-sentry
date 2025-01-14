package com.phdhuy.stock_alert.infrastructure.external.job;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.AssetRepositoryAdapter;
import com.phdhuy.stock_alert.infrastructure.external.adapter.InfoStockAdapter;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockInfoJob {

  private final InfoStockAdapter infoStockAdapter;

  private final AssetRepositoryAdapter assetRepositoryAdapter;

  @Scheduled(cron = "0 0 17 * * *")
  public void crawlDataStockAndSaveToDB() throws IOException {
    List<Asset> assetList = infoStockAdapter.crawlDataStock();
    for (Asset asset : assetList) {
      if (assetRepositoryAdapter.existsByIdentity(asset.getIdentity())) {
        assetRepositoryAdapter.updateAsset(asset);
      } else {
        assetRepositoryAdapter.createAsset(asset);
      }
    }
  }
}
