package com.phdhuy.springhexagonaltemplate.domain.ports.outbound.asset;

import com.phdhuy.springhexagonaltemplate.domain.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllAssetPort {

  Page<Asset> getAllAsset(Pageable pageable);
}
