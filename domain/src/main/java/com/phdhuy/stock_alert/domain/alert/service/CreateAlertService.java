package com.phdhuy.stock_alert.domain.alert.service;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.ports.inbound.CreateAlertUseCase;
import com.phdhuy.stock_alert.domain.alert.ports.outbound.CreateAlertPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class CreateAlertService implements CreateAlertUseCase {

    private final CreateAlertPort createAlertPort;

    @Override
    public Alert createAlert(Alert alert, UUID userId, UUID assetId) {
        return createAlertPort.createAlert(alert, userId, assetId);
    }
}
