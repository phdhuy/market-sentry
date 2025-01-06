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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/alerts")
@RequiredArgsConstructor
@Tag(name = "Alert APIs")
public class AlertController {

  private final AlertUseCase alertUseCase;

  private final AlertDTOMapper alertDTOMapper;

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> createAlert(
      @RequestBody @Valid CreateAlertRequest createAlertRequest,
      @CurrentUser UserPrincipal userPrincipal) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            alertUseCase.createAlert(
                alertDTOMapper.toAlertFromAlertDTO(createAlertRequest),
                userPrincipal.getId(),
                createAlertRequest.getAssetId())));
  }

  @GetMapping
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
}
