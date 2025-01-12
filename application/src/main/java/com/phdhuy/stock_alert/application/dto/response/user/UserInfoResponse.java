package com.phdhuy.stock_alert.application.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {

  private UUID id;

  private String email;

  private String role;

  private Timestamp createdAt;

  private Boolean isConfirmed;

  private Timestamp confirmedAt;
}
