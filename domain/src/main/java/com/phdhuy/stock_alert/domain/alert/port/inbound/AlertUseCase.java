package com.phdhuy.stock_alert.domain.alert.port.inbound;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AlertUseCase {

  Alert createAlert(Alert alert, UUID userId, UUID assetId);

  Page<Alert> getMyAlert(Pageable pageable, UUID userId);

  Alert getDetailAlert(UUID alertId, UUID userId);

  void deleteAlert(UUID alertId, UUID userId);

  Alert updateAlert(Alert alertUpdate, UUID alertId, UUID userId);
}
