package org.training.api.gateway.auth;

import java.util.List;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
  private final JwtUtil jwtUtil;

  public AuthFilter(JwtUtil jwtUtil) {
    super(Config.class);
    this.jwtUtil = jwtUtil;
  }

  private final List<String> allowedPaths = List.of(
      "/user-service/api/auth/authenticate",
      "/user-service/api/auth/register",
      "api-docs"
  );

  @Override
  public GatewayFilter apply(Config config) {
    return ((exchange, chain) -> {
      String path = exchange.getRequest().getURI().getPath();

      // Skip auth for allowed paths
      if (allowedPaths.stream().anyMatch(path::contains)) {
        return chain.filter(exchange);
      }

      // Rest of the auth logic
      if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
        return handleUnauthorized(exchange);
      }

      String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
      String token = authHeader.substring(7);
      try {
        jwtUtil.getClaims(token);
      } catch (Exception e) {
        return handleUnauthorized(exchange);
      }
      return chain.filter(exchange);
    });
  }

  private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    String errorJson = "{\"message\": \"Unauthorized access\"}";
    DataBuffer buffer = response.bufferFactory().wrap(errorJson.getBytes());

    return response.writeWith(Mono.just(buffer));
  }

  public static class Config {}
}
