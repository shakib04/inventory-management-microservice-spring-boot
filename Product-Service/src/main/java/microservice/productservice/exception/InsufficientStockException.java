package microservice.productservice.exception;

public class InsufficientStockException
    extends RuntimeException {
  public InsufficientStockException(String s) {
    super(s);
  }
}
