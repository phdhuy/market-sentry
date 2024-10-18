package com.phdhuy.stock_alert.domain.asset.ports.outbound;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import java.util.UUID;

public interface GetDetailAssetPort {

  Asset getDetailAsset(UUID id);
}
