package com.phdhuy.stock_alert.databases.postgresql.entity;

import com.phdhuy.stock_alert.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

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
