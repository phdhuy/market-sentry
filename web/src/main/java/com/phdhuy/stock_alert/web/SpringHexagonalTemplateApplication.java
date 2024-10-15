package com.phdhuy.stock_alert.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.phdhuy.stock_alert.*"})
@EntityScan(basePackages = {"com.phdhuy.stock_alert.*"})
@EnableJpaRepositories(basePackages = {"com.phdhuy.stock_alert.*"})
public class SpringHexagonalTemplateApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringHexagonalTemplateApplication.class, args);
  }
}
