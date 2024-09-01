package com.phdhuy.stock_alert.utils;

import com.phdhuy.stock_alert.common.CommonFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CaseUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Slf4j
public final class PagingUtils {

  public static final String ASC = "asc";

  public static final String DESC = "desc";

  private PagingUtils() {}

  public static Pageable makePageRequest(String sortBy, String order, int page, int paging) {
    String sortField = CaseUtils.toCamelCase(sortBy, false, '_');
    Sort sort =
        ASC.equalsIgnoreCase(order)
            ? Sort.by(sortField).ascending()
            : Sort.by(sortField).descending();
    return PageRequest.of(page - 1, paging, sort);
  }

  public static Pageable makePageRequestWithSnakeCase(
      String sortBy, String order, int page, int paging) {
    String sortField = CommonFunction.convertToSnakeCase(sortBy);
    Sort sort =
        ASC.equalsIgnoreCase(order)
            ? Sort.by(sortField).ascending()
            : Sort.by(sortField).descending();
    return PageRequest.of(page - 1, paging, sort);
  }
}
