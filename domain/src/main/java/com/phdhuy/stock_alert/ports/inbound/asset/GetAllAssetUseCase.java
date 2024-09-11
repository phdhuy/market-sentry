package com.phdhuy.stock_alert.ports.inbound.asset;

import com.phdhuy.stock_alert.payload.general.ResponseDataAPI;
import org.springframework.data.domain.Pageable;

public interface GetAllAssetUseCase {

  ResponseDataAPI getAllAsset(Pageable pageable, String type, String query);
}
