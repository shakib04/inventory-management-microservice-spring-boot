package microservice.inventoryservice.mapper;

import java.util.List;
import microservice.inventoryservice.dto.InventoryItemDTO;
import microservice.inventoryservice.dto.StockMovementRequest;
import microservice.inventoryservice.entity.InventoryItem;
import microservice.inventoryservice.entity.InventoryTransaction;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryMapper {

  @Mapping(target = "productName", ignore = true)
    // Will be set manually after fetching from Product Service
  InventoryItemDTO toDTO(InventoryItem inventoryItem);

  List<InventoryItemDTO> toDTOList(List<InventoryItem> items);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "lastRestockDate", ignore = true)
  @Mapping(target = "lastCountDate", ignore = true)
  InventoryItem toEntity(InventoryItemDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "inventoryItem", ignore = true)
  @Mapping(target = "transactionDate", expression = "java(java.time.LocalDateTime.now())")
  InventoryTransaction toTransactionEntity(StockMovementRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromDto(InventoryItemDTO dto, @MappingTarget InventoryItem entity);
}
