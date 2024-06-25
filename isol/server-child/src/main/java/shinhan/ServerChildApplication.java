package shinhan;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shinhan.server_common.notification.entity.Notification;

@SpringBootApplication
public class ServerChildApplication implements CommandLineRunner{
    @Autowired
    private final RabbitTemplate rabbitTemplate ;

    public ServerChildApplication(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerChildApplication.class, args);
    }

    

    @Value("${rabbitmq.message}")
    private String queueName;

    @Override
    public void run(String... args) throws Exception {
//            notification = Notification.builder()
//                .createDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime())
//                .message("testsss").receiverSerialNumber(12312312L).sender("asd").build();
            rabbitTemplate.convertAndSend("alarm", "sdfsdf");
//            System.out.println("객체 전송: " + notification);

    }
}