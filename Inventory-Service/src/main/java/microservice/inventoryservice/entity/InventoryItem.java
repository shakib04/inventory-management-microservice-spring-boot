package microservice.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_items")
@Data
public class InventoryItem {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String productId;
  private String productName;
  private String warehouseId;
  private Integer quantity;
  private Integer reorderLevel;
  private Integer reorderQuantity;

  private LocalDateTime lastRestockDate;
  private LocalDateTime lastCountDate;
}
