package microservice.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  // Queue names as constants
  public static final String ORDER_QUEUE = "order_queue";
  public static final String INVENTORY_QUEUE = "inventory_queue";
  public static final String ORDER_EXCHANGE = "order_exchange";
  public static final String INVENTORY_ROUTING_KEY = "inventory_routing_key";

  @Bean
  public Queue orderQueue() {
    return new Queue(ORDER_QUEUE, true);
  }

  @Bean
  public Queue inventoryQueue() {
    return new Queue(INVENTORY_QUEUE, true);
  }

  @Bean
  public TopicExchange orderExchange() {
    return new TopicExchange(ORDER_EXCHANGE);
  }

  @Bean
  public Binding inventoryBinding(Queue inventoryQueue, TopicExchange orderExchange) {
    return BindingBuilder
        .bind(inventoryQueue)
        .to(orderExchange)
        .with(INVENTORY_ROUTING_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(converter());
    return rabbitTemplate;
  }
}
