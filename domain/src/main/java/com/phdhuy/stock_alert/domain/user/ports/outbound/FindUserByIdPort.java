package com.phdhuy.stock_alert.domain.user.ports.outbound;

import com.phdhuy.stock_alert.domain.user.model.User;

import java.util.UUID;

public interface FindUserByIdPort {

  User findByUserId(UUID userId);
}
