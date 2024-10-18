package com.phdhuy.stock_alert.domain.asset.ports.inbound;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import java.util.UUID;

public interface GetDetailAssetUseCase {

  Asset getDetailAsset(UUID asset);
}
