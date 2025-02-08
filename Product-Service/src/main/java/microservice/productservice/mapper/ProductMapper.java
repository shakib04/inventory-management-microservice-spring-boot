package microservice.productservice.mapper;

import java.util.List;
import microservice.productservice.dto.ProductDTO;
import microservice.productservice.entity.Product;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "currentStock", constant = "0")
  Product toEntity(ProductDTO request);

  ProductDTO toDTO(Product product);

  List<ProductDTO> toDTOList(List<Product> products);

  // Method to map Page<Entity> to Page<DTO>
  default Page<ProductDTO> toProductDTOPage(Page<Product> page) {
    return page.map(this::toDTO);
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateProductFromRequest(ProductDTO request, @MappingTarget Product product);
}
