package shinhan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = {"shinhan.server_parent", "shinhan.server_common"})
@EnableJpaRepositories(basePackages={"shinhan.server_parent","shinhan.server_common.domain"})
@EnableMongoRepositories(basePackages = {"shinhan.server_common.notification.mongo"})
public class ServerParentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerParentApplication.class, args);
    }


}