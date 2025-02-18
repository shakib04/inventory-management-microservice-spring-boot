package microservice.inventoryservice.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductRabbitMQConfig {

  public static final String QUEUE = "product.queue";
  public static final String EXCHANGE = "product.exchange";
  public static final String ROUTING_KEY = "product.created";

  @Bean
  public Queue queue() {
    return new Queue(QUEUE);
  }

  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE);
  }

  @Bean
  public Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(ROUTING_KEY);
  }
}
