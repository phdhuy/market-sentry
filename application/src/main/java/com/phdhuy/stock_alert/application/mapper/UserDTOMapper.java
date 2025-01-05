package com.phdhuy.stock_alert.application.mapper;

import com.phdhuy.stock_alert.application.dto.response.user.UserInfoResponse;
import com.phdhuy.stock_alert.domain.user.model.User;
import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface UserDTOMapper {

  @Mapping(source = "user.isConfirmed", target = "isConfirmed")
  @Mapping(source = "user.confirmedAt", target = "confirmedAt")
  UserInfoResponse toUserInfoResponse(User user);
}
