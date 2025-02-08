package org.training.api.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI apiGatewayOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("API Gateway Documentation")
            .description("API Gateway for microservices")
            .version("1.0"));
  }

//  @Bean
//  @Lazy(false)
//  public List<GroupedOpenApi> apis(
//      SwaggerUiConfigParameters swaggerUiConfigParameters,
//      RouteDefinitionLocator locator) {
//
//    List<GroupedOpenApi> groups = new ArrayList<>();
//    List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
//
//    definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
//      String name = routeDefinition.getId().replaceAll("-service", "");
//      swaggerUiConfigParameters.addGroup(name);
//      GroupedOpenApi.builder().pathsToMatch("/" + routeDefinition.getId() + "/**").group(name).build();
//    });
//
//    return groups;
//  }
}
