package microservice.productservice.exception;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(String productId) {
  }
}
