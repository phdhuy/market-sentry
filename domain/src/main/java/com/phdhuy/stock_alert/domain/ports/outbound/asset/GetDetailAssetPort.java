package com.phdhuy.stock_alert.domain.ports.outbound.asset;

import com.phdhuy.stock_alert.domain.model.Asset;

import java.util.UUID;

public interface GetDetailAssetPort {

  Asset getDetailAsset(UUID id);
}
