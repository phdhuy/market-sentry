package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.asset;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.AssetRepositoryPort;
import com.phdhuy.stock_alert.infrastructure.databases.influxdb.adapter.PriceAssetRepositoryAdapter;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.AssetRepository;
import com.phdhuy.stock_alert.infrastructure.mapper.AssetMapper;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class AssetRepositoryAdapter implements AssetRepositoryPort {

  private final AssetRepository assetRepository;

  private final PriceAssetRepositoryAdapter priceAssetRepositoryAdapter;

  private final AssetMapper assetMapper;

  @Override
  public boolean existsByIdentity(String identity) {
    return assetRepository.existsByIdentity(identity);
  }

  @Override
  public Page<Asset> getAllAsset(Pageable pageable, String type, List<String> query) {
    boolean isAll = query.isEmpty();
    Page<AssetEntity> assetSummaries =
            assetRepository.getAllAssetSummary(pageable, type, query, isAll);
    List<String> symbols =
            assetSummaries.getContent().stream().map(AssetEntity::getIdentity).toList();

    HashMap<String, Double> latestPrices = priceAssetRepositoryAdapter.getLatestPriceAssets(symbols);

    List<Asset> assets =
            assetSummaries.getContent().stream()
                    .map(summary -> mapToAsset(summary, latestPrices))
                    .toList();

    return new PageImpl<>(assets, pageable, assetSummaries.getTotalElements());
  }

  private Asset mapToAsset(AssetEntity assetEntity, HashMap<String, Double> latestPrices) {
    Double latestPrice = latestPrices.get(assetEntity.getIdentity());
    return assetMapper.toAssetFromAssetEntity(assetEntity, latestPrice != null ? latestPrice : 0.0);
  }

  @Override
  public Asset getDetailAsset(UUID id) {
    AssetEntity assetEntity =
        assetRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));
    return assetMapper.toAssetFromAssetEntity(
        assetEntity, priceAssetRepositoryAdapter.getLatestPriceAsset(assetEntity.getIdentity()));
  }

  public AssetEntity findAssetEntityById(UUID id) {
    return assetRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));
  }

  @Override
  public Asset getDetailAsset(UUID id) {
    AssetEntity assetEntity =
        assetRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));
    return assetMapper.toAssetFromAssetEntity(
        assetEntity, getLatestPriceAssetPort.getLatestPriceAsset(assetEntity.getIdentity()));
  }
}
