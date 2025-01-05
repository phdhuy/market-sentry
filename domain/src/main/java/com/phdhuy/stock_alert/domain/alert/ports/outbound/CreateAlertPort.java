package com.phdhuy.stock_alert.domain.alert.ports.outbound;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import java.util.UUID;

public interface CreateAlertPort {

  Alert createAlert(Alert alert, UUID userId, UUID assetId);
}
