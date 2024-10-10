package com.phdhuy.stock_alert.domain.ports.outbound.user;

import com.phdhuy.stock_alert.domain.model.User;

public interface CreateUserPort {
  User createUser(User user);
}
