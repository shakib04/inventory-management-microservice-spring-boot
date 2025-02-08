package microservice.inventoryservice.repository;

import java.util.List;
import microservice.inventoryservice.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransactionRepository extends
    JpaRepository<InventoryTransaction, String> {
  List<InventoryTransaction> findByInventoryItemId(String inventoryItemId);
}
