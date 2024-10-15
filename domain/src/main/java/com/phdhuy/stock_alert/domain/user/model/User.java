package com.phdhuy.stock_alert.domain.user.model;

import com.phdhuy.stock_alert.shared.enums.Role;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

  private UUID id;

  private String email;

  private String password;

  private Role role;

  private Boolean isConfirmed;

  private Timestamp createdAt;

  private Timestamp updatedAt;

  private Timestamp deletedAt;

  private Timestamp confirmedAt;
}
