package microservice.inventoryservice.service.impl;

import feign.FeignException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microservice.inventoryservice.dto.InventoryItemDTO;
import microservice.inventoryservice.repository.InventoryItemRepository;
import microservice.inventoryservice.dto.InventoryLevelResponse;
import microservice.inventoryservice.mapper.InventoryMapper;
import microservice.inventoryservice.repository.InventoryTransactionRepository;
import microservice.inventoryservice.dto.OrderCreatedEvent;
import microservice.inventoryservice.dto.ProductDTO;
import microservice.inventoryservice.dto.StockMovementRequest;
import microservice.inventoryservice.dto.StockUpdateRequest;
import microservice.inventoryservice.entity.InventoryItem;
import microservice.inventoryservice.entity.InventoryTransaction;
import microservice.inventoryservice.enums.StockUpdateType;
import microservice.inventoryservice.exception.InsufficientStockException;
import microservice.inventoryservice.exception.InventoryNotFoundException;
import microservice.inventoryservice.feign.ProductServiceClient;
import microservice.inventoryservice.service.InventoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class InventoryServiceImpl implements InventoryService {

  private final InventoryItemRepository inventoryItemRepository;
  private final InventoryTransactionRepository transactionRepository;
  private final ProductServiceClient productServiceClient;
  private final InventoryMapper inventoryMapper;

  @Override
  public InventoryItemDTO recordStockMovement(StockMovementRequest request) {
    // Get or create inventory item
    InventoryItem inventoryItem = inventoryItemRepository
        .findByProductIdAndWarehouseId(request.getProductId(), request.getWarehouseId())
        .orElseGet(() -> createNewInventoryItem(request));

    // Update quantity
    updateInventoryQuantity(inventoryItem, request);

    // Record transaction
    InventoryTransaction transaction = new InventoryTransaction();
    transaction.setInventoryItem(inventoryItem);
    transaction.setType(request.getType());
    transaction.setQuantity(request.getQuantity());
    transaction.setReferenceId(request.getReferenceId());
    transaction.setNotes(request.getNotes());
    transaction.setTransactionDate(LocalDateTime.now());
    transactionRepository.save(transaction);

    // Update product service
    updateProductStock(inventoryItem);

    return inventoryMapper.toDTO(inventoryItem);
  }

  @Override
  public InventoryLevelResponse getProductInventory(String productId) {
    List<InventoryItem> items = inventoryItemRepository.findByProductId(productId);

    // Get product details
    ProductDTO product = productServiceClient.getProduct(productId).getBody();

    InventoryLevelResponse response = new InventoryLevelResponse();
    response.setProductId(productId);
    response.setProductName(product.getName());

    Map<String, Integer> warehouseStocks = items.stream()
        .collect(Collectors.toMap(
            InventoryItem::getWarehouseId,
            InventoryItem::getQuantity
        ));
    response.setWarehouseStocks(warehouseStocks);

    Integer totalStock = warehouseStocks.values().stream()
        .mapToInt(Integer::intValue)
        .sum();
    response.setTotalStock(totalStock);

    return response;
  }

  @Override
  @Transactional(readOnly = true)
  public List<InventoryItemDTO> getWarehouseInventory(String warehouseId) {
    // Get all inventory items for the warehouse
    List<InventoryItem> items = inventoryItemRepository.findByWarehouseId(warehouseId);

    // Convert to DTOs
    List<InventoryItemDTO> dtos = inventoryMapper.toDTOList(items);

    // Fetch and set product names
    dtos.forEach(dto -> {
      try {
        ResponseEntity<ProductDTO> productResponse =
            productServiceClient.getProduct(dto.getProductId());

        if (productResponse.getBody() != null) {
          dto.setProductName(productResponse.getBody().getName());
        } else {
          dto.setProductName("Product Not Found");
        }
      } catch (FeignException e) {
        log.error("Error fetching product details for ID: {}", dto.getProductId(), e);
        dto.setProductName("Error Fetching Product Details");
      }
    });

    return dtos;
  }

  // Helper method to create a new inventory item
  private InventoryItem createNewInventoryItem(StockMovementRequest request) {
    InventoryItem newItem = new InventoryItem();
    newItem.setProductId(request.getProductId());
    newItem.setWarehouseId(request.getWarehouseId());
    newItem.setQuantity(0);

    // Fetch product details to get reorder levels
    try {
      ResponseEntity<ProductDTO> productResponse =
          productServiceClient.getProduct(request.getProductId());

      if (productResponse.getBody() != null) {
        // Set reorder levels based on product settings
        newItem.setReorderLevel(productResponse.getBody().getMinimumStock());
        newItem.setReorderQuantity(
            productResponse.getBody().getMinimumStock() * 2); // Example reorder quantity
      }
    } catch (FeignException e) {
      log.error("Error fetching product details for new inventory item", e);
      // Set default values
      newItem.setReorderLevel(10);
      newItem.setReorderQuantity(20);
    }

    return inventoryItemRepository.save(newItem);
  }

  @Override
  public List<InventoryItemDTO> checkReorderNeeds() {
    return inventoryItemRepository.findAll().stream()
        .filter(item -> item.getQuantity() <= item.getReorderLevel())
        .map(inventoryMapper::toDTO)
        .collect(Collectors.toList());
  }

  private void updateInventoryQuantity(InventoryItem item, StockMovementRequest request) {
    switch (request.getType()) {
      case STOCK_IN:
        item.setQuantity(item.getQuantity() + request.getQuantity());
        item.setLastRestockDate(LocalDateTime.now());
        break;
      case STOCK_OUT:
        if (item.getQuantity() < request.getQuantity()) {
          throw new InsufficientStockException("Insufficient stock");
        }
        item.setQuantity(item.getQuantity() - request.getQuantity());
        break;
      case ADJUSTMENT:
        item.setQuantity(request.getQuantity());
        item.setLastCountDate(LocalDateTime.now());
        break;
    }
    inventoryItemRepository.save(item);
  }

  private void updateProductStock(InventoryItem item) {
    StockUpdateRequest stockUpdate = new StockUpdateRequest();
    stockUpdate.setQuantity(item.getQuantity());
    stockUpdate.setWarehouseId(item.getWarehouseId());
    stockUpdate.setUpdateType(StockUpdateType.SET);

    productServiceClient.updateStock(item.getProductId(), stockUpdate);
  }

  @RabbitListener(queues = "#{inventoryQueue.name}")
  public void handleOrderCreated(OrderCreatedEvent event) {
    log.info("Received order created event: {}", event.getOrderId());

    // Process each order item
    event.getItems().forEach(item -> {
      updateInventory(item.getProductId(), item.getQuantity());
    });
  }

  private void updateInventory(String productId, Integer quantity) {
    InventoryItem inventoryItem = inventoryItemRepository
        .findByProductId(productId)
        .stream().findFirst()
        .orElseThrow(() -> new InventoryNotFoundException(
            "Inventory not found for product: " + productId));

    inventoryItem.setQuantity(inventoryItem.getQuantity() - quantity);
    inventoryItemRepository.save(inventoryItem);
  }
}

