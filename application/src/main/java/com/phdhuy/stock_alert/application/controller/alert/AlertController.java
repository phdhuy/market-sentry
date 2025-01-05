package com.phdhuy.stock_alert.application.controller.alert;

import com.phdhuy.stock_alert.application.dto.request.alert.CreateAlertRequest;
import com.phdhuy.stock_alert.application.mapper.AlertDTOMapper;
import com.phdhuy.stock_alert.domain.alert.ports.inbound.CreateAlertUseCase;
import com.phdhuy.stock_alert.infrastructure.security.domain.UserPrincipal;
import com.phdhuy.stock_alert.shared.annotation.CurrentUser;
import com.phdhuy.stock_alert.shared.payload.general.ResponseDataAPI;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/alerts")
@RequiredArgsConstructor
@Tag(name = "Alert APIs")
public class AlertController {

  private final AlertDTOMapper alertDTOMapper;

  private final CreateAlertUseCase createAlertUseCase;

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> createAlert(
      @RequestBody @Valid CreateAlertRequest createAlertRequest,
      @CurrentUser UserPrincipal userPrincipal) {
    return ResponseEntity.ok(
        ResponseDataAPI.successWithoutMeta(
            createAlertUseCase.createAlert(
                alertDTOMapper.toAlertFromAlertDTO(createAlertRequest),
                userPrincipal.getId(),
                createAlertRequest.getAssetId())));
  }

  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseDataAPI> getMyAlert(@CurrentUser UserPrincipal userPrincipal) {
    return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
  }
}
