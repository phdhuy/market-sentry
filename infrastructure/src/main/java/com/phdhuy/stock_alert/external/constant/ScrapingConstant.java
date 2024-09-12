package com.phdhuy.stock_alert.external.constant;

public class ScrapingConstant {
  private ScrapingConstant() {}

  public static final String LAST_PRICE_VALUE_XPATH = "//*[contains(@id, '_lastPrice_value')]";

  public static final String MARKET_STATUS_CLASS_NAME = "chart-footer-vn30";

  public static final String MARKET_STATUS_XPATH =
      "//div[contains(@class, 'chart-footer-vn30')]//div";

  public static final String MARKET_STATUS_IS_CLOSED = "Đóng cửa";

  public static final String MARKET_STATUS_IS_BREAK = "Nghỉ trưa";
}
