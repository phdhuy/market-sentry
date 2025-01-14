package com.phdhuy.stock_alert.domain.asset.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Asset {

  private UUID id;

  private String identity;

  private String symbol;

  private String name;

  private String explorer;

  private Double currentPriceUsd;

  private String assetType;

  private String logo;

  private List<PriceAsset> priceAssets;
}
