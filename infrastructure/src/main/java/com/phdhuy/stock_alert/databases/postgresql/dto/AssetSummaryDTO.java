package com.phdhuy.stock_alert.databases.postgresql.dto;

import java.util.UUID;

public interface AssetSummaryDTO {

  UUID getId();

  Long getRank();

  String getIdentity();

  String getSymbol();

  String getName();

  Double getSupply();

  Double getMaxSupply();

  Double getMarketCapUsd();

  Double getVolumeUsd24Hr();

  Double getChangePercent24Hr();

  Double getVwap24Hr();

  String getExplorer();
}
