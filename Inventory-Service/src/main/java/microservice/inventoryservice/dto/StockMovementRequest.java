package microservice.inventoryservice.dto;

import lombok.Data;
import microservice.inventoryservice.enums.TransactionType;

@Data
public class StockMovementRequest {
  private String productId;
  private String warehouseId;
  private Integer quantity;
  private TransactionType type;
  private String referenceId;
  private String notes;
}
