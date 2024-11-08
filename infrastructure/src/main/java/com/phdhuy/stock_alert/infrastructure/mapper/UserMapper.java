package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.domain.user.model.User;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserEntity;
import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

  User toUserDomain(UserEntity userEntity);
}
