package com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity;

import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.*;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Type;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alert")
@SQLRestriction("deleted_at is NULL")
public class AlertEntity extends BaseEntity {

  @Id @GeneratedValue private UUID id;

  @Column
  @Enumerated(EnumType.STRING)
  private AlertType alertType;

  @Column
  @Enumerated(EnumType.STRING)
  private AlertConditionType alertConditionType;

  @Column private Double value;

  @Column
  @Enumerated(EnumType.STRING)
  private TriggerType triggerType;

  @Column private Timestamp expirationAt;

  @Column
  @Enumerated(EnumType.STRING)
  private AlertStatus alertStatus;

  @Type(ListArrayType.class)
  @Column(columnDefinition = "text[]")
  private List<String> alertMethodTypes;

  @ManyToOne
  @JoinColumn(name = "asset_id")
  private AssetEntity assetEntity;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;
}
