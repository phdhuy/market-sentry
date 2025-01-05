package com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums;

public enum AlertConditionType {
  CROSSING,
  CROSSING_UP,
  CROSSING_DOWN,
  GREATER_THAN,
  LESS_THAN,
  MOVING_UP_PERCENTAGE,
  MOVING_DOWN_PERCENTAGE
}
