package com.phdhuy.stock_alert.domain.ports.outbound.user;

import com.phdhuy.stock_alert.domain.model.User;

import java.util.UUID;

public interface FindUserByIdPort {

  User findByUserId(UUID userId);
}
