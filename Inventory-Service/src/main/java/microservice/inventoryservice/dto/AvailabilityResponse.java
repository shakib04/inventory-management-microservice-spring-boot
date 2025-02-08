package microservice.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityResponse {
  private String productId;
  private boolean available;
  private Integer currentStock;
  private String message;
}
