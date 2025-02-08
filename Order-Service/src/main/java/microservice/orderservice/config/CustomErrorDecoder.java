package microservice.orderservice.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import microservice.orderservice.exception.BadRequestException;
import microservice.orderservice.exception.ProductNotFoundException;
import microservice.orderservice.exception.ServiceUnavailableException;

public class CustomErrorDecoder implements ErrorDecoder {
  @Override
  public Exception decode(String methodKey, Response response) {
    switch (response.status()) {
      case 400:
        return new BadRequestException("Bad request");
      case 404:
        return new ProductNotFoundException("Product not found");
      case 503:
        return new ServiceUnavailableException("Service unavailable");
      default:
        return new Exception("Generic error");
    }
  }
}
