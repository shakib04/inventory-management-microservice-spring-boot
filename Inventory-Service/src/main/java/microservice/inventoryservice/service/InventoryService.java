package microservice.inventoryservice.service;

import java.util.List;
import microservice.inventoryservice.dto.InventoryItemDTO;
import microservice.inventoryservice.dto.InventoryLevelResponse;
import microservice.inventoryservice.dto.StockMovementRequest;

public interface InventoryService {
  InventoryItemDTO recordStockMovement(StockMovementRequest request);
  InventoryLevelResponse getProductInventory(String productId);
  List<InventoryItemDTO> getWarehouseInventory(String warehouseId);
  List<InventoryItemDTO> checkReorderNeeds();
}
