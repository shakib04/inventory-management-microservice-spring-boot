package microservice.orderservice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemRequest {
  private String productId;
  private Integer quantity;
}
