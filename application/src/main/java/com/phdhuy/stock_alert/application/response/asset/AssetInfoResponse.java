package com.phdhuy.stock_alert.application.response.asset;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetInfoResponse {

  private UUID id;

  private String identity;

  private Long rank;

  private String symbol;

  private String name;

  private Double supply;

  private Double maxSupply;

  private Double marketCapUsd;

  private Double volumeUsd24Hr;

  private Double changePercent24Hr;

  private Double vwap24Hr;

  private String explorer;

  private Double currentPriceUsd;
}
