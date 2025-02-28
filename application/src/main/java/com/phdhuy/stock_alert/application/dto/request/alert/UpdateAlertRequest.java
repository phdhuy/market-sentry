package com.phdhuy.stock_alert.application.dto.request.alert;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AlertConditionType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AlertType;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.TriggerType;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateAlertRequest {

  @NotNull private AlertType alertType;

  @NotNull private AlertConditionType alertConditionType;

  private Double value;

  @NotNull private TriggerType triggerType;

  private Timestamp expirationAt;
}
