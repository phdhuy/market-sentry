package com.phdhuy.springhexagonaltemplate.infrastructure.databases.postgresql.adapter.asset;

import com.phdhuy.springhexagonaltemplate.domain.model.Asset;
import com.phdhuy.springhexagonaltemplate.domain.ports.outbound.asset.GetAllAssetPort;
import com.phdhuy.springhexagonaltemplate.domain.ports.outbound.asset.GetLatestPriceAssetPort;
import com.phdhuy.springhexagonaltemplate.infrastructure.databases.postgresql.dto.AssetSummaryDTO;
import com.phdhuy.springhexagonaltemplate.infrastructure.databases.postgresql.repository.AssetRepository;
import com.phdhuy.springhexagonaltemplate.infrastructure.mapper.AssetMapper;
import com.phdhuy.springhexagonaltemplate.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
@Slf4j
public class GetAllAssetAdapter implements GetAllAssetPort {

  private final AssetRepository assetRepository;

  private final GetLatestPriceAssetPort getLatestPriceAssetPort;

  private final AssetMapper assetMapper;

  @Override
  public Page<Asset> getAllAsset(Pageable pageable) {
    Page<AssetSummaryDTO> assetSummaries = assetRepository.getAllAssetSummary(pageable);
    List<String> symbols =
        assetSummaries.getContent().stream().map(AssetSummaryDTO::getIdentity).toList();

    HashMap<String, Double> latestPrices = getLatestPriceAssetPort.getLatestPriceAssets(symbols);

    List<Asset> assets =
        assetSummaries.getContent().stream()
            .map(summary -> mapToAsset(summary, latestPrices))
            .toList();

    return new PageImpl<>(assets, pageable, assetSummaries.getTotalElements());
  }

  private Asset mapToAsset(AssetSummaryDTO assetSummaryDTO, HashMap<String, Double> latestPrices) {
    Double latestPrice = latestPrices.get(assetSummaryDTO.getIdentity());
    return assetMapper.toAssetFromProjection(assetSummaryDTO, latestPrice != null ? latestPrice : 0.0);
  }
}
