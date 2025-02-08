package microservice.productservice.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microservice.productservice.config.RabbitMQConfig;
import microservice.productservice.dto.AvailabilityResponse;
import microservice.productservice.dto.ProductDTO;
import microservice.productservice.dto.StockUpdateRequest;
import microservice.productservice.entity.Product;
import microservice.productservice.event.dto.NewProductEvent;
import microservice.productservice.exception.InsufficientStockException;
import microservice.productservice.exception.ProductNotFoundException;
import microservice.productservice.mapper.ProductMapper;
import microservice.productservice.repository.ProductRepository;
import microservice.productservice.service.ProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final RabbitTemplate rabbitTemplate;

  @Override
  public ProductDTO createProduct(ProductDTO productDTO) {
    Product product = productMapper.toEntity(productDTO);
    Product savedProduct = productRepository.save(product);

// Publish event for inventory creation
    NewProductEvent event = new NewProductEvent(
        product.getId(),
        product.getName(),
        10,  // default reorder level
        20,  // default reorder quantity
        LocalDateTime.now()
    );
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.EXCHANGE,
        RabbitMQConfig.ROUTING_KEY,
        event);

    return productMapper.toDTO(savedProduct);
  }

  @CacheEvict(value = "products", key = "#productId")
  @Override
  public ProductDTO updateProduct(String productId, ProductDTO productDTO) {
    // Update product and cache will be automatically invalidated
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    // Update logic here

    productMapper.updateProductFromRequest(productDTO, product);
    return productMapper.toDTO(productRepository.save(product));
  }

  @Override
  @Cacheable(value = "products", key = "#productId")
  public ProductDTO getProduct(String productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    return productMapper.toDTO(product);
  }

  @Override
  public void updateStock(String productId, StockUpdateRequest request) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

    int newStock;
    switch (request.getUpdateType()) {
      case ADD:
        newStock = product.getCurrentStock() + request.getQuantity();
        break;
      case SUBTRACT:
        newStock = product.getCurrentStock() - request.getQuantity();
        if (newStock < 0) {
          throw new InsufficientStockException("Insufficient stock for product: " + productId);
        }
        break;
      case SET:
        newStock = request.getQuantity();
        break;
      default:
        throw new IllegalArgumentException("Invalid stock update type");
    }

    product.setCurrentStock(newStock);
    productRepository.save(product);

    // Log stock movement
    log.info("Stock updated for product {}: {} -> {} ({})",
        productId, product.getCurrentStock(), newStock, request.getUpdateType());

    // Check if stock is below minimum
    if (newStock <= product.getMinimumStock()) {
      log.warn("Low stock alert for product {}: Current stock {} is below minimum {}",
          productId, newStock, product.getMinimumStock());
      // Here you could trigger alerts or notifications
    }
  }

  @Override
  @Transactional(readOnly = true)
  public AvailabilityResponse checkAvailability(String productId, Integer requestedQuantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

    boolean isAvailable = product.getCurrentStock() >= requestedQuantity;
    String message = isAvailable ?
        "Product is available" :
        "Insufficient stock. Available: " + product.getCurrentStock();

    return new AvailabilityResponse(
        productId,
        isAvailable,
        product.getCurrentStock(),
        message
    );
  }

  @Override
  public Page<ProductDTO> getProducts(Pageable pageable) {
    return productRepository.findAll(pageable).map(productMapper::toDTO);
  }
}
