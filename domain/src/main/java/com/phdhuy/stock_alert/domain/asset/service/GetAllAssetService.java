package com.phdhuy.stock_alert.domain.asset.service;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.asset.ports.inbound.GetAllAssetUseCase;
import com.phdhuy.stock_alert.domain.asset.ports.outbound.AssetRepositoryPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import com.phdhuy.stock_alert.shared.payload.general.PageInfo;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@UseCase
@RequiredArgsConstructor
public class GetAllAssetService implements GetAllAssetUseCase {

  private final AssetRepositoryPort assetRepositoryPort;

  @Override
  public ResponseDataAPI getAllAsset(Pageable pageable, String type, List<String> query) {
    Page<Asset> assets = assetRepositoryPort.getAllAsset(pageable, type, query);

    PageInfo pageInfo =
        new PageInfo(
            pageable.getPageNumber() + 1, assets.getTotalPages(), assets.getTotalElements());

    return ResponseDataAPI.success(assets.getContent().stream().toList(), pageInfo);
  }
}
