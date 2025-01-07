package com.phdhuy.stock_alert.domain.alert.service;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.ports.inbound.AlertUseCase;
import com.phdhuy.stock_alert.domain.alert.ports.outbound.AlertRepositoryPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import java.util.UUID;

import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@UseCase
@RequiredArgsConstructor
public class AlertService implements AlertUseCase {

  private final AlertRepositoryPort alertRepositoryPort;

  @Override
  public Alert createAlert(Alert alert, UUID userId, UUID assetId) {
    return alertRepositoryPort.createAlert(alert, userId, assetId);
  }

  @Override
  public Page<Alert> getMyAlert(Pageable pageable, UUID userId) {
    return alertRepositoryPort.getMyAlert(pageable, userId);
  }

  @Override
  public Alert getDetailAlert(UUID alertId, UUID userId) {
    Alert alert = alertRepositoryPort.getDetailAlert(alertId);

    if(!alert.getUser().getId().equals(userId)) {
      throw new ForbiddenException(MessageConstant.FORBIDDEN);
    }
    return alert;
  }
}
