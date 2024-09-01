package com.phdhuy.stock_alert.model;

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
