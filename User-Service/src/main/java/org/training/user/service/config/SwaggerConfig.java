package org.training.user.service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Value("${server.servlet.context-path}")
  private String contextPath;

  @Bean
  public OpenAPI userServiceOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("User Service API")
            .description("User management and authentication endpoints")
            .version("1.0"))
        .addServersItem(new Server().url(contextPath + "/"));
  }
}
