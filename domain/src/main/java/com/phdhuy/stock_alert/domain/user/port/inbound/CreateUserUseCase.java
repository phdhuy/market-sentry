package com.phdhuy.stock_alert.domain.user.port.inbound;

import com.phdhuy.stock_alert.domain.user.model.User;

public interface CreateUserUseCase {

  User createUser(String email, String password, String confirmPassword);
}
