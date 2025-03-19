package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.port.outbound.AssetRepositoryPort;
import com.phdhuy.stock_alert.infrastructure.databases.influxdb.adapter.PriceAssetRepositoryAdapter;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AssetType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.AssetRepository;
import com.phdhuy.stock_alert.infrastructure.external.crawl.InfoStockAdapter;
import com.phdhuy.stock_alert.infrastructure.mapper.AssetMapper;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.shared.constant.CommonConstant;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.exception.NotFoundException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@RequiredArgsConstructor
public class AssetRepositoryAdapter implements AssetRepositoryPort {

  private final AssetRepository assetRepository;

  private final PriceAssetRepositoryAdapter priceAssetRepositoryAdapter;

  private final OkHttpClient httpClient;

  private final ObjectMapper objectMapper;

  private final AssetMapper assetMapper;

  @Override
  public boolean existsByIdentity(String identity) {
    return assetRepository.existsByIdentity(identity);
  }

  @Override
  public Page<Asset> getAllAsset(Pageable pageable, String type, String query, String category) {
    Page<AssetEntity> assetSummaries;
    if (category.equals("VN30") && type.equals("STOCK")) {
      assetSummaries =
          assetRepository.getAssetByCategory(
              pageable,
              AssetType.valueOf(type),
              this.fetchStockSymbolsVN30(),
              false);
    } else {
      assetSummaries =
          assetRepository.getAllAssetSummary(
              pageable, AssetType.valueOf(type), query.toUpperCase(), query.isEmpty());
    }
    List<String> symbols =
        assetSummaries.getContent().stream().map(AssetEntity::getIdentity).toList();

    HashMap<String, Double> latestPrices =
        priceAssetRepositoryAdapter.getLatestPriceAssets(symbols);

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

  @Transactional
  public void createAssetsInBatch(List<Asset> assets) {
    List<AssetEntity> assetEntities = assets.stream().map(assetMapper::toAssetEntity).toList();

    assetRepository.saveAll(assetEntities);
  }

  public AssetEntity findAssetEntityById(UUID id) {
    return assetRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));
  }

  public AssetEntity findAssetEntityByIdentity(String identity) {
    return assetRepository
        .findByIdentity(identity)
        .orElseThrow(() -> new NotFoundException(MessageConstant.ASSET_NOT_FOUND));
  }

  public Map<String, Asset> findAllByIdentities(Set<String> identities) {
    List<AssetEntity> existingAssets = assetRepository.findByIdentityIn(identities);
    return existingAssets.stream()
        .collect(Collectors.toMap(AssetEntity::getIdentity, assetMapper::toAsset));
  }

  public List<String> fetchStockSymbolsVN30() {
    Request request = new Request.Builder().url(CommonConstant.STOCK_VN_30_URL).build();

    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      String responseBody = response.body().string();
      List<String> stockList = objectMapper.readValue(responseBody, List.class);

      return stockList.stream().map(String::toUpperCase).toList();

    } catch (IOException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }
}
