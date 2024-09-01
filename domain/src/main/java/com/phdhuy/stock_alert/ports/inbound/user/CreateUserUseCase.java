package com.phdhuy.stock_alert.ports.inbound.user;

import com.phdhuy.stock_alert.model.User;

public interface CreateUserUseCase {

  User createUser(String email, String password, String confirmPassword);
}
