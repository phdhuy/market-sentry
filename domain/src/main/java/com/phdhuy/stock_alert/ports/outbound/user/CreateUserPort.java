package com.phdhuy.stock_alert.ports.outbound.user;

import com.phdhuy.stock_alert.model.User;

public interface CreateUserPort {
  User createUser(User user);
}
