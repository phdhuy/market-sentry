package com.phdhuy.stock_alert.domain.user.port.inbound;

import com.phdhuy.stock_alert.domain.user.model.User;
import java.util.UUID;

public interface FindUserByIdUseCase {

  User findById(UUID id);
}
