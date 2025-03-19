package com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository;

import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.enums.AssetType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, UUID> {

  boolean existsByIdentity(String identity);

  Optional<AssetEntity> findByIdentity(String identity);

  @Query(
      value =
          "SELECT a "
              + "FROM AssetEntity a "
              + "WHERE a.assetType = :assetType and (:isAll = true or a.symbol LIKE %:query%) ")
  Page<AssetEntity> getAllAssetSummary(
      Pageable pageable, AssetType assetType, String query, boolean isAll);

  @Query(
      value =
          "SELECT a "
              + "FROM AssetEntity a "
              + "WHERE a.assetType = :assetType and (:isAll = true or a.symbol IN (:query)) ")
  Page<AssetEntity> getAssetByCategory(
      Pageable pageable, AssetType assetType, List<String> query, boolean isAll);

  List<AssetEntity> findByIdentityIn(Set<String> identities);
}
