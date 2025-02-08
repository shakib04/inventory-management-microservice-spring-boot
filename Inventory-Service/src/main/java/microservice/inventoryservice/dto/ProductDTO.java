package microservice.inventoryservice.dto;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
public class ProductDTO {
  private String id;
  private String sku;
  private String name;
  private String description;
  private String category;
  private BigDecimal unitPrice;
  private Integer minimumStock;
}
