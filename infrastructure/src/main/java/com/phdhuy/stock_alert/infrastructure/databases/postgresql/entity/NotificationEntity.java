package com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class NotificationEntity extends BaseEntity {

  @Id @GeneratedValue private UUID id;

  @Column private Boolean isRead = false;

  @Column private String content;

  @ManyToOne
  @JoinColumn(name = "alert_id")
  private AlertEntity alertEntity;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;
}
