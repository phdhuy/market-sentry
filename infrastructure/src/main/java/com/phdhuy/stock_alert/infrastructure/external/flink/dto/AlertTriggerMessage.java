package com.phdhuy.stock_alert.infrastructure.external.flink.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phdhuy.stock_alert.domain.alert.model.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertTriggerMessage {

  private Double priceTrigger;

  private String condition;

  private Alert alert;
}
