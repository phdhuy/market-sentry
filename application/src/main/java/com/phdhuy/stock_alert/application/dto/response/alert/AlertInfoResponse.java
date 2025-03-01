package com.phdhuy.stock_alert.application.dto.response.alert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.phdhuy.stock_alert.application.dto.response.asset.AssetInfoResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertInfoResponse {

  private UUID id;

  private Timestamp createdAt;

  private Timestamp updatedAt;

  private String alertType;

  private String alertConditionType;

  private Double value;

  private String triggerType;

  private Timestamp expirationAt;

  private String alertStatus;

  private List<String> alertMethodTypes;

  private AssetInfoResponse asset;
}
