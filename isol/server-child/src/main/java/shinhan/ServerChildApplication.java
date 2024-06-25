package shinhan;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import shinhan.server_common.notification.entity.Notification;

@SpringBootApplication
public class ServerChildApplication implements CommandLineRunner{
    private final RabbitTemplate rabbitTemplate ;

    public ServerChildApplication(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerChildApplication.class, args);
    }

    @Bean
    public Queue queue(@Value("${rabbitmq.queue}") String queueName) {
        return new Queue(queueName, false);
    }

    @Value("${rabbitmq.queue}")
    private String queueName;

    @Override
    public void run(String... args) throws Exception {
        rabbitTemplate.convertAndSend(queueName, "Hello from Producer!");
    }
}