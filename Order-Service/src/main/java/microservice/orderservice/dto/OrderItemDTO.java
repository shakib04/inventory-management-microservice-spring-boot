package microservice.orderservice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemDTO {
  private String productId;
  private String productName;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal subtotal;
}