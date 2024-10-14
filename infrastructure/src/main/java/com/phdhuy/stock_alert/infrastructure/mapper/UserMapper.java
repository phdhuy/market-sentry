package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserEntity;
import com.phdhuy.stock_alert.domain.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

  @Mapping(source = "user.isConfirmed", target = "isConfirmed")
  UserEntity fromUserDomain(User user);

  User toUserDomain(UserEntity userEntity);
}
