package com.phdhuy.stock_alert.job;

import com.phdhuy.stock_alert.databases.postgresql.adapter.asset.CreateAssetAdapter;
import com.phdhuy.stock_alert.external.adapter.InfoStockAdapter;
import com.phdhuy.stock_alert.model.Asset;
import com.phdhuy.stock_alert.ports.outbound.asset.ExistsCryptoPort;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockInfoSyncJob {

  private final CreateAssetAdapter createAssetAdapter;

  private final InfoStockAdapter infoStockAdapter;

  private final ExistsCryptoPort existsCryptoPort;

  @Scheduled(cron = "*/10 * * * * *")
  public void crawlDataStockAndSaveToDB() throws IOException {
    List<Asset> assetList = infoStockAdapter.crawlDataStock();
    for (Asset asset : assetList) {
      if (existsCryptoPort.existsByIdentity(asset.getIdentity())) {
        createAssetAdapter.updateStock(asset);
      } else {
        createAssetAdapter.createStock(asset);
      }
    }
  }
}
