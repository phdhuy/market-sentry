package com.phdhuy.springhexagonaltemplate.infrastructure.job;

import com.phdhuy.springhexagonaltemplate.domain.model.Asset;
import com.phdhuy.springhexagonaltemplate.domain.ports.outbound.asset.ExistsCryptoPort;
import com.phdhuy.springhexagonaltemplate.infrastructure.databases.postgresql.adapter.asset.CreateAssetAdapter;
import com.phdhuy.springhexagonaltemplate.infrastructure.external.adapter.InfoCryptoAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoInfoSyncJob {

  private final CreateAssetAdapter createAssetAdapter;

  private final InfoCryptoAdapter infoCryptoAdapter;

  private final ExistsCryptoPort existsCryptoPort;

  @Scheduled(cron = "0 */1 * * * *")
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
