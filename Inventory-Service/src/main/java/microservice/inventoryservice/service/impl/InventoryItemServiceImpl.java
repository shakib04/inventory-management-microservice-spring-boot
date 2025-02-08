package microservice.inventoryservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import microservice.inventoryservice.dto.InventoryItemDTO;
import microservice.inventoryservice.dto.NewProductEvent;
import microservice.inventoryservice.entity.InventoryItem;
import microservice.inventoryservice.exception.InventoryNotFoundException;
import microservice.inventoryservice.feign.ProductServiceClient;
import microservice.inventoryservice.mapper.InventoryMapper;
import microservice.inventoryservice.repository.InventoryItemRepository;
import microservice.inventoryservice.service.InventoryItemService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements InventoryItemService {

  private final InventoryItemRepository inventoryItemRepository;
  private final InventoryMapper inventoryItemMapper;
  private final ProductServiceClient productServiceClient;

  @RabbitListener(queues = "product.queue")
  public void handleNewProduct(NewProductEvent event) {
    InventoryItem item = new InventoryItem();
    item.setProductId(event.getProductId());
    item.setProductName(event.getProductName());
    item.setQuantity(0);
    item.setReorderLevel(event.getDefaultReorderLevel());
    item.setReorderQuantity(event.getDefaultReorderQuantity());
    item.setLastCountDate(event.getCreatedAt());

    inventoryItemRepository.save(item);
  }

  @Override
  public InventoryItemDTO createInventoryItem(InventoryItemDTO dto) {
    InventoryItem entity = inventoryItemMapper.toEntity(dto);
    entity.setLastRestockDate(LocalDateTime.now());
    entity.setLastCountDate(LocalDateTime.now());
    return inventoryItemMapper.toDTO(inventoryItemRepository.save(entity));
  }

  @Override
  public InventoryItemDTO getInventoryItem(String id) {
    return inventoryItemRepository.findById(id)
        .map(inventoryItemMapper::toDTO)
        .orElseThrow(() -> new InventoryNotFoundException("Inventory item not found"));
  }

  @Override
  public List<InventoryItemDTO> getInventoryItemsByProductId(String productId) {
    return inventoryItemRepository.findByProductId(productId).stream()
       .map(inventoryItemMapper::toDTO)
       .collect(Collectors.toList());
  }

  @Override
  public List<InventoryItemDTO> getAllInventoryItems() {
    return inventoryItemRepository.findAll().stream()
        .map(inventoryItemMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public InventoryItemDTO updateInventoryItem(String id, InventoryItemDTO dto) {
    InventoryItem item = inventoryItemRepository.findById(id)
        .orElseThrow(() -> new InventoryNotFoundException("Inventory item not found"));

    inventoryItemMapper.updateEntityFromDto(dto, item);
    return inventoryItemMapper.toDTO(inventoryItemRepository.save(item));
  }

  @Override
  public void deleteInventoryItem(String id) {
    inventoryItemRepository.deleteById(id);
  }

  @Override
  public Page<InventoryItemDTO> findWithFilters(String productName, Integer quantityLessThan,
                                                Pageable pageable) {
    return inventoryItemRepository.findByProductNameAndQuantity(productName, quantityLessThan, pageable);
  }
}
