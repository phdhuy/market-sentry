package com.phdhuy.stock_alert.infrastructure.external.flink.datasource;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.ports.outbound.AlertRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertDatabaseSource extends RichSourceFunction<List<Alert>> {

    private final AlertRepositoryPort alertRepositoryPort;

    private volatile boolean running = true;


    @Override
    public void run(SourceContext<List<Alert>> ctx) throws Exception {
        while (running) {
            List<Alert> alerts = alertRepositoryPort.getListAlertActive();
            ctx.collect(alerts);
            Thread.sleep(30000);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}

