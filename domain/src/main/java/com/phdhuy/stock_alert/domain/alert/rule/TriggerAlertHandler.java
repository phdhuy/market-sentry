package com.phdhuy.stock_alert.domain.alert.rule;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.port.outbound.AlertRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TriggerAlertHandler implements AlertHandler{

    private final AlertRepositoryPort alertRepositoryPort;

    @Override
    public void setNext(AlertHandler nextHandler) {
        // last handler
    }

    @Override
    public boolean handle(Alert alert, double currentPrice) {
        if (alert.getAlertStatus().equals("ACTIVE")) {
            alert.setAlertStatus("TRIGGERED");
            alertRepositoryPort.updateAlertStatus(alert);
            return true;
        }

        return false;
    }
}
