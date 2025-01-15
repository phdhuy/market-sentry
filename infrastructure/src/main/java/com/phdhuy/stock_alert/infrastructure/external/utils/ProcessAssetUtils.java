package com.phdhuy.stock_alert.infrastructure.external.utils;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.AssetRepositoryAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessAssetUtils {

  private final AssetRepositoryAdapter assetRepositoryAdapter;

  public void processCryptoAssets(List<Asset> assetList) {
    List<Asset> newAssets = new ArrayList<>();

    Set<String> identities = assetList.stream().map(Asset::getIdentity).collect(Collectors.toSet());
    Map<String, Asset> existingAssets = assetRepositoryAdapter.findAllByIdentities(identities);

    for (Asset asset : assetList) {
      if (!existingAssets.containsKey(asset.getIdentity())) {
        newAssets.add(asset);
      }
    }

    if (!newAssets.isEmpty()) {
      assetRepositoryAdapter.createAssetsInBatch(newAssets);
    }
  }
}
