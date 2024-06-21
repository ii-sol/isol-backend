package shinhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"shinhan.server_child", "shinhan.server_common"})
@EnableJpaRepositories(basePackages={"shinhan.server_child","shinhan.server_common.domain"})
@EnableMongoRepositories(basePackages = {"shinhan.server_common.notification.mongo"})
public class ServerChildApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerChildApplication.class, args);
    }
}