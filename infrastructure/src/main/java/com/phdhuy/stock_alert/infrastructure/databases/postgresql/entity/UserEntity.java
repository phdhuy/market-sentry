package com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity;

import com.phdhuy.stock_alert.shared.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity {

  @Id @GeneratedValue private UUID id;

  @Email
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column private Timestamp confirmedAt;

  @Column private Boolean isConfirmed;

  @Enumerated(EnumType.STRING)
  @Column
  private Role role;
}
