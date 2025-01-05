package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.ports.outbound.AlertRepositoryPort;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter.asset.AssetRepositoryAdapter;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AlertEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AlertConditionType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AlertStatus;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AlertType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.TriggerType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.AlertRepository;
import com.phdhuy.stock_alert.infrastructure.mapper.AlertMapper;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class AlertRepositoryAdapter implements AlertRepositoryPort {

  private final AlertRepository alertRepository;

  private final UserRepositoryAdapter userRepositoryAdapter;

  private final AssetRepositoryAdapter assetRepositoryAdapter;

  private final AlertMapper alertMapper;

  @Override
  public Alert createAlert(Alert alert, UUID userId, UUID assetId) {
    AlertEntity alertEntity = new AlertEntity();

    alertEntity.setAlertType(AlertType.valueOf(alert.getAlertType()));
    alertEntity.setAlertConditionType(AlertConditionType.valueOf(alert.getAlertConditionType()));
    alertEntity.setAlertStatus(AlertStatus.ACTIVE);
    alertEntity.setValue(alert.getValue());
    alertEntity.setTriggerType(TriggerType.valueOf(alert.getTriggerType()));
    alertEntity.setExpirationAt(alert.getExpirationAt());

    alertEntity.setUserEntity(userRepositoryAdapter.findUserEntityById(userId));
    alertEntity.setAssetEntity(assetRepositoryAdapter.findAssetEntityById(assetId));

    return alertMapper.toAlertFromAlertEntity(alertRepository.save(alertEntity));
  }
}
