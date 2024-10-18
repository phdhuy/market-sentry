package com.phdhuy.stock_alert.infrastructure.databases.influxdb.entity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

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
