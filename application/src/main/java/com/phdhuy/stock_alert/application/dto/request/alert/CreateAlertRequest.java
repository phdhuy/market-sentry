package com.phdhuy.stock_alert.application.dto.request.alert;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AlertConditionType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AlertType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.TriggerType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateAlertRequest {

  @NotBlank private AlertType alertType;

  @NotBlank private AlertConditionType alertConditionType;

  private Double value;

  @NotBlank private TriggerType triggerType;

  private Timestamp expirationAt;

  private UUID assetId;
}
