package shinhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableMongoRepositories(basePackages = {"shinhan.server_common.notification.mongo"})
public class ServerChildApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerChildApplication.class, args);
    }
}