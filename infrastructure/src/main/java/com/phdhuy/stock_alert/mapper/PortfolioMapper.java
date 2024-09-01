package com.phdhuy.stock_alert.mapper;

import com.phdhuy.stock_alert.config.MapStructConfig;
import com.phdhuy.stock_alert.databases.postgresql.entity.PortfolioEntity;
import com.phdhuy.stock_alert.model.Portfolio;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface PortfolioMapper {

  PortfolioEntity fromPortfolioDomain(Portfolio portfolio);

  Portfolio toPortfolioDomain(PortfolioEntity portfolioEntity);
}
