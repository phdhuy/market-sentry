package com.phdhuy.stock_alert.domain.ports.inbound.asset;

import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetAllAssetUseCase {

  ResponseDataAPI getAllAsset(Pageable pageable, String type, List<String> query);
}
