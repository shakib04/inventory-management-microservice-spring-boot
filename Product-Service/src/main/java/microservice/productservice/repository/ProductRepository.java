package microservice.productservice.repository;

import java.util.Optional;
import microservice.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
  Optional<Product> findBySku(String sku);
}
