package microservice.orderservice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import microservice.orderservice.enums.OrderStatus;

@Data
public class OrderResponse {
  private String id;
  private String orderNumber;
  private OrderStatus status;
  private BigDecimal totalAmount;
  private List<OrderItemDTO> items;
}
