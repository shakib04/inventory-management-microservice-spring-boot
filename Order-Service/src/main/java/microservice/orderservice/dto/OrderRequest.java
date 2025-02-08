package microservice.orderservice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {
  private String customerId;
  private List<OrderItemRequest> items;
}
