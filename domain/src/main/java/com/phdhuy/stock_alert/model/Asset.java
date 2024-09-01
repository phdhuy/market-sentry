package com.phdhuy.stock_alert.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Asset {

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

  private List<PriceAsset> priceAssets;
}
