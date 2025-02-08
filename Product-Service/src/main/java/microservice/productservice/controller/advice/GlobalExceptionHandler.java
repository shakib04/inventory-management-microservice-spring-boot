package microservice.productservice.controller.advice;

import lombok.AllArgsConstructor;
import lombok.Data;
import microservice.productservice.exception.InsufficientStockException;
import microservice.productservice.exception.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
    return ResponseEntity.notFound()
        .build();
  }

  @ExceptionHandler(InsufficientStockException.class)
  public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException ex) {
    return ResponseEntity.badRequest()
        .body(new ErrorResponse(ex.getMessage()));
  }

  @Data
  @AllArgsConstructor
  public static class ErrorResponse {
    private String message;
  }
}