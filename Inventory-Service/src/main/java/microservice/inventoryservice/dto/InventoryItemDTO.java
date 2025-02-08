package microservice.inventoryservice.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class InventoryItemDTO {
  private String id;
  private String productId;
  private String productName;  // Fetched from Product Service
  private String warehouseId;
  private Integer quantity;
  private Integer reorderLevel;
  private Integer reorderQuantity;
  private LocalDateTime lastRestockDate;
}
