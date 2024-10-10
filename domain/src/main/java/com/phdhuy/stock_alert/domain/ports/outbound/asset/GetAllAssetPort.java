package com.phdhuy.stock_alert.domain.ports.outbound.asset;

import com.phdhuy.stock_alert.domain.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetAllAssetPort {

  Page<Asset> getAllAsset(Pageable pageable, String type, List<String> query);
}
