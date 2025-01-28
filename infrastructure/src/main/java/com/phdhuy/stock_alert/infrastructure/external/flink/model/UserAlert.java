package com.phdhuy.stock_alert.infrastructure.external.flink.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAlert implements Serializable {
  private String coin;
  private double threshold;
  private String condition;

  public UserAlert() {}

  public UserAlert(String coin, double threshold, String condition) {
    this.coin = coin;
    this.threshold = threshold;
    this.condition = condition;
  }
}
