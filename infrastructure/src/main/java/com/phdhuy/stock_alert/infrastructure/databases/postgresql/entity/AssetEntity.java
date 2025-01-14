package com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity;

import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AssetType;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asset")
public class AssetEntity extends BaseEntity {

  @Id @GeneratedValue private UUID id;

  @Column private String identity;

  @Column private String symbol;

  @Column private String name;

  @Column private String explorer;

  @Column
  @Enumerated(EnumType.STRING)
  private AssetType assetType;

  @Column private String logo;
}
