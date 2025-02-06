package com.phdhuy.stock_alert.infrastructure.databases.influxdb.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
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
    InfluxDBClientOptions options =
        InfluxDBClientOptions.builder()
            .url(url)
            .authenticateToken(token.toCharArray())
            .okHttpClient(
                new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS))
            .org(org)
            .bucket(bucket)
            .build();

    return InfluxDBClientFactory.create(options);
  }
}
