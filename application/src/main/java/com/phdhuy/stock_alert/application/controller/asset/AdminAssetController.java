package com.phdhuy.stock_alert.application.controller.asset;

import com.phdhuy.stock_alert.infrastructure.external.adapter.InfoCryptoAdapter;
import com.phdhuy.stock_alert.infrastructure.external.adapter.InfoStockAdapter;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin/assets")
@RequiredArgsConstructor
@Tag(name = "Admin Asset APIs")
public class AdminAssetController {

  private final InfoCryptoAdapter infoCryptoAdapter;

  private final InfoStockAdapter infoStockAdapter;

  @PostMapping("/{assetType}/crawl")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ResponseDataAPI> crawlInfoAsset(@PathVariable String assetType) {
    if (assetType.equals("CRYPTO")) {
      infoCryptoAdapter.crawlDataCryptoAndSaveToDB();
    } else if (assetType.equals("STOCK")) {
      infoStockAdapter.crawlDataStockAndSaveToDB();
    }
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }

  @PutMapping("/{assetId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ResponseDataAPI> updateInfoAsset(@PathVariable UUID assetId) {
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }
}
