package com.phdhuy.stock_alert.domain.ports.inbound.user;

import com.phdhuy.stock_alert.domain.model.User;

public interface CreateUserUseCase {

  User createUser(String email, String password, String confirmPassword);
}
