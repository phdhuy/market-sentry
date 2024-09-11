package com.phdhuy.stock_alert.ports.inbound.asset;

import com.phdhuy.stock_alert.payload.general.ResponseDataAPI;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetAllAssetUseCase {

  ResponseDataAPI getAllAsset(Pageable pageable, String type, List<String> query);
}
