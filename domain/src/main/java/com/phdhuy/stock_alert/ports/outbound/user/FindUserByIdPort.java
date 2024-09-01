package com.phdhuy.stock_alert.ports.outbound.user;

import com.phdhuy.stock_alert.model.User;

import java.util.UUID;

public interface FindUserByIdPort {

  User findByUserId(UUID userId);
}
