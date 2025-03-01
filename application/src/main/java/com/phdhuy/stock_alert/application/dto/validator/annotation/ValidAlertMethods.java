package com.phdhuy.stock_alert.application.dto.validator.annotation;

import com.phdhuy.stock_alert.application.dto.validator.impl.AlertMethodValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlertMethodValidator.class)
public @interface ValidAlertMethods {
  String message() default "Invalid alert method";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
