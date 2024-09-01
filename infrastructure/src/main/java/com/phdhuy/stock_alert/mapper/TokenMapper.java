package com.phdhuy.stock_alert.mapper;

import com.phdhuy.stock_alert.config.MapStructConfig;
import com.phdhuy.stock_alert.model.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface TokenMapper {

  @Mapping(source = "accessToken", target = "accessToken")
  @Mapping(source = "refreshToken", target = "refreshToken")
  @Mapping(source = "expiresIn", target = "expiresIn")
  Token toOauthAccessToken(String accessToken, String refreshToken, long expiresIn);
}
