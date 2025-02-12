package com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository;

import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.NotificationEntity;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

  @Query(
      value =
          "select count(*) from notification n "
              + "where n.is_read = false and n.user_id = :userId",
      nativeQuery = true)
  Integer countUnreadNotification(UUID userId);

  @Modifying
  @Query(
      value =
          "update notification n set is_read = true"
              + " where n.id = :notificationId and n.user_id = :userId",
      nativeQuery = true)
  void markReadNotification(UUID notificationId, UUID userId);

  @Query(value = "select * from notification n where n.user_id = :userId", nativeQuery = true)
  Page<NotificationEntity> getMyNotification(Pageable pageable, UUID userId);

  @Override
  @Query(value = "SELECT n FROM NotificationEntity n JOIN FETCH n.alertEntity WHERE n.id = :notificationId")
  Optional<NotificationEntity> findById(UUID notificationId);
}
