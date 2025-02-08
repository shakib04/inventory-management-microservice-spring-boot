package microservice.orderservice.rabbit;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class RabbitMQErrorHandler implements RabbitListenerErrorHandler {

  @Override
  public Object handleError(Message amqpMessage,
                            org.springframework.messaging.Message<?> message,
                            ListenerExecutionFailedException exception) {
    log.error("Error processing message: {}", amqpMessage, exception);

    // You might want to send to a dead letter queue
    // or implement retry logic
    return null;
  }
}
