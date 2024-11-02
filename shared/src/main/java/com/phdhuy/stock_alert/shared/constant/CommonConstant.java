package com.phdhuy.stock_alert.shared.constant;

public final class CommonConstant {
  private CommonConstant() {}

  public static final String SUCCESS = "success";

  public static final String ERROR = "error";

  public static final String SERVER = "Server";

  public static final String AUTHORIZATION = "Authorization";

  public static final String BEARER = "Bearer ";

  public static final String RULE_PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,32})";

  public static final String BEARER_AUTH = "bearerAuth";

  public static final String INFO_CRYPTO = "https://api.coincap.io/v2/assets?limit=2000";

  public static final String PRICE_CRYPTO = "wss://ws.coincap.io/prices?assets=ALL";

  public static final String PRICE_STOCK = "https://banggia.vps.com.vn/chung-khoan/VN30";

  public static final String IMAGE_STOCK_VN = "https://finance.vietstock.vn/image/";

  public static final String INFO_STOCK_VN =
      "https://api-finfo.vndirect.com.vn/v4/stocks?q=type:STOCK~status:LISTED&fields=code,companyName,companyNameEng,shortName,floor,industryName,status&size=2000";

  public static final String LAST_PRICE_VALUE_XPATH = "//*[contains(@id, '_lastPrice_value')]";

  public static final String VALUE = "_value";

  public static final String ZONE_ID = "Asia/Bangkok";
}
