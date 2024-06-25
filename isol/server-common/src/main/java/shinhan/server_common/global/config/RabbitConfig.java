package shinhan.server_common.global.config;

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
    public Queue queue(){
        return new Queue("alarm");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        return new RabbitTemplate();
    }
}
