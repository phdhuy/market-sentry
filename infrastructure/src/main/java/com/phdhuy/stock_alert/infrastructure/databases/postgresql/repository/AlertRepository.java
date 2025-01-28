package com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository;

import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AlertEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, UUID> {

  @Query(value = "select * from alert a where a.user_id = :userId", nativeQuery = true)
  Page<AlertEntity> getMyAlert(Pageable pageable, UUID userId);

  @Query(
      "SELECT a FROM AlertEntity a JOIN FETCH a.assetEntity JOIN FETCH a.userEntity WHERE a.alertStatus = 'ACTIVE'")
  List<AlertEntity> getAlertActive();
}
