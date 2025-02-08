package microservice.inventoryservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelledEvent implements Serializable {
  private String orderId;
  private String reason;
  private LocalDateTime timestamp;
}
