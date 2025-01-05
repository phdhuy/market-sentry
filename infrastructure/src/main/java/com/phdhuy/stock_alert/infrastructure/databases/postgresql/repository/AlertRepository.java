package com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository;

import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AlertEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, UUID> {}
