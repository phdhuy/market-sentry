package com.phdhuy.stock_alert.application.dto.validator.impl;

import com.phdhuy.stock_alert.application.dto.validator.annotation.ValidAlertMethods;
import com.phdhuy.stock_alert.shared.common.CommonFunction;
import com.phdhuy.stock_alert.shared.payload.error.ErrorResponse;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;

public class AlertMethodValidator implements ConstraintValidator<ValidAlertMethods, List<String>> {
  private static final Set<String> VALID_METHODS = Set.of("EMAIL", "TELEGRAM");

  @Override
  public boolean isValid(List<String> alertMethods, ConstraintValidatorContext context) {
    if (alertMethods == null || alertMethods.isEmpty()) {
      return false;
    }

    for (String method : alertMethods) {
      if (!VALID_METHODS.contains(method)) {
        context.disableDefaultConstraintViolation();

        String fieldName = CommonFunction.convertToSnakeCase("alertMethodTypes");
        String error = CommonFunction.convertToSnakeCase("invalid_value");
        String resource = CommonFunction.convertToSnakeCase("createAlertRequest");

        ErrorResponse errorResponse = CommonFunction.getValidationError(resource, fieldName, error);
        ResponseDataAPI responseDataAPI = ResponseDataAPI.error(errorResponse);

        context
            .buildConstraintViolationWithTemplate(responseDataAPI.toString())
            .addConstraintViolation();

        return false;
      }
    }
    return true;
  }
}
