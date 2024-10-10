package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import com.phdhuy.stock_alert.domain.model.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface TokenMapper {

  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  @Mapping(source = "expiresIn", target = "expiresIn")
  Token toOauthAccessToken(String accessToken, String refreshToken, long expiresIn);
}
