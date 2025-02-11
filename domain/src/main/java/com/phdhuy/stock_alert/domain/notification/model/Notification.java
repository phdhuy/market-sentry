package com.phdhuy.stock_alert.domain.notification.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.phdhuy.stock_alert.domain.alert.model.Alert;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Notification {

  private UUID id;

  private Timestamp createdAt;

  private String content;

  @JsonProperty("is_read")
  private Boolean isRead;

  private Alert alert;
}
