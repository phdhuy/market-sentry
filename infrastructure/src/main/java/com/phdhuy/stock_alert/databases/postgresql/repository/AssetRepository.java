package com.phdhuy.stock_alert.databases.postgresql.repository;

import com.phdhuy.stock_alert.databases.postgresql.entity.AssetEntity;
import java.util.Optional;
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
          "SELECT * "
                  + "FROM asset a "
                  + "WHERE a.asset_type = :assetType and (:query = 'ALL' or a.symbol like %:query%) ",
      nativeQuery = true)
  Page<AssetEntity> getAllAssetSummary(Pageable pageable, String assetType, String query);
}
