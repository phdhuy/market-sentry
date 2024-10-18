package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.domain.auth.model.Token;
import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface TokenMapper {

  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  @Mapping(source = "expiresIn", target = "expiresIn")
  Token toOauthAccessToken(String accessToken, String refreshToken, long expiresIn);
}
