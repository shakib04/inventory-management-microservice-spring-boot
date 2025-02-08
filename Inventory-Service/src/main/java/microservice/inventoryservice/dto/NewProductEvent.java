package microservice.inventoryservice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProductEvent {
  private String productId;
  private String productName;
  private Integer defaultReorderLevel;
  private Integer defaultReorderQuantity;
  private LocalDateTime createdAt;
}
