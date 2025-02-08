package microservice.orderservice.controller;

import microservice.orderservice.dto.OrderRequest;
import microservice.orderservice.dto.OrderResponse;
import microservice.orderservice.enums.OrderStatus;
import microservice.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
    return ResponseEntity.ok(orderService.createOrder(orderRequest));
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
    return ResponseEntity.ok(orderService.getOrder(orderId));
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<OrderResponse>> getCustomerOrders(
      @PathVariable String customerId) {
    return ResponseEntity.ok(orderService.getCustomerOrders(customerId));
  }

  @PutMapping("/{orderId}/cancel")
  public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId) {
    return ResponseEntity.ok(orderService.cancelOrder(orderId));
  }

  @PutMapping("/{orderId}/status")
  public ResponseEntity<OrderResponse> updateOrderStatus(
      @PathVariable String orderId,
      @RequestParam OrderStatus status) {
    return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
  }
}