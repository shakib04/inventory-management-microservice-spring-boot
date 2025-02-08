package microservice.inventoryservice.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEvent implements Serializable {
  private String productId;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal subtotal;
}
