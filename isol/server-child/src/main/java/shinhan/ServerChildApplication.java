package shinhan;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerChildApplication {
//    @Autowired
//    private final RabbitTemplate rabbitTemplate ;
//
//    public ServerChildApplication(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
@Bean
public ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setHost("${ec2-13-124-164-1.ap-northeast-2.compute.amazonaws.com}"); // RabbitMQ host
    connectionFactory.setPort(5672); // RabbitMQ port
    connectionFactory.setUsername("new_user"); // RabbitMQ username
    connectionFactory.setPassword("new_password"); // RabbitMQ password
    // Other connection settings can be added as needed
    return connectionFactory;
}

    public static void main(String[] args) {
        SpringApplication.run(ServerChildApplication.class, args);
    }
}