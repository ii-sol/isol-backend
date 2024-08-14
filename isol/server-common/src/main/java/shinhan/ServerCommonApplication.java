package shinhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication

public class ServerCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerCommonApplication.class, args);
    }

//    @RabbitListener(queues = "hello")
//    public void listen(String message) {
//        System.out.println("Received: " + message);
//    }
}