package microservice.orderservice.service;

import java.util.List;
import microservice.orderservice.dto.OrderRequest;
import microservice.orderservice.dto.OrderResponse;
import microservice.orderservice.enums.OrderStatus;

public interface OrderService {
  OrderResponse createOrder(OrderRequest orderRequest);

  OrderResponse getOrder(String orderId);

  List<OrderResponse> getCustomerOrders(String customerId);

  OrderResponse cancelOrder(String orderId);

  OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus);
}
