package com.phdhuy.stock_alert.ports.inbound.asset;


import com.phdhuy.stock_alert.model.Asset;

import java.util.UUID;

public interface GetDetailAssetUseCase {

  Asset getDetailAsset(UUID asset);
}
