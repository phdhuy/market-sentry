package com.phdhuy.stock_alert.databases.postgresql.repository;

import com.phdhuy.stock_alert.databases.postgresql.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  boolean existsByEmail(String email);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findById(UUID id);
}
