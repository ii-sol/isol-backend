package shinhan.server_child.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class RabbitConfig {

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("${ec2-13-124-164-1.ap-northeast-2.compute.amazonaws.com}");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("${new_user}");
        connectionFactory.setPassword("${new_password}");
        // 다른 연결 설정도 필요에 따라 추가할 수 있습니다.
        return connectionFactory;
    }
    @Bean
    public Queue queue(){
        return new Queue("alarm");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        return new RabbitTemplate();
    }
}
