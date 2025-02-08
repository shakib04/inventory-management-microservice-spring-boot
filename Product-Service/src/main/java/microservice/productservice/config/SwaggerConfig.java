package microservice.productservice.config;

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
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Product Management Service API")
            .description("API Documentation for Product Management System")
            .version("1.0"))
//        .contact(new Contact()
//            .name("Developer Name")
//            .email("developer-email@example.com"))
        .addServersItem(new Server().url(contextPath + "/"));
  }
}
