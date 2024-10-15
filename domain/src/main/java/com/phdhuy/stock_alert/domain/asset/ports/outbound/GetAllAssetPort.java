package com.phdhuy.stock_alert.domain.asset.ports.outbound;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllAssetPort {

  Page<Asset> getAllAsset(Pageable pageable, String type, List<String> query);
}
