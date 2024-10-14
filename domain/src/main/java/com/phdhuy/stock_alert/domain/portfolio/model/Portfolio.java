package com.phdhuy.stock_alert.domain.portfolio.model;

import com.phdhuy.stock_alert.domain.transaction.model.Transaction;
import com.phdhuy.stock_alert.domain.user.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Portfolio {

  private UUID id;

  private String name;

  private String avatar;

  private boolean isDefault;

  private List<Transaction> transactions;

  private User user;
}
