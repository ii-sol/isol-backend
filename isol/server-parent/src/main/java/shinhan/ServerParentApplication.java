package shinhan;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import shinhan.server_common.notification.entity.Notification;

@SpringBootApplication
public class ServerParentApplication implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate ;

    public ServerParentApplication(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerParentApplication.class, args);
    }

    @Bean
    public Queue queue(@Value("${rabbitmq.queue}") String queueName) {
        return new Queue(queueName, false);
    }
    @Value("${rabbitmq.queue}")
    private String queueName;

    @Override
    public void run(String... args) throws Exception {
        Notification notification = Notification.builder().message("chiod").sender("child").receiverSerialNumber(123123L).build();
        rabbitTemplate.convertAndSend(queueName, notification);
    }

}