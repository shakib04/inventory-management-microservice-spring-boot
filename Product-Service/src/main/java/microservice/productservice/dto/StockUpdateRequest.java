package microservice.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.productservice.enums.StockUpdateType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockUpdateRequest {
  private Integer quantity;
  private String warehouseId;
  private StockUpdateType updateType; // ADD, SUBTRACT, SET
}
