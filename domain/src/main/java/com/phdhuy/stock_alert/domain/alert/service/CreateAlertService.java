package com.phdhuy.stock_alert.domain.alert.service;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.ports.inbound.CreateAlertUseCase;
import com.phdhuy.stock_alert.domain.alert.ports.outbound.AlertRepositoryPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateAlertService implements CreateAlertUseCase {

  private final AlertRepositoryPort alertRepositoryPort;

  @Override
  public Alert createAlert(Alert alert, UUID userId, UUID assetId) {
    return alertRepositoryPort.createAlert(alert, userId, assetId);
  }
}
