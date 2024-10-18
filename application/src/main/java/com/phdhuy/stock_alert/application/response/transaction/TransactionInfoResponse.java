package com.phdhuy.stock_alert.application.response.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.phdhuy.stock_alert.application.response.asset.AssetInfoResponse;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionInfoResponse {

  private UUID id;

  private int quantity;

  private Double pricePerUnit;

  private Timestamp transactionAt;

  private Double fee;

  private Double total;

  private AssetInfoResponse asset;
}
