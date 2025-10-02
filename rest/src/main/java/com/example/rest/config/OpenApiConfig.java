package com.example.rest.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public GroupedOpenApi calcApi() {
    return GroupedOpenApi.builder()
        .group("calc")
        .packagesToScan("com.example.rest.web")
        .build();
  }
}
