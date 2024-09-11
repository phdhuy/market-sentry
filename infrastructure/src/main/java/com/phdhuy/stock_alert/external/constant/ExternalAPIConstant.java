package com.phdhuy.stock_alert.external.constant;

public class ExternalAPIConstant {

  private ExternalAPIConstant() {}

  public static final String INFO_CRYPTO = "https://api.coincap.io/v2/assets?limit=2000";

  public static final String PRICE_CRYPTO = "wss://ws.coincap.io/prices?assets=ALL";

  public static final String PRICE_STOCK = "https://banggia.vps.com.vn/chung-khoan/VN30";

  public static final String IMAGE_STOCK_VN = "https://finance.vietstock.vn/image/";

  public static final String INFO_STOCK_VN =
      "https://api-finfo.vndirect.com.vn/v4/stocks?q=type:STOCK~status:LISTED&fields=code,companyName,companyNameEng,shortName,floor,industryName,status&size=2000";
}
