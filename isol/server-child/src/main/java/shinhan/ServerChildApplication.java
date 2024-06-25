package shinhan;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerChildApplication{
    private final RabbitTemplate rabbitTemplate ;

    public ServerChildApplication(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerChildApplication.class, args);
    }

    @Value("${rabbitmq.queue}")
    private String queueName;
}