package microservice.inventoryservice.repository;

import java.util.List;
import java.util.Optional;
import microservice.inventoryservice.dto.InventoryItemDTO;
import microservice.inventoryservice.entity.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {
  List<InventoryItem> findByProductId(String productId);
  List<InventoryItem> findByWarehouseId(String warehouseId);
  Optional<InventoryItem> findByProductIdAndWarehouseId(String productId, String warehouseId);

  @Query("""
      select model from InventoryItem model
      where :productName is null or model.productName = :productName
      and :quantityLessThan is null or model.quantity < :quantityLessThan
      """)
  Page<InventoryItemDTO> findByProductNameAndQuantity(String productName, Integer quantityLessThan, Pageable pageable);
}
