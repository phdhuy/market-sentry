package com.phdhuy.stock_alert.domain.alert.ports.inbound;

import com.phdhuy.stock_alert.domain.alert.model.Alert;

import java.util.UUID;

public interface CreateAlertUseCase {

  Alert createAlert(Alert alert, UUID userId, UUID asset);
}
