package com.phdhuy.stock_alert.mapper;

import com.phdhuy.stock_alert.config.MapStructConfig;
import com.phdhuy.stock_alert.databases.postgresql.entity.UserEntity;
import com.phdhuy.stock_alert.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

  @Mapping(source = "user.isConfirmed", target = "isConfirmed")
  UserEntity fromUserDomain(User user);

  User toUserDomain(UserEntity userEntity);
}
