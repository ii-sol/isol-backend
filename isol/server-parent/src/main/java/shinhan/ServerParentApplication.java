package shinhan;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerParentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerParentApplication.class, args);
    }

    @RabbitListener(queues = "asdfsdf")
    public void listen(String message) {
        System.out.println("Received: " + message);
    }
}