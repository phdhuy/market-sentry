package com.phdhuy.stock_alert.shared.payload.general;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDataAPI {

  private static final String SUCCESS = "success";

  private static final String ERROR = "error";

  private String status;

  private Object data;

  private Object error;

  private Object meta;

  public static ResponseDataAPI success(Object data, Object meta) {
    return ResponseDataAPI.builder().status(SUCCESS).data(data).meta(meta).build();
  }

  public static ResponseDataAPI successWithoutMeta(Object data) {
    return ResponseDataAPI.builder().status(SUCCESS).data(data).build();
  }

  public static ResponseDataAPI successWithoutMetaAndData() {
    return ResponseDataAPI.builder().status(SUCCESS).build();
  }

  public static ResponseDataAPI error(Object error) {
    return ResponseDataAPI.builder().status(ERROR).error(error).build();
  }
}
