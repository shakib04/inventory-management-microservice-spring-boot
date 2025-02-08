package microservice.productservice.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import microservice.productservice.dto.AvailabilityResponse;
import microservice.productservice.dto.ProductDTO;
import microservice.productservice.dto.StockUpdateRequest;
import microservice.productservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
    return ResponseEntity.ok(productService.createProduct(productDTO));
  }

  @PutMapping("/{productId}")
  public ResponseEntity<ProductDTO> updateProduct(@PathVariable String productId,
                                                  @RequestBody ProductDTO productDTO) {
    return ResponseEntity.ok(productService.updateProduct(productId, productDTO));
  }

  @GetMapping("")
  public ResponseEntity<Page<ProductDTO>> getProducts(Pageable pageable) {
    return ResponseEntity.ok(productService.getProducts(pageable));
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductDTO> getProduct(@PathVariable String productId) {
    return ResponseEntity.ok(productService.getProduct(productId));
  }

  @PutMapping("/{productId}/stock")
  public ResponseEntity<Void> updateStock(
      @PathVariable String productId,
      @RequestBody StockUpdateRequest request) {
    productService.updateStock(productId, request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{productId}/availability")
  public ResponseEntity<AvailabilityResponse> checkAvailability(
      @PathVariable String productId,
      @RequestParam Integer quantity) {
    return ResponseEntity.ok(productService.checkAvailability(productId, quantity));
  }
}