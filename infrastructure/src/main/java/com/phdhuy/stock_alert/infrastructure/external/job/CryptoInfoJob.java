package com.phdhuy.stock_alert.infrastructure.external.job;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.ExistsCryptoPort;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.asset.CreateAssetAdapter;
import com.phdhuy.stock_alert.infrastructure.external.adapter.InfoCryptoAdapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CryptoInfoJob {

  private final CreateAssetAdapter createAssetAdapter;

  private final InfoCryptoAdapter infoCryptoAdapter;

  private final ExistsCryptoPort existsCryptoPort;

  @Scheduled(cron = "*/60 * * * * *")
  public void crawlDataCryptoAndSaveToDB() {
    List<Asset> assetList = infoCryptoAdapter.crawlDataCrypto();
    for (Asset asset : assetList) {
      if (existsCryptoPort.existsByIdentity(asset.getIdentity())) {
        createAssetAdapter.updateCrypto(asset);
      } else {
        createAssetAdapter.createCrypto(asset);
      }
    }
  }
}
