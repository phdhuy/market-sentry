package com.phdhuy.stock_alert.application.dto.response.asset;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

  private String floor;

  private String nameVn;

  private String assetType;

  private String logo;
}
