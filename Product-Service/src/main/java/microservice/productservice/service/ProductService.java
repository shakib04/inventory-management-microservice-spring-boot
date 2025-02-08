package microservice.productservice.service;

import microservice.productservice.dto.AvailabilityResponse;
import microservice.productservice.dto.ProductDTO;
import microservice.productservice.dto.StockUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {
  ProductDTO createProduct(ProductDTO productDTO);

  ProductDTO updateProduct(String productId, ProductDTO productDTO);

  ProductDTO getProduct(String productId);

  void updateStock(String productId, StockUpdateRequest request);

  @Transactional(readOnly = true)
  AvailabilityResponse checkAvailability(String productId, Integer requestedQuantity);

  Page<ProductDTO> getProducts(Pageable pageable);
}
