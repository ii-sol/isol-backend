package shinhan;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication(scanBasePackages = {"shinhan.server_child", "shinhan.server_common"})
@EnableJpaRepositories(basePackages={"shinhan.server_child","shinhan.server_common.domain"})
@EnableMongoRepositories(basePackages = {"shinhan.server_common.notification.mongo"})
public class ServerChildApplication implements CommandLineRunner {

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