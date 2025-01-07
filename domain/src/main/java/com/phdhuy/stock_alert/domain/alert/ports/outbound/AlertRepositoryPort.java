package com.phdhuy.stock_alert.domain.alert.ports.outbound;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AlertRepositoryPort {

  Alert createAlert(Alert alert, UUID userId, UUID assetId);

  Page<Alert> getMyAlert(Pageable pageable, UUID userId);

  Alert getDetailAlert(UUID alertId);
}
