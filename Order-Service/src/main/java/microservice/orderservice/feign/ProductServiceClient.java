package microservice.orderservice.feign;

import microservice.orderservice.dto.AvailabilityResponse;
import microservice.orderservice.dto.ProductDTO;
import microservice.orderservice.dto.StockUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "${product.service.name}")
// url = "${product.service.url}"
public interface ProductServiceClient {

  @GetMapping("/api/products/{productId}")
  ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") String productId);

  @PutMapping("/api/products/{productId}/stock")
  ResponseEntity<Void> updateStock(
      @PathVariable("productId") String productId,
      @RequestBody StockUpdateRequest request
  );

  @GetMapping("/api/products/{productId}/availability")
  ResponseEntity<AvailabilityResponse> checkAvailability(
      @PathVariable("productId") String productId,
      @RequestParam("quantity") Integer quantity
  );
}
