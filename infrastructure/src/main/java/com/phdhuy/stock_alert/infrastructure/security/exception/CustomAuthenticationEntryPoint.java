package com.phdhuy.stock_alert.infrastructure.security.exception;

import com.phdhuy.stock_alert.infrastructure.security.utils.LogUtils;
import com.phdhuy.stock_alert.shared.common.CommonFunction;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.payload.error.ErrorResponse;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AuthenticationException e)
      throws IOException {
    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    httpServletResponse.setContentType("application/json");
    httpServletResponse.setCharacterEncoding("UTF-8");
    ErrorResponse error = CommonFunction.getExceptionError(MessageConstant.UNAUTHORIZED);
    ResponseDataAPI responseDataAPI = ResponseDataAPI.error(error);
    LogUtils.error(
        httpServletRequest.getMethod(),
        httpServletRequest.getRequestURL().toString(),
        e.getMessage());
    httpServletResponse
        .getWriter()
        .write(Objects.requireNonNull(CommonFunction.convertToJSONString(responseDataAPI)));
  }
}
