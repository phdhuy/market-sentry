package com.phdhuy.stock_alert.application.controller.asset;

import com.phdhuy.stock_alert.domain.asset.port.inbound.GetAllAssetUseCase;
import com.phdhuy.stock_alert.domain.asset.port.inbound.GetDetailAssetUseCase;
import com.phdhuy.stock_alert.domain.asset.port.inbound.GetPriceHistoryAssetUseCase;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import com.phdhuy.stock_alert.shared.utils.PagingUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/assets")
@RequiredArgsConstructor
@Tag(name = "Asset APIs")
public class AssetController {

  private final GetAllAssetUseCase getAllAssetUseCase;

  private final GetDetailAssetUseCase getDetailAssetUseCase;

  private final GetPriceHistoryAssetUseCase getPriceHistoryAssetUseCase;

  @GetMapping
  public ResponseEntity<ResponseDataAPI> getAllAsset(
      @RequestParam(name = "sort", defaultValue = "rank") String sortBy,
      @RequestParam(name = "order", defaultValue = "asc") String order,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "paging", defaultValue = "30") int paging,
      @RequestParam(name = "type", defaultValue = "CRYPTO") String type,
      @RequestParam(name = "q", defaultValue = "") List<String> q) {
    Pageable pageable = PagingUtils.makePageRequestWithSnakeCase(sortBy, order, page, paging);
    return ResponseEntity.ok(getAllAssetUseCase.getAllAsset(pageable, type, q));
  }

  @GetMapping("/{assetId}")
  public ResponseEntity<ResponseDataAPI> getDetailAsset(@PathVariable UUID assetId) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(getDetailAssetUseCase.getDetailAsset(assetId)));
  }

  @GetMapping("/{assetId}/history")
  public ResponseEntity<ResponseDataAPI> getPriceHistoryAsset(
      @PathVariable UUID assetId,
      @Parameter(
              description =
                  "Time interval for price history data. Valid values are '1m', '5m', '1h', '1d', etc.",
              schema =
                  @Schema(
                      type = "string",
                      allowableValues = {
                        "1m", "5m", "15m", "1h", "4h", "1d", "1w", "1mo", "6mo", "1y", "2y"
                      }))
          @RequestParam
          String interval) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            getPriceHistoryAssetUseCase.getPriceHistoryAsset(assetId, interval)));
  }
}
