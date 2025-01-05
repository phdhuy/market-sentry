package com.phdhuy.stock_alert.domain.user.ports.outbound;

import com.phdhuy.stock_alert.domain.user.model.User;
import java.util.UUID;

public interface UserRepositoryPort {

  User createUser(String email, String password);

  boolean existsByEmail(String email);

  User findByUserId(UUID userId);
}
