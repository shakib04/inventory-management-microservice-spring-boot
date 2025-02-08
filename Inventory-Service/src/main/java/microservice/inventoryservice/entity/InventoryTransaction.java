package microservice.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import microservice.inventoryservice.enums.TransactionType;

@Entity
@Table(name = "inventory_transactions")
@Data
public class InventoryTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "inventory_item_id")
  private InventoryItem inventoryItem;

  @Enumerated(EnumType.STRING)
  private TransactionType type;

  private Integer quantity;
  private String referenceId;  // Order ID or PO ID
  private String notes;
  private LocalDateTime transactionDate;
}
