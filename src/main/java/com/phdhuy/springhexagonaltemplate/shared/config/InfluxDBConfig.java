package com.phdhuy.springhexagonaltemplate.shared.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

  @Value("${influx.bucket}")
  private String bucket;

  @Value("${influx.org}")
  private String org;

  @Value("${influx.url}")
  private String url;

  @Value("${influx.token}")
  private String token;

  @Bean
  public InfluxDBClient configInfluxDbClient() {
    return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
  }
}
