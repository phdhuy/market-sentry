package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.asset;

import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.AssetRepository;
import com.phdhuy.stock_alert.infrastructure.mapper.AssetMapper;
import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.GetAllAssetPort;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.GetLatestPriceAssetPort;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@PersistenceAdapter
@RequiredArgsConstructor
@Slf4j
public class GetAllAssetAdapter implements GetAllAssetPort {

  private final AssetRepository assetRepository;

  private final GetLatestPriceAssetPort getLatestPriceAssetPort;

  private final AssetMapper assetMapper;

  @Override
  public Page<Asset> getAllAsset(Pageable pageable, String type, List<String> query) {
    boolean isAll = query.isEmpty();
    Page<AssetEntity> assetSummaries =
        assetRepository.getAllAssetSummary(pageable, type, query, isAll);
    List<String> symbols =
        assetSummaries.getContent().stream().map(AssetEntity::getIdentity).toList();

    HashMap<String, Double> latestPrices = getLatestPriceAssetPort.getLatestPriceAssets(symbols);

    List<Asset> assets =
        assetSummaries.getContent().stream()
            .map(summary -> mapToAsset(summary, latestPrices))
            .toList();

    return new PageImpl<>(assets, pageable, assetSummaries.getTotalElements());
  }

  private Asset mapToAsset(AssetEntity assetEntity, HashMap<String, Double> latestPrices) {
    Double latestPrice = latestPrices.get(assetEntity.getIdentity());
    return assetMapper.toAssetFromAssetEntity(
            assetEntity, latestPrice != null ? latestPrice : 0.0);
  }
}
