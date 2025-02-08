package microservice.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import microservice.orderservice.enums.OrderStatus;

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

