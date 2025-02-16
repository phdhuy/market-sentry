package com.phdhuy.stock_alert.domain.alert.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.user.model.User;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.exception.ForbiddenException;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Alert {

  private UUID id;

  private Timestamp createdAt;

  private Timestamp updatedAt;

  private String alertType;

  private String alertConditionType;

  private Double value;

  private String triggerType;

  private Timestamp expirationAt;

  private String alertStatus;

  private Asset asset;

  private User user;

  public void isOwner(UUID userId) {
    if (this.user == null || !this.user.getId().equals(userId)) {
      throw new ForbiddenException(MessageConstant.FORBIDDEN);
    }
  }

  public boolean isTriggered() {
    return alertStatus.equals("TRIGGERED");
  }
}
