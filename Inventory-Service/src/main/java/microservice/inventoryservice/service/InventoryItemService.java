package microservice.inventoryservice.service;

import java.util.List;
import microservice.inventoryservice.dto.InventoryItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryItemService {
  InventoryItemDTO createInventoryItem(InventoryItemDTO dto);

  InventoryItemDTO getInventoryItem(String id);

  List<InventoryItemDTO> getInventoryItemsByProductId(String productId);

  List<InventoryItemDTO> getAllInventoryItems();

  InventoryItemDTO updateInventoryItem(String id, InventoryItemDTO dto);

  void deleteInventoryItem(String id);

  Page<InventoryItemDTO> findWithFilters(String productName, Integer quantityLessThan, Pageable pageable);
}
