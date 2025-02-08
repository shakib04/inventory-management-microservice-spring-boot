package microservice.inventoryservice.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import microservice.inventoryservice.enums.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent implements Serializable {
  private String orderId;
  private String customerId;
  private OrderStatus status;
  private LocalDateTime orderDate;
  private BigDecimal totalAmount;
  private List<OrderItemEvent> items;
}

