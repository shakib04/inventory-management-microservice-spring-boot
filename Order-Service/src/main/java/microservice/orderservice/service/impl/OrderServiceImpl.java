package microservice.orderservice.service.impl;

import feign.FeignException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microservice.orderservice.config.RabbitMQConfig;
import microservice.orderservice.dto.AvailabilityResponse;
import microservice.orderservice.dto.OrderCreatedEvent;
import microservice.orderservice.dto.OrderItemDTO;
import microservice.orderservice.dto.OrderItemEvent;
import microservice.orderservice.dto.OrderItemRequest;
import microservice.orderservice.dto.OrderRequest;
import microservice.orderservice.dto.OrderResponse;
import microservice.orderservice.dto.ProductDTO;
import microservice.orderservice.dto.StockUpdateRequest;
import microservice.orderservice.entity.Order;
import microservice.orderservice.entity.OrderItem;
import microservice.orderservice.enums.OrderStatus;
import microservice.orderservice.exception.InsufficientStockException;
import microservice.orderservice.exception.InvalidOrderStateException;
import microservice.orderservice.exception.OrderNotFoundException;
import microservice.orderservice.feign.ProductServiceClient;
import microservice.orderservice.repository.OrderRepository;
import microservice.orderservice.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final ProductServiceClient productServiceClient;
  private final RabbitTemplate rabbitTemplate;


  @Override
  public OrderResponse createOrder(OrderRequest orderRequest) {
    // Validate all products availability
    validateProductsAvailability(orderRequest.getItems());

    // Create order
    Order order = new Order();
    order.setCustomerId(orderRequest.getCustomerId());
    order.setOrderDate(LocalDateTime.now());
    order.setStatus(OrderStatus.PENDING);
    order.setOrderNumber(generateOrderNumber());

    // Create order items
    List<OrderItem> orderItems = orderRequest.getItems().stream()
        .map(item -> createOrderItem(item, order))
        .collect(Collectors.toList());

    order.setOrderItems(orderItems);
    order.setTotalAmount(calculateTotalAmount(orderItems));

    Order savedOrder = orderRepository.save(order);

    // Create event
    OrderCreatedEvent event = new OrderCreatedEvent(
        order.getId(),
        order.getCustomerId(),
        order.getStatus(),
        order.getOrderDate(),
        order.getTotalAmount(),
        mapToOrderItemEvents(order.getOrderItems())
    );

    // Publish event
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.ORDER_EXCHANGE,
        RabbitMQConfig.INVENTORY_ROUTING_KEY,
        event
    );
    return mapToOrderResponse(savedOrder);
  }

  private List<OrderItemEvent> mapToOrderItemEvents(List<OrderItem> items) {
    return items.stream()
        .map(item -> new OrderItemEvent(
            item.getProductId(),
            item.getQuantity(),
            item.getUnitPrice(),
            item.getSubtotal()
        ))
        .collect(Collectors.toList());
  }

  private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
    return orderItems.stream().map(orderItem ->
            orderItem.getUnitPrice().multiply(
                BigDecimal.valueOf(orderItem.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private String generateOrderNumber() {
    return UUID.randomUUID().toString();
  }

  private OrderItem createOrderItem(OrderItemRequest itemRequest, Order order) {
    // Get product details from product service
    ProductDTO product = productServiceClient.getProduct(itemRequest.getProductId())
        .getBody();

    OrderItem item = new OrderItem();
    item.setOrder(order);
    item.setProductId(itemRequest.getProductId());
    item.setQuantity(itemRequest.getQuantity());
    item.setUnitPrice(product.getUnitPrice());
    item.setSubtotal(product.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));

    return item;
  }

  private void validateProductsAvailability(List<OrderItemRequest> items) {
    for (OrderItemRequest item : items) {
      AvailabilityResponse availability = productServiceClient.checkAvailability(
          item.getProductId(),
          item.getQuantity()
      ).getBody();

      if (availability == null || !availability.isAvailable()) {
        throw new InsufficientStockException(
            "Product " + item.getProductId() + " is not available in requested quantity");
      }
    }
  }


  @Override
  public OrderResponse getOrder(String orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
    return mapToOrderResponse(order);
  }

  @Override
  public List<OrderResponse> getCustomerOrders(String customerId) {
    List<Order> orders = orderRepository.findByCustomerId(customerId);
    return orders.stream()
        .map(this::mapToOrderResponse)
        .collect(Collectors.toList());
  }

  @Override
  public OrderResponse cancelOrder(String orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

    // Can only cancel orders that are PENDING or CONFIRMED
    if (order.getStatus() == OrderStatus.SHIPPED ||
        order.getStatus() == OrderStatus.DELIVERED) {
      throw new InvalidOrderStateException(
          "Cannot cancel order in " + order.getStatus() + " state");
    }

    // Update product stock
    restoreProductStock(order.getOrderItems());

    order.setStatus(OrderStatus.CANCELLED);
    Order cancelledOrder = orderRepository.save(order);
    return mapToOrderResponse(cancelledOrder);
  }

  @Override
  public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

    // Validate status transition
    validateStatusTransition(order.getStatus(), newStatus);

    order.setStatus(newStatus);
    Order updatedOrder = orderRepository.save(order);
    return mapToOrderResponse(updatedOrder);
  }

  private void restoreProductStock(List<OrderItem> orderItems) {
    for (OrderItem item : orderItems) {
      try {
        // Call product service to restore stock
        StockUpdateRequest stockUpdate = new StockUpdateRequest(
            item.getQuantity(),
            "RESTORE"
        );
        productServiceClient.updateStock(item.getProductId(), stockUpdate);
      } catch (FeignException e) {
        // Log error but continue with cancellation
        log.error("Failed to restore stock for product: " + item.getProductId(), e);
      }
    }
  }

  private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
    // Define valid transitions
    if (currentStatus == OrderStatus.CANCELLED ||
        currentStatus == OrderStatus.DELIVERED) {
      throw new InvalidOrderStateException(
          "Cannot change status of " + currentStatus + " order");
    }

    // Can't go backwards in fulfillment
    if (currentStatus == OrderStatus.SHIPPED &&
        newStatus == OrderStatus.CONFIRMED) {
      throw new InvalidOrderStateException(
          "Cannot change shipped order to confirmed status");
    }
  }

  private OrderResponse mapToOrderResponse(Order order) {
    OrderResponse response = new OrderResponse();
    response.setId(order.getId());
    response.setOrderNumber(order.getOrderNumber());
    response.setStatus(order.getStatus());
    response.setTotalAmount(order.getTotalAmount());

    List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
        .map(this::mapToOrderItemDTO)
        .collect(Collectors.toList());
    response.setItems(itemDTOs);

    return response;
  }

  private OrderItemDTO mapToOrderItemDTO(OrderItem item) {
    OrderItemDTO dto = new OrderItemDTO();
    dto.setProductId(item.getProductId());
    dto.setQuantity(item.getQuantity());
    dto.setUnitPrice(item.getUnitPrice());
    dto.setSubtotal(item.getSubtotal());

    // Get product name from product service
    try {
      ProductDTO product = productServiceClient.getProduct(item.getProductId())
          .getBody();
      if (product != null) {
        dto.setProductName(product.getName());
      }
    } catch (FeignException e) {
      // If product service is down, continue with null product name
      log.warn("Could not fetch product details for id: " + item.getProductId(), e);
      dto.setProductName("Product details unavailable");
    }

    return dto;
  }

}
