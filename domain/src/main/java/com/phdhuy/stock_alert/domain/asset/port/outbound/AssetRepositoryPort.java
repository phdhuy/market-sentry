package com.phdhuy.stock_alert.domain.asset.port.outbound;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetRepositoryPort {

  boolean existsByIdentity(String identity);

  Page<Asset> getAllAsset(Pageable pageable, String type, String query);

  Asset getDetailAsset(UUID id);
}
