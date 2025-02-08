package microservice.orderservice.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  private String productId;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal subtotal;
}
