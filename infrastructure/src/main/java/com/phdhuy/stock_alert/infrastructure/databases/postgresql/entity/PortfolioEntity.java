package com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "portfolio")
public class PortfolioEntity extends BaseEntity {

  @Id @GeneratedValue private UUID id;

  @Column private String name;

  @Column private String avatar;

  @Column private boolean isDefault;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;
}
