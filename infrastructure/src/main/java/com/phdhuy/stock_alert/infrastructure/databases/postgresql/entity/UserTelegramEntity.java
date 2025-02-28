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
@Table(name = "user_telegram")
public class UserTelegramEntity extends BaseEntity {

  @Id @GeneratedValue private UUID id;

  @Column private String chatId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;
}
