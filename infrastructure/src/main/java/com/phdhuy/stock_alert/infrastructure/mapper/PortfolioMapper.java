package com.phdhuy.stock_alert.infrastructure.mapper;

import com.phdhuy.stock_alert.shared.config.MapStructConfig;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.PortfolioEntity;
import com.phdhuy.stock_alert.domain.model.Portfolio;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface PortfolioMapper {

  PortfolioEntity fromPortfolioDomain(Portfolio portfolio);

  Portfolio toPortfolioDomain(PortfolioEntity portfolioEntity);
}
