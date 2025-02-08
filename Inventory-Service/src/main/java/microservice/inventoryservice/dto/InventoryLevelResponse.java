package microservice.inventoryservice.dto;

import java.util.Map;
import lombok.Data;

@Data
public class InventoryLevelResponse {
  private String productId;
  private String productName;
  private Map<String, Integer> warehouseStocks;  // warehouseId -> quantity
  private Integer totalStock;
}
