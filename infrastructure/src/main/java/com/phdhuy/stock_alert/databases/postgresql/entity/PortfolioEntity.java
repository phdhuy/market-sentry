package com.phdhuy.stock_alert.databases.postgresql.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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
