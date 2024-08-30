package com.phdhuy.springhexagonaltemplate.infrastructure.databases.postgresql.repository;

import com.phdhuy.springhexagonaltemplate.infrastructure.databases.postgresql.entity.AssetEntity;
import com.phdhuy.springhexagonaltemplate.infrastructure.databases.postgresql.dto.AssetSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, UUID> {

  boolean existsByIdentity(String identity);

  Optional<AssetEntity> findByIdentity(String identity);

  @Query(
      value =
          "SELECT a.id AS id, a.identity as identity, a.symbol as symbol, "
              + "a.name as name, a.supply as supply, a.max_supply as maxSupply, a.rank as rank, "
              + "a.market_cap_usd as marketCapUsd, a.volume_usd24hr as volumeUsd24Hr, "
              + "a.change_percent24hr as changePercent24Hr, a.vwap24hr as vwap24Hr, "
              + "a.explorer as explorer "
              + "FROM asset a ",
      nativeQuery = true)
  Page<AssetSummaryDTO> getAllAssetSummary(Pageable pageable);
}
