package com.phdhuy.stock_alert.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    String schemeName = "bearerAuth";

    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes(
                    schemeName,
                    new SecurityScheme()
                        .name(schemeName)
                        .type(HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
        .info(apiInfo())
        .addSecurityItem(new SecurityRequirement().addList(schemeName));
  }

  private Info apiInfo() {
    return new Info().title("Spring Hexagonal Template").version("1.0.0");
  }
}
