package com.phdhuy.stock_alert.databases.influxdb.entity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Measurement(name = "price_asset")
public class PriceAssetEntity {

  @Column(tag = true)
  private String name;

  @Column(tag = true)
  private String symbol;

  @Column private Double price;

  @Column(timestamp = true)
  private Instant time;
}
