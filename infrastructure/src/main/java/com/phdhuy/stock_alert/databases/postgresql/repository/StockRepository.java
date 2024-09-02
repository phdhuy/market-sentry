package com.phdhuy.stock_alert.databases.postgresql.repository;

import com.phdhuy.stock_alert.databases.postgresql.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, UUID> {

  Optional<StockEntity> findBySymbol(String symbol);

  boolean existsBySymbol(String symbol);
}
