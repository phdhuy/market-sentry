package com.phdhuy.springhexagonaltemplate.shared.config;

import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.SqlStorageProvider;
import org.jobrunr.storage.sql.postgres.PostgresStorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class JobRunrConfig {

  @Value("${spring.datasource.url}")
  private String jdbcUrl;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Bean
  public StorageProvider storageProvider(JobMapper jobMapper) {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(jdbcUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    SqlStorageProvider sqlStorageProvider = new PostgresStorageProvider(dataSource);
    sqlStorageProvider.setJobMapper(jobMapper);
    return sqlStorageProvider;
  }
}
