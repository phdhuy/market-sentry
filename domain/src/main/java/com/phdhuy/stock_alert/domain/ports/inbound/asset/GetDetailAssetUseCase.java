package com.phdhuy.stock_alert.domain.ports.inbound.asset;


import com.phdhuy.stock_alert.domain.model.Asset;

import java.util.UUID;

public interface GetDetailAssetUseCase {

  Asset getDetailAsset(UUID asset);
}
