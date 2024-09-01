package com.phdhuy.stock_alert.ports.outbound.asset;

import com.phdhuy.stock_alert.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllAssetPort {

  Page<Asset> getAllAsset(Pageable pageable);
}
