package microservice.inventoryservice.feign;

import microservice.inventoryservice.dto.ProductDTO;
import microservice.inventoryservice.dto.StockUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductServiceClient {

  @GetMapping("/api/products/{productId}")
  ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") String productId);

  @PutMapping("/api/products/{productId}/stock")
  ResponseEntity<Void> updateStock(
      @PathVariable("productId") String productId,
      @RequestBody StockUpdateRequest request
  );
}
