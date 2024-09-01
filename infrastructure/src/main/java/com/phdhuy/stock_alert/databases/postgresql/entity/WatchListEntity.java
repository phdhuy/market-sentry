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
@Table(name = "watch_list")
public class WatchListEntity extends BaseEntity {

  @Id @GeneratedValue private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  @ManyToOne
  @JoinColumn(name = "asset_id")
  private AssetEntity assetEntity;
}
