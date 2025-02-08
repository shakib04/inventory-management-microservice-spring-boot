package microservice.orderservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.orderservice.enums.OrderStatus;

// You can also create other order-related events
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusChangedEvent implements Serializable {
  private String orderId;
  private OrderStatus oldStatus;
  private OrderStatus newStatus;
  private LocalDateTime timestamp;
}
