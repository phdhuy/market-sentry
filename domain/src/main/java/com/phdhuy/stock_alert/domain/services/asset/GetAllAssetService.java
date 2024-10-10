package com.phdhuy.stock_alert.domain.services.asset;

import com.phdhuy.stock_alert.shared.annotation.UseCase;
import com.phdhuy.stock_alert.domain.model.Asset;
import com.phdhuy.stock_alert.shared.payload.general.PageInfo;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import com.phdhuy.stock_alert.domain.ports.inbound.asset.GetAllAssetUseCase;
import com.phdhuy.stock_alert.domain.ports.outbound.asset.GetAllAssetPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetAllAssetService implements GetAllAssetUseCase {

  private final GetAllAssetPort getAllAssetPort;

  @Override
  public ResponseDataAPI getAllAsset(Pageable pageable, String type, List<String> query) {
    Page<Asset> assets = getAllAssetPort.getAllAsset(pageable, type, query);

    PageInfo pageInfo =
        new PageInfo(
            pageable.getPageNumber() + 1, assets.getTotalPages(), assets.getTotalElements());

    return ResponseDataAPI.success(assets.getContent().stream().toList(), pageInfo);
  }
}
