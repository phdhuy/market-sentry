package com.phdhuy.stock_alert.domain.user.ports.outbound;


import com.phdhuy.stock_alert.domain.user.model.User;

public interface CreateUserPort {
  User createUser(User user);
}
