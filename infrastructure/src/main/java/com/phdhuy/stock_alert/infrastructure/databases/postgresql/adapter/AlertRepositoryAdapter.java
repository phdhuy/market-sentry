package com.phdhuy.stock_alert.infrastructure.databases.postgresql.adapter;

import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.port.outbound.AlertRepositoryPort;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AlertEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AlertConditionType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AlertStatus;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AlertType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.TriggerType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.AlertRepository;
import com.phdhuy.stock_alert.infrastructure.external.flink.dto.UserAlertActionMessage;
import com.phdhuy.stock_alert.infrastructure.external.messagebroker.RabbitMQAdapter;
import com.phdhuy.stock_alert.infrastructure.mapper.AlertMapper;
import com.phdhuy.stock_alert.shared.annotation.PersistenceAdapter;
import com.phdhuy.stock_alert.shared.common.CommonFunction;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.exception.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@RequiredArgsConstructor
public class AlertRepositoryAdapter implements AlertRepositoryPort {

  private final AlertRepository alertRepository;

  private final UserRepositoryAdapter userRepositoryAdapter;

  private final AssetRepositoryAdapter assetRepositoryAdapter;

  private final RabbitMQAdapter rabbitMQAdapter;

  private final AlertMapper alertMapper;

  @Override
  @Transactional
  public Alert createAlert(Alert alert, UUID userId, UUID assetId) {
    AlertEntity alertEntity = new AlertEntity();

    this.save(alert, alertEntity);
    alertEntity.setAlertStatus(AlertStatus.ACTIVE);
    alertEntity.setUserEntity(userRepositoryAdapter.findUserEntityById(userId));
    alertEntity.setAssetEntity(assetRepositoryAdapter.findAssetEntityById(assetId));

    alertRepository.save(alertEntity);

    Alert alertResponse =
        alertMapper.toAlert(alertEntity, alertEntity.getAssetEntity(), alertEntity.getUserEntity());
    rabbitMQAdapter.sendUserAlertMessage(
        UserAlertActionMessage.builder().action("ADD").data(alertResponse).build());
    return alertResponse;
  }

  @Override
  public Page<Alert> getMyAlert(Pageable pageable, UUID userId) {
    Page<AlertEntity> alertEntities = alertRepository.getMyAlert(pageable, userId);
    return alertEntities.map(alertMapper::toAlert);
  }

  @Override
  public Alert getDetailAlert(UUID alertId) {
    AlertEntity alertEntity = this.findById(alertId);
    return alertMapper.toAlert(
        alertEntity, alertEntity.getAssetEntity(), alertEntity.getUserEntity());
  }

  @Override
  public void deleteAlert(Alert alert) {
    AlertEntity alertEntity = this.findById(alert.getId());
    alertEntity.setDeletedAt(CommonFunction.getCurrentDateTime());
    alertRepository.save(alertEntity);
    rabbitMQAdapter.sendUserAlertMessage(
        UserAlertActionMessage.builder().action("DELETE").data(alert).build());
  }

  @Override
  public Alert updateAlert(Alert alert, Alert alertUpdate) {
    AlertEntity alertEntity = this.findById(alert.getId());

    this.save(alertUpdate, alertEntity);

    alertRepository.save(alertEntity);
    Alert alertResponse =
        alertMapper.toAlert(alertEntity, alertEntity.getAssetEntity(), alertEntity.getUserEntity());

    rabbitMQAdapter.sendUserAlertMessage(
        UserAlertActionMessage.builder().action("UPDATE").data(alertResponse).build());

    return alertResponse;
  }

  @Override
  public List<Alert> getListAlertActive() {
    List<AlertEntity> alertEntities = alertRepository.getAlertActive();
    return alertEntities.stream()
        .map(
            alertEntity ->
                alertMapper.toAlert(
                    alertEntity, alertEntity.getAssetEntity(), alertEntity.getUserEntity()))
        .toList();
  }

  @Override
  public void updateAlertStatus(Alert alert) {
    AlertEntity alertEntity = this.findById(alert.getId());

    alertEntity.setAlertStatus(AlertStatus.valueOf(alert.getAlertStatus()));
    alertRepository.save(alertEntity);

    if (alert.getAlertStatus().equals(AlertStatus.TRIGGERED.toString())) {
      rabbitMQAdapter.sendUserAlertMessage(
          UserAlertActionMessage.builder().action("DELETE").data(alert).build());
    }
  }

  private void save(Alert alert, AlertEntity alertEntity) {
    alertEntity.setAlertType(AlertType.valueOf(alert.getAlertType()));
    alertEntity.setAlertConditionType(AlertConditionType.valueOf(alert.getAlertConditionType()));
    alertEntity.setValue(alert.getValue());
    alertEntity.setTriggerType(TriggerType.valueOf(alert.getTriggerType()));
    alertEntity.setExpirationAt(alert.getExpirationAt());
    alertEntity.setAlertMethodTypes(alert.getAlertMethodTypes());
  }

  public AlertEntity findById(UUID alertId) {
    return alertRepository
        .findById(alertId)
        .orElseThrow(() -> new NotFoundException(MessageConstant.ALERT_NOT_FOUND));
  }
}
