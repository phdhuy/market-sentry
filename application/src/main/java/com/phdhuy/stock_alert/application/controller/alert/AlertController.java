package com.phdhuy.stock_alert.application.controller.alert;

import com.phdhuy.stock_alert.application.dto.request.alert.CreateAlertRequest;
import com.phdhuy.stock_alert.application.mapper.AlertDTOMapper;
import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.domain.alert.ports.inbound.AlertUseCase;
import com.phdhuy.stock_alert.infrastructure.security.domain.UserPrincipal;
import com.phdhuy.stock_alert.shared.annotation.CurrentUser;
import com.phdhuy.stock_alert.shared.payload.general.PageInfo;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import com.phdhuy.stock_alert.shared.utils.PagingUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Tag(name = "Alert APIs")
public class AlertController {

  private final AlertUseCase alertUseCase;

  private final AlertDTOMapper alertDTOMapper;

  @PostMapping("/assets/{assetId}/alerts")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> createAlert(
      @PathVariable UUID assetId,
      @RequestBody @Valid CreateAlertRequest createAlertRequest,
      @CurrentUser UserPrincipal userPrincipal) {
    Alert alert =
        alertUseCase.createAlert(
            alertDTOMapper.toAlertFromAlertDTO(createAlertRequest), userPrincipal.getId(), assetId);
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(alertDTOMapper.toAlertInfoResponse(alert)));
  }

  @GetMapping("/alerts")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getMyAlert(
      @RequestParam(name = "sort", defaultValue = "createdAt") String sortBy,
      @RequestParam(name = "order", defaultValue = "asc") String order,
      @RequestParam(name = "page", defaultValue = "1") int page,
      @RequestParam(name = "paging", defaultValue = "30") int paging,
      @CurrentUser UserPrincipal userPrincipal) {
    Pageable pageable = PagingUtils.makePageRequestWithSnakeCase(sortBy, order, page, paging);

    Page<Alert> alerts = alertUseCase.getMyAlert(pageable, userPrincipal.getId());

    PageInfo pageInfo =
        new PageInfo(
            pageable.getPageNumber() + 1, alerts.getTotalPages(), alerts.getTotalElements());

    return ResponseEntity.ok(
        ResponseDataAPI.success(alerts.getContent().stream().toList(), pageInfo));
  }

  @GetMapping("/alerts/{alertId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getDetailAlert(
      @PathVariable UUID alertId, @CurrentUser UserPrincipal userPrincipal) {
    Alert alert = alertUseCase.getDetailAlert(alertId, userPrincipal.getId());
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(alertDTOMapper.toAlertInfoResponse(alert)));
  }

  @PutMapping("/alerts/{alertId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> updateAlert(
      @PathVariable UUID alertId,
      @RequestBody @Valid CreateAlertRequest createAlertRequest,
      @CurrentUser UserPrincipal userPrincipal) {
    Alert alert =
        alertUseCase.updateAlert(
            alertDTOMapper.toAlertFromAlertDTO(createAlertRequest), alertId, userPrincipal.getId());
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(alertDTOMapper.toAlertInfoResponse(alert)));
  }

  @DeleteMapping("/alerts/{alertId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> deleteAlert(
      @PathVariable UUID alertId, @CurrentUser UserPrincipal userPrincipal) {
    alertUseCase.deleteAlert(alertId, userPrincipal.getId());
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }

  @GetMapping("/test-flink")
  public ResponseEntity<ResponseDataAPI> testFlink() throws Exception {
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    // Define a simple Flink job (e.g., a word count)
    env.fromElements("Apache Flink is awesome", "Spring Boot integration")
        .flatMap(
            (String line, Collector<String> out) -> {
              for (String word : line.split("\\s+")) {
                out.collect(word);
              }
            })
        .returns(Types.STRING)
        .map(word -> Tuple2.of(word, 1))
        .returns(Types.TUPLE(Types.STRING, Types.INT))
        .keyBy(tuple -> tuple.f0)
        .sum(1)
        .print();

    env.execute("Flink Job in Spring Boot");
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }
}
