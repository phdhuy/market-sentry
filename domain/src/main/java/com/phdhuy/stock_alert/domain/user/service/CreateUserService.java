package com.phdhuy.stock_alert.domain.user.service;

import com.phdhuy.stock_alert.domain.user.model.User;
import com.phdhuy.stock_alert.domain.user.ports.inbound.CreateUserUseCase;
import com.phdhuy.stock_alert.domain.user.ports.outbound.CreateUserPort;
import com.phdhuy.stock_alert.domain.user.ports.outbound.ExistsUserPort;
import com.phdhuy.stock_alert.shared.annotation.UseCase;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {

  private final CreateUserPort createUserPort;

  private final ExistsUserPort existsUserPort;


  @Override
  @Transactional
  public User createUser(String email, String password, String confirmPassword) {
    if (existsUserPort.existsByEmail(email)) {
      throw new BadRequestException(MessageConstant.EMAIL_ALREADY_EXISTS);
    }

    if (!this.isPasswordMatch(password, confirmPassword)) {
      throw new BadRequestException(MessageConstant.PASSWORD_NOT_MATCHED_WITH_CONFIRM_PASSWORD);
    }

    return createUserPort.createUser(email, password);
  }

  private boolean isPasswordMatch(String password, String confirmPassword) {
    return password.equals(confirmPassword);
  }
}
