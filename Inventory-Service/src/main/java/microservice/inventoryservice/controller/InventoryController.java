package microservice.inventoryservice.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import microservice.inventoryservice.dto.InventoryItemDTO;
import microservice.inventoryservice.dto.InventoryLevelResponse;
import microservice.inventoryservice.exception.WarehouseNotFoundException;
import microservice.inventoryservice.service.InventoryService;
import microservice.inventoryservice.service.impl.InventoryServiceImpl;
import microservice.inventoryservice.dto.StockMovementRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

  private final InventoryService inventoryService;

  @PostMapping("/movement")
  public ResponseEntity<InventoryItemDTO> recordStockMovement(
      @RequestBody StockMovementRequest request) {
    return ResponseEntity.ok(inventoryService.recordStockMovement(request));
  }

  @GetMapping("/product/{productId}")
  public ResponseEntity<InventoryLevelResponse> getProductInventory(
      @PathVariable String productId) {
    return ResponseEntity.ok(inventoryService.getProductInventory(productId));
  }

  @GetMapping("/warehouse/{warehouseId}/products")
  public ResponseEntity<List<InventoryItemDTO>> getWarehouseInventory(
      @PathVariable String warehouseId) {
    try {
      List<InventoryItemDTO> inventory = inventoryService.getWarehouseInventory(warehouseId);
      return ResponseEntity.ok(inventory);
    } catch (WarehouseNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/reorder-check")
  public ResponseEntity<List<InventoryItemDTO>> checkReorderNeeds() {
    return ResponseEntity.ok(inventoryService.checkReorderNeeds());
  }
}
