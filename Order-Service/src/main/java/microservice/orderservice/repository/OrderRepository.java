package microservice.orderservice.repository;

import java.util.List;
import microservice.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
  List<Order> findByCustomerId(String customerId);
}
