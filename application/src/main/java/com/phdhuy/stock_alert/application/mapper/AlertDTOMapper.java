package com.phdhuy.stock_alert.application.mapper;

import com.phdhuy.stock_alert.application.dto.request.alert.CreateAlertRequest;
import com.phdhuy.stock_alert.application.dto.response.alert.AlertInfoResponse;
import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface AlertDTOMapper {

  Alert toAlertFromAlertDTO(CreateAlertRequest alertRequest);

  @Mapping(source = "alert.id", target = "id")
  @Mapping(source = "alert.createdAt", target = "createdAt")
  @Mapping(source = "alert.updatedAt", target = "updatedAt")
  @Mapping(source = "alert.asset", target = "asset")
  AlertInfoResponse toAlertInfoResponse(Alert alert);
}
