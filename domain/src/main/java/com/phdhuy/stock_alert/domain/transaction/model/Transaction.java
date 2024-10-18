package com.phdhuy.stock_alert.domain.transaction.model;

import com.phdhuy.stock_alert.domain.asset.model.Asset;
import com.phdhuy.stock_alert.domain.transaction.model.enums.TransactionType;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {

  private UUID id;

  private Timestamp createdAt;

  private TransactionType transactionType;

  private int quantity;

  private Double pricePerUnit;

  private Timestamp transactionAt;

  private Double fee;

  private Double total;

  private Asset asset;
}
