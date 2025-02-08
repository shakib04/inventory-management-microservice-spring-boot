package microservice.inventoryservice.exception;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(String productId) {
  }
}
