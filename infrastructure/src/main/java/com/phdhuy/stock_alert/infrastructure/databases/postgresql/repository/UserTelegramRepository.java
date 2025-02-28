package com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository;

import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserTelegramEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTelegramRepository extends JpaRepository<UserTelegramEntity, UUID> {

  List<UserTelegramEntity> findByUserEntityId(UUID userEntityId);
}
