package microservice.orderservice.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
  private String id;
  private String sku;
  private String name;
  private BigDecimal unitPrice;
  private Integer stock;
}
