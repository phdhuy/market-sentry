package com.phdhuy.stock_alert.domain.asset.port.inbound;

import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface GetAllAssetUseCase {

  ResponseDataAPI getAllAsset(Pageable pageable, String type, List<String> query);
}
