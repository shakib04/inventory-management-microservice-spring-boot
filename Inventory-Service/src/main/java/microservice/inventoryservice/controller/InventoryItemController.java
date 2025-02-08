package microservice.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import microservice.inventoryservice.dto.InventoryItemDTO;
import microservice.inventoryservice.service.InventoryItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory-items")
@RequiredArgsConstructor
public class InventoryItemController {

  private final InventoryItemService inventoryItemService;

  @PostMapping
  public ResponseEntity<InventoryItemDTO> createInventoryItem(@RequestBody InventoryItemDTO dto) {
    return ResponseEntity.ok(inventoryItemService.createInventoryItem(dto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<InventoryItemDTO> getInventoryItem(@PathVariable String id) {
    return ResponseEntity.ok(inventoryItemService.getInventoryItem(id));
  }

  @GetMapping
  public Page<InventoryItemDTO> getInventoryItems(
      @RequestParam(required = false) String productName,
      @RequestParam(required = false) Integer quantityLessThan,
      Pageable pageable
  ) {
    return inventoryItemService.findWithFilters(
        productName,
        quantityLessThan,
        pageable
    );
  }

  @PutMapping("/{id}")
  public ResponseEntity<InventoryItemDTO> updateInventoryItem(
      @PathVariable String id, @RequestBody InventoryItemDTO dto) {
    return ResponseEntity.ok(inventoryItemService.updateInventoryItem(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteInventoryItem(@PathVariable String id) {
    inventoryItemService.deleteInventoryItem(id);
    return ResponseEntity.noContent().build();
  }
}

