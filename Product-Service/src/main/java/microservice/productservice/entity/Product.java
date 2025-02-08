package microservice.productservice.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;


@Entity
@Table(name = "products")
@Data
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(unique = true)
  private String sku;

  private String name;
  private String description;
  private String category;
  private BigDecimal unitPrice;
  private Integer minimumStock;
  private Integer currentStock;
}
