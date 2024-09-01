package com.phdhuy.stock_alert.ports.outbound.asset;

import com.phdhuy.stock_alert.model.Asset;

import java.util.UUID;

public interface GetDetailAssetPort {

  Asset getDetailAsset(UUID id);
}
