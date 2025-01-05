package com.phdhuy.stock_alert.application.mapper;

import com.phdhuy.stock_alert.application.dto.request.alert.CreateAlertRequest;
import com.phdhuy.stock_alert.domain.alert.model.Alert;
import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface AlertDTOMapper {

  Alert toAlertFromAlertDTO(CreateAlertRequest alertRequest);
}
