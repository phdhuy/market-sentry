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
@Table(name = "stock")
public class StockEntity extends BaseEntity {

  @Id @GeneratedValue private UUID id;

  @Column private String symbol;

  @Column private String title;

  @Column private String industry;

  @Column private String highestPrice;

  @Column private String lowestPrice;

  @Column private String openPrice;

  @Column private String volume;

  @Column private String tradingStatus;
}
